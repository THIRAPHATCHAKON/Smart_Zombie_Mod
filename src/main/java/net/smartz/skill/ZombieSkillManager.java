package net.smartz.skill;

import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smartz.attributes.ZombieConfig;

import java.util.concurrent.ThreadLocalRandom;

public class ZombieSkillManager {

    private static final String SKILL_TAG = "SmartZombieSkill";

    @SubscribeEvent
    public static void onZombieSpawn(EntityJoinLevelEvent event) {

        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof Zombie zombie)) return;

        // ถ้ามี Skill แล้ว ไม่สุ่มใหม่
        if (hasSkill(zombie)) return;

        assignRandomSkill(zombie);
    }

    private static void assignRandomSkill(Zombie zombie) {

        int normal = ZombieConfig.ZOMBIE_NORMAL_CHANCE.get();
        int dodge = ZombieConfig.ZOMBIE_DODGE_CHANCE.get();
        int shield = ZombieConfig.ZOMBIE_SHIELD_CHANCE.get();
        int flint = ZombieConfig.ZOMBIE_FLINT_CHANCE.get();
        int breakBlock = ZombieConfig.ZOMBIE_BREAK_BLOCK_CHANCE.get();
        int placeBlock = ZombieConfig.ZOMBIE_PLACE_BLOCK_CHANCE.get();

        int total = normal + dodge + shield + flint + breakBlock + placeBlock;



        // ถ้า Chance ทั้งหมดเป็น 0
        if (total <= 0) {
            zombie.getPersistentData().putString(SKILL_TAG, ZombieSkill.NONE.name());
            return;
        }


        int roll = ThreadLocalRandom.current().nextInt(total);

        ZombieSkill skill;


        if (roll < normal) {

            skill = ZombieSkill.NONE;

        } else if (roll < normal + dodge) {

            skill = ZombieSkill.DODGE;

        } else if (roll < normal + dodge + shield) {

            skill = ZombieSkill.SHIELD;

        } else if (roll < normal + dodge + shield + flint) {

            skill = ZombieSkill.FLINT_AND_STEEL;

        } else if (roll < normal + dodge + shield + flint + breakBlock) {

            skill = ZombieSkill.BREAK_BLOCK;

        } else {

            skill = ZombieSkill.PLACE_BLOCK;
        }


        zombie.getPersistentData().putString(SKILL_TAG, skill.name());
    }


    public static boolean hasSkill(Zombie zombie) {

        return zombie.getPersistentData().contains(SKILL_TAG);
    }

    public static ZombieSkill getSkill(Zombie zombie) {

        String value = zombie.getPersistentData().getString(SKILL_TAG);

        if (value.isEmpty()) return ZombieSkill.NONE;

        try {
            return ZombieSkill.valueOf(value);

        } catch (IllegalArgumentException e) {
            return ZombieSkill.NONE;
        }
    }


    public static boolean hasSkill(Zombie zombie, ZombieSkill skill) {

        return getSkill(zombie) == skill;
    }
}