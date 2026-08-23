package net.smartz.skill;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smartz.attributes.ZombieConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ZombieFlintAndSteel {

    private static final Map<UUID, Integer> COOLDOWNS =
            new HashMap<>();


    @SubscribeEvent
    public static void onZombieSpawn(
            EntityJoinLevelEvent event) {

        if (event.getLevel().isClientSide()) {
            return;
        }

        if (!(event.getEntity() instanceof Zombie zombie)) {
            return;
        }

        if (!ZombieConfig.FLINT_ENABLED.get()) {
            return;
        }

        // ถ้ามี Flint อยู่แล้ว ไม่ต้องเพิ่ม
        if (zombie.getMainHandItem().is(Items.FLINT_AND_STEEL)
                || zombie.getOffhandItem().is(Items.FLINT_AND_STEEL)) {
            return;
        }

        // โอกาสเกิดมาพร้อม Flint
        if (ThreadLocalRandom.current().nextDouble()
                > ZombieConfig.FLINT_CHANCE.get()) {
            return;
        }

        ItemStack flint =
                new ItemStack(Items.FLINT_AND_STEEL);

        zombie.setItemSlot(
                EquipmentSlot.MAINHAND,
                flint
        );

        zombie.setDropChance(
                EquipmentSlot.MAINHAND,
                0.0F
        );

        COOLDOWNS.put(
                zombie.getUUID(),
                20
        );
    }

    @SubscribeEvent
    public static void onLivingTick(
            LivingEvent.LivingTickEvent event) {

        if (!(event.getEntity() instanceof Zombie zombie)) {
            return;
        }

        if (!ZombieSkillManager.hasSkill(
                zombie,
                ZombieSkill.FLINT_AND_STEEL)) {
            return;
        }

        if (zombie.level().isClientSide()) {
            return;
        }

        if (!ZombieConfig.FLINT_ENABLED.get()) {
            return;
        }

        // ต้องถือ Flint
        if (!zombie.getMainHandItem().is(
                Items.FLINT_AND_STEEL)) {
            return;
        }

        UUID uuid = zombie.getUUID();


        int cooldown =
                COOLDOWNS.getOrDefault(uuid, 0);

        if (cooldown > 0) {

            COOLDOWNS.put(
                    uuid,
                    cooldown - 1
            );

            return;
        }


        LivingEntity target =
                zombie.getTarget();

        if (target == null) {
            resetCooldown(zombie);
            return;
        }

        // ต้องเป็นผู้เล่น
        if (!(target instanceof net.minecraft.world.entity.player.Player)) {
            resetCooldown(zombie);
            return;
        }

        double distance =
                zombie.distanceTo(target);

        if (distance > ZombieConfig.FLINT_RANGE.get()) {
            resetCooldown(zombie);
            return;
        }


        if (!zombie.hasLineOfSight(target)) {
            resetCooldown(zombie);
            return;
        }


        tryLightPlayer(
                zombie,
                target
        );


        resetCooldown(zombie);
    }


    private static void tryLightPlayer(
            Zombie zombie,
            LivingEntity target) {

        Level level = zombie.level();

        double distance =
                zombie.distanceTo(target);

        // ต้องอยู่ในระยะ 1-2 บล็อก
        if (distance > ZombieConfig.FLINT_RANGE.get()) {
            return;
        }

        // ตำแหน่งผู้เล่น
        BlockPos playerPos =
                target.blockPosition();


        if (canPlaceFire(level, playerPos)) {

            placeFire(
                    level,
                    playerPos,
                    zombie
            );

            return;
        }

        Direction playerDirection =
                target.getDirection();

        BlockPos front =
                playerPos.relative(playerDirection);

        if (canPlaceFire(level, front)) {

            placeFire(
                    level,
                    front,
                    zombie
            );
        }
    }


    private static void resetCooldown(
            Zombie zombie) {

        COOLDOWNS.put(
                zombie.getUUID(),
                ZombieConfig.FLINT_COOLDOWN.get()
        );
    }

    private static boolean canPlaceFire(
            Level level,
            BlockPos pos) {

        if (!level.isEmptyBlock(pos)) {
            return false;
        }

        BlockPos below =
                pos.below();

        return level.getBlockState(below)
                .isFaceSturdy(
                        level,
                        below,
                        Direction.UP
                );
    }

    private static void placeFire(
            Level level,
            BlockPos pos,
            Zombie zombie) {

        level.setBlockAndUpdate(
                pos,
                Blocks.FIRE.defaultBlockState()
        );

        ItemStack flint =
                zombie.getMainHandItem();

        if (flint.is(Items.FLINT_AND_STEEL)) {

            flint.hurtAndBreak(
                    1,
                    zombie,
                    entity -> entity.broadcastBreakEvent(
                            EquipmentSlot.MAINHAND
                    )
            );
        }
    }
}