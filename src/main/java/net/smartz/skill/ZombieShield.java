package net.smartz.skill;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smartz.attributes.ZombieConfig;

import java.util.Random;

public class ZombieShield {

    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onZombieSpawn(EntityJoinLevelEvent event) {

        // Server เท่านั้น
        if (event.getLevel().isClientSide()) {
            return;
        }

        // ต้องเป็น Zombie
        if (!(event.getEntity() instanceof Zombie zombie)) {
            return;
        }

        // ระบบถูกปิด
        if (!ZombieConfig.SHIELD_ENABLED.get()) {
            return;
        }

        // สุ่มโอกาส
        if (RANDOM.nextDouble() >
                ZombieConfig.SHIELD_CHANCE.get()) {
            return;
        }

        ItemStack shield =
                new ItemStack(Items.SHIELD);

        zombie.setItemSlot(
                net.minecraft.world.entity.EquipmentSlot.OFFHAND,
                shield
        );

        zombie.setDropChance(
                net.minecraft.world.entity.EquipmentSlot.OFFHAND,
                0.0F
        );
    }
}