package net.smartz.skill;

import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class ZombieHear {

    @SubscribeEvent
    public static void onZombieTick(LivingEvent.LivingTickEvent event){

        if (!(event.getEntity() instanceof Player player)) return;

        if (player.level().isClientSide()) return;

        if (player.tickCount % 10 != 0) return;


        targetHear(player);
    }

    public static void targetHear(Player player){

        double movementX = player.getDeltaMovement().x;
        double movementZ = player.getDeltaMovement().z;

        double speed = Math.sqrt(Math.pow(movementX, 2) + Math.pow(movementZ, 2));

        if(speed < 0.01D) return;

        double hearingRange;

        if (player.isCrouching()) {
            hearingRange = 2.0D;

        } else if (player.isSprinting()) {
            hearingRange = 12.0D;

        } else {
            hearingRange = 6.0D;
        }

        List<Zombie> zombies = player.level().getEntitiesOfClass(
                Zombie.class,
                player.getBoundingBox().inflate(hearingRange)
        );

        for (Zombie zombie : zombies) {

            double distance = zombie.distanceTo(player);

            if (distance > hearingRange) continue;

            zombie.setTarget(player);
        }
    }
}
