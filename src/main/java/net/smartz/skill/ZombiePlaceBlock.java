package net.smartz.skill;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.world.InteractionHand;

import net.smartz.attributes.ZombieConfig;

public class ZombiePlaceBlock {

    @SubscribeEvent
    public static void onZombieTick(LivingEvent.LivingTickEvent event){
        if(!(event.getEntity() instanceof Zombie zombie)){
            return;
        }

        if(zombie.level().isClientSide()){
            return;
        }
        long day = event.getLevel().getDayTime() / 24000L;
        if(!(day >= ZombieConfig.START_DAY_PLACE_BLOCK.get())) return;
        if (!ZombieConfig.BLOCK_PLACE_ENABLED.get()) {
            return;
        }

        if (!ZombieSkillManager.hasSkill(zombie, ZombieSkill.PLACE_BLOCK)) return;

        if(!(zombie.getTarget() instanceof Player player)){
            return;
        }

        double differenceY = player.getY() - zombie.getY();
        double differenceX = player.getX() - zombie.getX();
        double differenceZ = player.getZ() - zombie.getZ();

        double horizontalDistance = Math.sqrt(
                differenceX * differenceX +
                        differenceZ * differenceZ
        );

        if (differenceY > 1.0D){
            tryPlaceBlock_y(zombie);
        } else if (horizontalDistance > 1.5D) {
            tryPlaceBlock_x(zombie);
        }

    }

    public static void tryPlaceBlock_y(Zombie zombie){
        Level level = zombie.level();

        if (zombie.onGround()) {
            zombie.getJumpControl().jump();
            return;
        }

        BlockPos placePos = zombie.blockPosition().below();

        BlockState currntBlock = level.getBlockState(placePos);

        if(!(currntBlock.isAir())){
            return;
        };

        zombie.swing(InteractionHand.MAIN_HAND);

        level.setBlock(
                placePos,
                Blocks.COBBLESTONE.defaultBlockState(),
                3
        );

    }

    public static void tryPlaceBlock_x(Zombie zombie){

        if (!(zombie.getTarget() instanceof Player player)) return;

        Level level = zombie.level();

        BlockPos zombiePos = zombie.blockPosition();

        double differenceX = player.getX() - zombie.getX();
        double differenceZ = player.getZ() - zombie.getZ();

        BlockPos frontPos;

        if (Math.abs(differenceX) > Math.abs(differenceZ)) {

            if (differenceX > 0) {
                frontPos = zombiePos.east();
            } else {
                frontPos = zombiePos.west();
            }

        } else {

            if (differenceZ > 0) {
                frontPos = zombiePos.south();
            } else {
                frontPos = zombiePos.north();
            }
        }

        BlockPos placePos = frontPos.below();

        BlockState currentBlock = level.getBlockState(placePos);

        if (!currentBlock.isAir()) return;

        zombie.swing(InteractionHand.MAIN_HAND);

        level.setBlock(
                placePos,
                Blocks.COBBLESTONE.defaultBlockState(),
                3
        );

    }
}
