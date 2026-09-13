package net.smartz.skill;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smartz.attributes.ZombieConfig;

public class ZombieShield {

    @SubscribeEvent
    public static void onZombieSpawn(EntityJoinLevelEvent event) {

        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof Zombie zombie)) return;
        if (!ZombieConfig.SHIELD_ENABLED.get()) return;

        if (!ZombieSkillManager.hasSkill(zombie, ZombieSkill.SHIELD)) return;

        ItemStack shield = new ItemStack(Items.SHIELD);

        zombie.setItemSlot(EquipmentSlot.OFFHAND, shield);
        zombie.setDropChance(EquipmentSlot.OFFHAND, 0.0F);
    }
}