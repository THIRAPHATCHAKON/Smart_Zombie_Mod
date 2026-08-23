package net.smartz.attributes;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class ZombieAttributes {

    @SubscribeEvent
    public static void attrubuteszombie(EntityAttributeModificationEvent event) {

        event.add(
                EntityType.ZOMBIE,
                Attributes.MAX_HEALTH,
                ZombieConfig.MAX_HEALTH.get()
        );

        event.add(
                EntityType.ZOMBIE,
                Attributes.FOLLOW_RANGE,
                ZombieConfig.FOLLOW_RANGE.get()
        );

        event.add(
                EntityType.ZOMBIE,
                Attributes.KNOCKBACK_RESISTANCE,
                ZombieConfig.KNOCKBACK_RESISTANCE.get()
        );

        event.add(
                EntityType.ZOMBIE,
                Attributes.MOVEMENT_SPEED,
                ZombieConfig.MOVEMENT_SPEED.get()
        );

        event.add(
                EntityType.ZOMBIE,
                Attributes.ATTACK_DAMAGE,
                ZombieConfig.ATTACK_DAMAGE.get()
        );

        event.add(
                EntityType.ZOMBIE,
                Attributes.ATTACK_KNOCKBACK,
                ZombieConfig.ATTACK_KNOCKBACK.get()
        );

        event.add(
                EntityType.ZOMBIE,
                Attributes.ARMOR,
                ZombieConfig.ARMOR.get()
        );

        event.add(
                EntityType.ZOMBIE,
                Attributes.ARMOR_TOUGHNESS,
                ZombieConfig.ARMOR_TOUGHNESS.get()
        );


        event.add(
                EntityType.ZOMBIE,
                Attributes.SPAWN_REINFORCEMENTS_CHANCE,
                ZombieConfig.SPAWN_REINFORCEMENTS_CHANCE.get()
        );
    }
}