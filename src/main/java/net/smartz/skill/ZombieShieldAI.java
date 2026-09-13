package net.smartz.skill;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smartz.attributes.ZombieConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ZombieShieldAI {

    private static final Map<UUID, Integer> BLOCKING_ZOMBIES = new HashMap<>();
    private static final Map<UUID, Integer> REACTION_ZOMBIES = new HashMap<>();


    @SubscribeEvent
    public static void onZombieAttacked(LivingAttackEvent event) {

        // ระบบ Shield Block ต้องเปิด
        if (!ZombieConfig.SHIELD_BLOCK_ENABLED.get()) return;

        // ต้องเป็น Zombie
        if (!(event.getEntity() instanceof Zombie zombie)) return;

        // ต้องมี Shield อยู่ในมือซ้าย
        ItemStack shield = zombie.getOffhandItem();

        if (!shield.is(Items.SHIELD)) return;


        long day = zombie.level().getDayTime() / 24000L;
        long progressionDay = Math.max(0, day - ZombieConfig.START_DAY.get());

        double chance = ZombieConfig.SHIELD_BLOCK_CHANCE.get()
                + progressionDay * ZombieConfig.SHIELD_BLOCK_CHANCE_PER_DAY.get();

        // จำกัดค่าให้อยู่ระหว่าง 0 - 1
        chance = Math.min(1.0D, Math.max(0.0D, chance));

        if (ThreadLocalRandom.current().nextDouble() > chance) return;

        double reactionDelay = ZombieConfig.SHIELD_REACTION_DELAY.get()
                - progressionDay * ZombieConfig.SHIELD_REACTION_DELAY_PER_DAY.get();

        int delay = Math.max(0, (int) Math.round(reactionDelay));


        // ถ้ามี Delay ให้รอก่อน Block
        if (delay > 0) {
            REACTION_ZOMBIES.put(zombie.getUUID(), delay);
            return;
        }

        startBlocking(zombie, progressionDay);
    }


    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {

        if (!(event.getEntity() instanceof Zombie zombie)) return;
        if (!ZombieSkillManager.hasSkill(zombie, ZombieSkill.SHIELD)) return;

        UUID uuid = zombie.getUUID();

        if (REACTION_ZOMBIES.containsKey(uuid)) {

            int ticks = REACTION_ZOMBIES.get(uuid) - 1;

            if (ticks <= 0) {

                REACTION_ZOMBIES.remove(uuid);

                long day = zombie.level().getDayTime() / 24000L;
                long progressionDay = Math.max(0, day - ZombieConfig.START_DAY.get());

                startBlocking(zombie, progressionDay);

            } else {
                REACTION_ZOMBIES.put(uuid, ticks);
            }
        }

        if (BLOCKING_ZOMBIES.containsKey(uuid)) {

            int ticks = BLOCKING_ZOMBIES.get(uuid) - 1;

            if (ticks <= 0) {

                zombie.stopUsingItem();
                BLOCKING_ZOMBIES.remove(uuid);

            } else {
                BLOCKING_ZOMBIES.put(uuid, ticks);
            }
        }
    }

    private static void startBlocking(Zombie zombie, long progressionDay) {

        // ต้องยังมี Shield อยู่
        if (!zombie.getOffhandItem().is(Items.SHIELD)) return;


        int blockTime = ZombieConfig.SHIELD_BLOCK_TIME.get()
                + (int) (progressionDay * ZombieConfig.SHIELD_BLOCK_TIME_PER_DAY.get());

        zombie.startUsingItem(InteractionHand.OFF_HAND);

        BLOCKING_ZOMBIES.put(zombie.getUUID(), blockTime);
    }
}