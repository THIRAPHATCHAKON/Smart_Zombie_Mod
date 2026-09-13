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

        if (event.getLevel().isClientSide()) return;

        if (!(event.getEntity() instanceof Zombie zombie)) return;

        if (!ZombieConfig.PROGRESSION_ENABLED.get()) return;

        long day = event.getLevel().getDayTime() / 24000L;

        long progressionDay = Math.max(0, day - ZombieConfig.START_DAY.get());

        double maxHealth = ZombieConfig.MAX_HEALTH.get();

        double followRange = ZombieConfig.FOLLOW_RANGE.get();

        double knockbackResistance = ZombieConfig.KNOCKBACK_RESISTANCE.get();

        double movementSpeed = ZombieConfig.MOVEMENT_SPEED.get();

        double attackDamage = ZombieConfig.ATTACK_DAMAGE.get();

        double attackKnockback = ZombieConfig.ATTACK_KNOCKBACK.get();

        double armor = ZombieConfig.ARMOR.get();

        double armorToughness = ZombieConfig.ARMOR_TOUGHNESS.get();

        double reinforcementChance = ZombieConfig.SPAWN_REINFORCEMENTS_CHANCE.get();

        maxHealth += progressionDay * ZombieConfig.HEALTH_PER_DAY.get();

        followRange += progressionDay * ZombieConfig.FOLLOW_RANGE_PER_DAY.get();

        knockbackResistance += progressionDay * ZombieConfig.KNOCKBACK_RESISTANCE_PER_DAY.get();

        movementSpeed += progressionDay * ZombieConfig.SPEED_PER_DAY.get();

        attackDamage += progressionDay * ZombieConfig.DAMAGE_PER_DAY.get();

        attackKnockback += progressionDay * ZombieConfig.ATTACK_KNOCKBACK_PER_DAY.get();

        armor += progressionDay * ZombieConfig.ARMOR_PER_DAY.get();

        armorToughness += progressionDay * ZombieConfig.ARMOR_TOUGHNESS_PER_DAY.get();

        reinforcementChance += progressionDay * ZombieConfig.SPAWN_REINFORCEMENTS_CHANCE_PER_DAY.get();

        if (maxHealth > ZombieConfig.MAX_HEALTH.get()) {
            setAttribute(zombie, Attributes.MAX_HEALTH, ZombieConfig.MAX_HEALTH.get());
        } else {
            setAttribute(zombie, Attributes.MAX_HEALTH, maxHealth);
        }

        if (followRange > ZombieConfig.MAX_FOLLOW_RANGE.get()) {
            setAttribute(zombie, Attributes.FOLLOW_RANGE, ZombieConfig.MAX_FOLLOW_RANGE.get());
        } else {
            setAttribute(zombie, Attributes.FOLLOW_RANGE, followRange);
        }

        if (knockbackResistance > ZombieConfig.MAX_KNOCKBACK_RESISTANCE.get()) {
            setAttribute(zombie, Attributes.KNOCKBACK_RESISTANCE, ZombieConfig.MAX_KNOCKBACK_RESISTANCE.get());
        } else {
            setAttribute(zombie, Attributes.KNOCKBACK_RESISTANCE, knockbackResistance);
        }

        if (movementSpeed > ZombieConfig.MAX_MOVEMENT_SPEED.get()) {
            setAttribute(zombie, Attributes.MOVEMENT_SPEED, ZombieConfig.MAX_MOVEMENT_SPEED.get());
        } else {
            setAttribute(zombie, Attributes.MOVEMENT_SPEED, movementSpeed);
        }

        if (attackDamage > ZombieConfig.MAX_ATTACK_DAMAGE.get()) {
            setAttribute(zombie, Attributes.ATTACK_DAMAGE, ZombieConfig.MAX_ATTACK_DAMAGE.get());
        } else {
            setAttribute(zombie, Attributes.ATTACK_DAMAGE, attackDamage);
        }

        if (attackKnockback > ZombieConfig.MAX_ATTACK_KNOCKBACK.get()) {
            setAttribute(zombie, Attributes.ATTACK_KNOCKBACK, ZombieConfig.MAX_ATTACK_KNOCKBACK.get());
        } else {
            setAttribute(zombie, Attributes.ATTACK_KNOCKBACK, attackKnockback);
        }

        if (armor > ZombieConfig.MAX_ARMOR.get()) {
            setAttribute(zombie, Attributes.ARMOR, ZombieConfig.MAX_ARMOR.get());
        } else {
            setAttribute(zombie, Attributes.ARMOR, armor);
        }

        if (armorToughness > ZombieConfig.MAX_ARMOR_TOUGHNESS.get()) {
            setAttribute(zombie, Attributes.ARMOR_TOUGHNESS, ZombieConfig.MAX_ARMOR_TOUGHNESS.get());
        } else {
            setAttribute(zombie, Attributes.ARMOR_TOUGHNESS, armorToughness);
        }

        if (reinforcementChance > ZombieConfig.MAX_SPAWN_REINFORCEMENTS_CHANCE.get()) {
            setAttribute(zombie, Attributes.SPAWN_REINFORCEMENTS_CHANCE, ZombieConfig.MAX_SPAWN_REINFORCEMENTS_CHANCE.get());
        } else {
            setAttribute(zombie, Attributes.SPAWN_REINFORCEMENTS_CHANCE, reinforcementChance);
        }

        zombie.setHealth(zombie.getMaxHealth());
    }

    private static void setAttribute(Zombie zombie, Attribute attribute, double value) {
        AttributeInstance instance = zombie.getAttribute(attribute);

        if (instance != null) instance.setBaseValue(value);

    }
}