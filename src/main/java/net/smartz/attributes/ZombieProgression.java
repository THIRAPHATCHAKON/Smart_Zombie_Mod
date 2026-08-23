package net.smartz.attributes;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ZombieProgression {

    @SubscribeEvent
    public static void onZombieSpawn(EntityJoinLevelEvent event) {

        // Server เท่านั้น
        if (event.getLevel().isClientSide()) {
            return;
        }

        // Zombie เท่านั้น
        if (!(event.getEntity() instanceof Zombie zombie)) {
            return;
        }

        // ปิดระบบ
        if (!ZombieConfig.PROGRESSION_ENABLED.get()) {
            return;
        }

        long day = event.getLevel().getDayTime() / 24000L;

        long progressionDay = Math.max(
                0,
                day - ZombieConfig.START_DAY.get()
        );

        double maxHealth =
                ZombieConfig.MAX_HEALTH.get();

        double followRange =
                ZombieConfig.FOLLOW_RANGE.get();

        double knockbackResistance =
                ZombieConfig.KNOCKBACK_RESISTANCE.get();

        double movementSpeed =
                ZombieConfig.MOVEMENT_SPEED.get();

        double attackDamage =
                ZombieConfig.ATTACK_DAMAGE.get();

        double attackKnockback =
                ZombieConfig.ATTACK_KNOCKBACK.get();

        double armor =
                ZombieConfig.ARMOR.get();

        double armorToughness =
                ZombieConfig.ARMOR_TOUGHNESS.get();

        double reinforcementChance =
                ZombieConfig.SPAWN_REINFORCEMENTS_CHANCE.get();


        maxHealth +=
                progressionDay *
                        ZombieConfig.HEALTH_PER_DAY.get();

        followRange +=
                progressionDay *
                        ZombieConfig.FOLLOW_RANGE_PER_DAY.get();

        knockbackResistance +=
                progressionDay *
                        ZombieConfig.KNOCKBACK_RESISTANCE_PER_DAY.get();

        movementSpeed +=
                progressionDay *
                        ZombieConfig.SPEED_PER_DAY.get();

        attackDamage +=
                progressionDay *
                        ZombieConfig.DAMAGE_PER_DAY.get();

        attackKnockback +=
                progressionDay *
                        ZombieConfig.ATTACK_KNOCKBACK_PER_DAY.get();

        armor +=
                progressionDay *
                        ZombieConfig.ARMOR_PER_DAY.get();

        armorToughness +=
                progressionDay *
                        ZombieConfig.ARMOR_TOUGHNESS_PER_DAY.get();

        reinforcementChance +=
                progressionDay *
                        ZombieConfig.SPAWN_REINFORCEMENTS_CHANCE_PER_DAY.get();


        setAttribute(
                zombie,
                Attributes.MAX_HEALTH,
                maxHealth
        );

        setAttribute(
                zombie,
                Attributes.FOLLOW_RANGE,
                followRange
        );

        setAttribute(
                zombie,
                Attributes.KNOCKBACK_RESISTANCE,
                knockbackResistance
        );

        setAttribute(
                zombie,
                Attributes.MOVEMENT_SPEED,
                movementSpeed
        );

        setAttribute(
                zombie,
                Attributes.ATTACK_DAMAGE,
                attackDamage
        );

        setAttribute(
                zombie,
                Attributes.ATTACK_KNOCKBACK,
                attackKnockback
        );

        setAttribute(
                zombie,
                Attributes.ARMOR,
                armor
        );

        setAttribute(
                zombie,
                Attributes.ARMOR_TOUGHNESS,
                armorToughness
        );

        setAttribute(
                zombie,
                Attributes.SPAWN_REINFORCEMENTS_CHANCE,
                reinforcementChance
        );

        zombie.setHealth(
                zombie.getMaxHealth()
        );
    }


    private static void setAttribute(
            Zombie zombie,
            Attribute attribute,
            double value
    ) {

        AttributeInstance instance =
                zombie.getAttribute(attribute);

        if (instance != null) {
            instance.setBaseValue(value);
        }
    }
}