package net.smartz.skill;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smartz.attributes.ZombieConfig;

import java.util.concurrent.ThreadLocalRandom;

public class SpecialZombie {

    private static final String SPECIAL_TAG =
            "SmartZombieSpecial";

    private static final String DODGE_COOLDOWN =
            "SmartZombieDodgeCooldown";

    private static final String DODGE_TIME =
            "SmartZombieDodgeTime";

    private static final String DODGE_DIRECTION =
            "SmartZombieDodgeDirection";


    @SubscribeEvent
    public static void onZombieSpawn(
            EntityJoinLevelEvent event) {

        if (event.getLevel().isClientSide()) {
            return;
        }

        if (!(event.getEntity() instanceof Zombie zombie)) {
            return;
        }

        if (!ZombieConfig.SPECIAL_ZOMBIE_ENABLED.get()) {
            return;
        }

        // ถ้าเป็นตัวพิเศษอยู่แล้ว
        if (!ZombieSkillManager.hasSkill(
                zombie,
                ZombieSkill.DODGE)) {
            return;
        }

        // สุ่มเกิดเป็น Special Zombie
        if (ThreadLocalRandom.current().nextDouble()
                > ZombieConfig.SPECIAL_ZOMBIE_CHANCE.get()) {
            return;
        }

        makeSpecial(zombie);
    }

    private static void makeSpecial(
            Zombie zombie) {

        // Mark
        zombie.addTag(SPECIAL_TAG);


        AttributeInstance speed =
                zombie.getAttribute(
                        Attributes.MOVEMENT_SPEED
                );

        if (speed != null) {

            speed.setBaseValue(
                    ZombieConfig.SPECIAL_ZOMBIE_SPEED.get()
            );
        }

        zombie.getPersistentData().putInt(
                DODGE_COOLDOWN,
                0
        );

        zombie.getPersistentData().putInt(
                DODGE_TIME,
                0
        );

    }


    @SubscribeEvent
    public static void onLivingTick(
            LivingEvent.LivingTickEvent event) {

        if (!(event.getEntity() instanceof Zombie zombie)) {
            return;
        }

        if (zombie.level().isClientSide()) {
            return;
        }

        if (!isSpecial(zombie)) {
            return;
        }

        // Particle
        spawnParticle(zombie);

        // Dodge
        if (ZombieConfig.SPECIAL_ZOMBIE_DODGE_ENABLED.get()) {

            handleDodge(zombie);
        }
    }

    private static void handleDodge(
            Zombie zombie) {

        if (!(zombie.getTarget() instanceof Player player)) {
            return;
        }

        double distance =
                zombie.distanceTo(player);

        // ต้องอยู่ในระยะที่สามารถหลบได้
        if (distance > 16.0D) {
            return;
        }

        int dodgeTime =
                zombie.getPersistentData()
                        .getInt(DODGE_TIME);

        if (dodgeTime > 0) {

            dodgeTime--;

            zombie.getPersistentData().putInt(
                    DODGE_TIME,
                    dodgeTime
            );

            // ถ้าถึงจุดหลบแล้ว
            if (zombie.getNavigation().isDone()) {

                zombie.getPersistentData().putInt(
                        DODGE_TIME,
                        0
                );
            }

            return;
        }

        int cooldown =
                zombie.getPersistentData()
                        .getInt(DODGE_COOLDOWN);

        if (cooldown > 0) {

            zombie.getPersistentData().putInt(
                    DODGE_COOLDOWN,
                    cooldown - 1
            );

            // ระหว่าง cooldown ยังวิ่งเข้าหา Player
            zombie.getNavigation().moveTo(
                    player,
                    1.0D
            );

            return;
        }

        if (!isPlayerLookingAtZombie(
                player,
                zombie)) {

            zombie.getNavigation().moveTo(
                    player,
                    1.0D
            );

            return;
        }

        if (ThreadLocalRandom.current().nextDouble()
                > ZombieConfig.SPECIAL_ZOMBIE_DODGE_CHANCE.get()) {
            return;
        }

        int direction =
                zombie.getPersistentData()
                        .getInt(DODGE_DIRECTION);

        if (direction == 0) {
            direction = 1;
        } else {
            direction = -direction;
        }

        zombie.getPersistentData().putInt(
                DODGE_DIRECTION,
                direction
        );


        Vec3 dodgePosition =
                calculateDodgePosition(
                        zombie,
                        player,
                        direction
                );


        zombie.getNavigation().moveTo(
                dodgePosition.x,
                dodgePosition.y,
                dodgePosition.z,
                ZombieConfig.SPECIAL_ZOMBIE_DODGE_SPEED.get()
        );

        zombie.getPersistentData().putInt(
                DODGE_TIME,
                ZombieConfig.SPECIAL_ZOMBIE_DODGE_DURATION.get()
        );

        zombie.getPersistentData().putInt(
                DODGE_COOLDOWN,
                ZombieConfig.SPECIAL_ZOMBIE_DODGE_COOLDOWN.get()
        );
    }

    private static boolean isPlayerLookingAtZombie(
            Player player,
            Zombie zombie) {

        Vec3 eyes =
                player.getEyePosition();

        Vec3 target =
                zombie.position()
                        .add(
                                0,
                                zombie.getBbHeight() * 0.5,
                                0
                        );

        Vec3 direction =
                target
                        .subtract(eyes)
                        .normalize();

        Vec3 look =
                player.getLookAngle()
                        .normalize();

        double dot =
                look.dot(direction);

        return dot > 0.85D;
    }

    private static Vec3 calculateDodgePosition(
            Zombie zombie,
            Player player,
            int direction) {

        // Vector จาก Player → Zombie
        Vec3 away =
                zombie.position()
                        .subtract(player.position());

        away = new Vec3(
                away.x,
                0,
                away.z
        );

        if (away.lengthSqr() < 0.001D) {
            away = new Vec3(
                    0,
                    0,
                    1
            );
        }

        away = away.normalize();

        Vec3 side =
                new Vec3(
                        -away.z,
                        0,
                        away.x
                ).normalize();

        side =
                side.scale(direction);

        double dodgeDistance =
                ZombieConfig.SPECIAL_ZOMBIE_DODGE_DISTANCE.get();


        double distance =
                zombie.distanceTo(player);

        double intensity =
                1.0D -
                        Math.min(
                                distance / 12.0D,
                                1.0D
                        );

        // ทำให้ช่วงใกล้แรงขึ้น
        intensity =
                intensity * intensity;

        dodgeDistance +=
                dodgeDistance * intensity;

        // จำกัดไม่ให้ไกลเกินไป
        dodgeDistance =
                Math.min(
                        dodgeDistance,
                        6.0D
                );


        Vec3 target =
                zombie.position()
                        .add(
                                side.x * dodgeDistance,
                                0,
                                side.z * dodgeDistance
                        );

        return target;
    }

    private static void spawnParticle(
            Zombie zombie) {

        if (!ZombieConfig.SPECIAL_ZOMBIE_PARTICLE.get()) {
            return;
        }

        if (!(zombie.level()
                instanceof ServerLevel level)) {
            return;
        }

        // ใช้ Particle แบบ Dust
        // ค่า RGB = 0,0,0
        net.minecraft.core.particles.DustParticleOptions black =
                new net.minecraft.core.particles.DustParticleOptions(
                        new org.joml.Vector3f(
                                0.0F,
                                0.0F,
                                0.0F
                        ),
                        1.5F
                );

        level.sendParticles(
                black,
                zombie.getX(),
                zombie.getY() + 1.0D,
                zombie.getZ(),
                2,
                0.3D,
                0.5D,
                0.3D,
                0.01D
        );
    }


    public static boolean isSpecial(
            Zombie zombie) {

        return zombie.getTags()
                .contains(SPECIAL_TAG);
    }
}