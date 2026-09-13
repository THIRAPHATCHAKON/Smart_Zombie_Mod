package net.smartz.skill;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class ZombieOpenDoor {

    @SubscribeEvent
    public static void onZombieSpawn(EntityJoinLevelEvent event){

        if (event.getLevel().isClientSide()) return;

        if (!(event.getEntity() instanceof Zombie zombie)) return;

        if(zombie.getNavigation() instanceof GroundPathNavigation navigation){
            navigation.setCanOpenDoors(true);
            navigation.setCanPassDoors(true);
        }

    }

    @SubscribeEvent
    public static void onZombieTick(LivingEvent.LivingTickEvent event){
        if (!(event.getEntity() instanceof Zombie zombie)) return;

        if (zombie.level().isClientSide()) return;

        openDoors(zombie);
    }

    public static void openDoors(Zombie zombie){
        Level level = zombie.level();
        BlockPos zombiePos = zombie.blockPosition();

        BlockPos[] position = {
                zombiePos,
                zombiePos.above(),
                zombiePos.north(),
                zombiePos.south(),
                zombiePos.east(),
                zombiePos.west()
        };

        for (BlockPos pos : position){
            BlockState state = level.getBlockState(pos);

            if (!(state.getBlock() instanceof DoorBlock door)) continue;
            if (state.getValue(DoorBlock.OPEN)) continue;

            door.setOpen(zombie, level, state, pos, true);

            return;
        }
    }
}
