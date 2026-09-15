package net.smartz.skill;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smartz.attributes.ZombieConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ZombieBlockBreak {

    private static final Map<UUID, MiningData> MINING = new HashMap<>();


    @SubscribeEvent
    public static void onZombieSpawn(EntityJoinLevelEvent event) {

        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof Zombie zombie)) return;
        long day = event.getLevel().getDayTime() / 24000L;
        if(!(day >= ZombieConfig.START_DAY_BREAK_BLOCK.get())) return;
        if (!ZombieConfig.BLOCK_BREAK_ENABLED.get()) return;

        // ถ้ามี Pickaxe อยู่แล้ว
        if (isPickaxe(zombie.getMainHandItem()) || isPickaxe(zombie.getOffhandItem())) return;

        // โอกาสเกิดพร้อม Pickaxe
        if (ThreadLocalRandom.current().nextDouble() > ZombieConfig.PICKAXE_CHANCE.get()) return;

        givePickaxe(zombie);
    }


    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {

        if (!(event.getEntity() instanceof Zombie zombie)) return;
        if (!ZombieSkillManager.hasSkill(zombie, ZombieSkill.BREAK_BLOCK)) return;
        if (zombie.level().isClientSide()) return;
        if (!ZombieConfig.BLOCK_BREAK_ENABLED.get()) return;

        UUID uuid = zombie.getUUID();
        MiningData data = MINING.get(uuid);

        if (data != null) {

            if (!canContinueMining(zombie, data.pos)) {
                MINING.remove(uuid);
                return;
            }

            data.progress++;

            // Animation การทุบ Block
            int destroyStage = (int) ((data.progress / (double) data.requiredTicks) * 10);
            destroyStage = Math.min(9, destroyStage);

            zombie.level().destroyBlockProgress(zombie.getId(), data.pos, destroyStage);


            // ขุดเสร็จ
            if (data.progress >= data.requiredTicks) {

                breakBlock(zombie, data.pos);

                zombie.level().destroyBlockProgress(zombie.getId(), data.pos, -1);

                MINING.remove(uuid);
            }

            return;
        }


        // -------------------------
        // หา Block ที่ขวางทาง
        // -------------------------

        BlockPos target = findBlockToBreak(zombie);

        if (target == null) return;

        int requiredTicks = calculateMiningTime(zombie, target);

        MINING.put(uuid, new MiningData(target, requiredTicks));
    }


    private static BlockPos findBlockToBreak(Zombie zombie) {

        // ต้องมี Player เป็น Target
        if (!(zombie.getTarget() instanceof net.minecraft.world.entity.player.Player player)) {
            return null;
        }

        Level level = zombie.level();

        int range = ZombieConfig.BLOCK_BREAK_RANGE.get();

        // จำกัดระยะ Zombie กับ Player
        if (zombie.distanceTo(player) > range + 1.0D) return null;


        // ตำแหน่ง Zombie
        double startX = zombie.getX();
        double startY = zombie.getEyeY();
        double startZ = zombie.getZ();

        // ตำแหน่ง Player
        double endX = player.getX();
        double endY = player.getEyeY();
        double endZ = player.getZ();


        // Vector จาก Zombie ไป Player
        double dx = endX - startX;
        double dy = endY - startY;
        double dz = endZ - startZ;

        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (distance <= 1.5D) return null;


        // จำนวนจุดที่ใช้ตรวจระหว่าง Zombie กับ Player
        int steps = Math.max(1, (int) Math.ceil(distance * 4));


        for (int i = 1; i < steps; i++) {

            double progress = i / (double) steps;

            double x = startX + dx * progress;
            double y = startY + dy * progress;
            double z = startZ + dz * progress;

            BlockPos pos = BlockPos.containing(x, y, z);
            BlockState state = level.getBlockState(pos);


            // ไม่มี Block
            if (state.isAir()) continue;

            // Block ที่ห้ามทำลาย
            if (isUnbreakable(state)) continue;

            // Block ที่เกมกำหนดว่าทำลายไม่ได้
            if (state.getDestroySpeed(level, pos) < 0) continue;

            // ห้ามทำลายพื้นที่ Zombie ยืนอยู่
            if (pos.equals(zombie.blockPosition().below())) continue;

            // เจอ Block ขวางทาง
            return pos;
        }

        return null;
    }


    private static boolean canContinueMining(Zombie zombie, BlockPos pos) {

        // ต้องยังมี Player เป็น Target
        if (!(zombie.getTarget() instanceof net.minecraft.world.entity.player.Player player)) {
            return false;
        }

        // เช็กว่า Block เดิมยังขวางทางอยู่หรือไม่
        BlockPos newTarget = findBlockToBreak(zombie);

        if (newTarget == null) return false;
        if (!newTarget.equals(pos)) return false;

        Level level = zombie.level();
        BlockState state = level.getBlockState(pos);

        if (state.isAir()) return false;
        if (isUnbreakable(state)) return false;

        return state.getDestroySpeed(level, pos) >= 0;
    }


    private static int calculateMiningTime(Zombie zombie, BlockPos pos) {

        Level level = zombie.level();
        BlockState state = level.getBlockState(pos);

        float hardness = state.getDestroySpeed(level, pos);

        if (hardness < 0) return Integer.MAX_VALUE;


        ItemStack tool = zombie.getMainHandItem();

        float toolSpeed = tool.getDestroySpeed(state);

        if (toolSpeed <= 1.0F) {
            toolSpeed = 1.0F;
        }


        double speed = toolSpeed * ZombieConfig.BLOCK_BREAK_SPEED.get();
        double ticks = (hardness * 30.0D) / speed;

        return Math.max(1, (int) Math.ceil(ticks));
    }


    private static void breakBlock(Zombie zombie, BlockPos pos) {

        Level level = zombie.level();
        BlockState state = level.getBlockState(pos);

        if (isUnbreakable(state)) return;

        level.destroyBlock(pos, true, zombie);


        // Damage Pickaxe
        ItemStack tool = zombie.getMainHandItem();

        if (isPickaxe(tool)) {

            tool.hurtAndBreak(
                    1,
                    zombie,
                    entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND)
            );
        }
    }


    private static void givePickaxe(Zombie zombie) {

        double roll = ThreadLocalRandom.current().nextDouble();
        double accumulated = 0.0D;


        accumulated += ZombieConfig.WOODEN_PICKAXE_CHANCE.get();

        if (roll < accumulated) {
            giveTool(zombie, new ItemStack(Items.WOODEN_PICKAXE));
            return;
        }


        accumulated += ZombieConfig.STONE_PICKAXE_CHANCE.get();

        if (roll < accumulated) {
            giveTool(zombie, new ItemStack(Items.STONE_PICKAXE));
            return;
        }


        accumulated += ZombieConfig.IRON_PICKAXE_CHANCE.get();

        if (roll < accumulated) {
            giveTool(zombie, new ItemStack(Items.IRON_PICKAXE));
            return;
        }


        accumulated += ZombieConfig.GOLD_PICKAXE_CHANCE.get();

        if (roll < accumulated) {
            giveTool(zombie, new ItemStack(Items.GOLDEN_PICKAXE));
            return;
        }


        accumulated += ZombieConfig.DIAMOND_PICKAXE_CHANCE.get();

        if (roll < accumulated) {
            giveTool(zombie, new ItemStack(Items.DIAMOND_PICKAXE));
            return;
        }


        accumulated += ZombieConfig.NETHERITE_PICKAXE_CHANCE.get();

        if (roll < accumulated) {
            giveTool(zombie, new ItemStack(Items.NETHERITE_PICKAXE));
        }
    }


    private static void giveTool(Zombie zombie, ItemStack tool) {

        zombie.setItemSlot(EquipmentSlot.MAINHAND, tool);
        zombie.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }


    private static boolean isPickaxe(ItemStack stack) {

        return stack.is(Items.WOODEN_PICKAXE)
                || stack.is(Items.STONE_PICKAXE)
                || stack.is(Items.IRON_PICKAXE)
                || stack.is(Items.GOLDEN_PICKAXE)
                || stack.is(Items.DIAMOND_PICKAXE)
                || stack.is(Items.NETHERITE_PICKAXE);
    }


    private static boolean isUnbreakable(BlockState state) {

        ResourceLocation id = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(state.getBlock());

        if (id == null) return true;

        String blockId = id.toString();

        return ZombieConfig.UNBREAKABLE_BLOCKS.get().contains(blockId);
    }


    private static class MiningData {

        final BlockPos pos;
        final int requiredTicks;

        int progress = 0;


        MiningData(BlockPos pos, int requiredTicks) {
            this.pos = pos;
            this.requiredTicks = requiredTicks;
        }
    }
}