package net.smartz.smartzombiemod;

import net.minecraftforge.fml.config.ModConfig;
import net.smartz.attributes.ZombieProgression;
import net.smartz.attributes.ZombieConfig;
import net.smartz.skill.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(SmartZombieMod.MOD_ID)
public class SmartZombieMod
{
    public static final String MOD_ID = "smartzombiemod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public SmartZombieMod(FMLJavaModLoadingContext context) {

        context.registerConfig(ModConfig.Type.COMMON, ZombieConfig.SPEC);

        MinecraftForge.EVENT_BUS.register(ZombieProgression.class);

        MinecraftForge.EVENT_BUS.register(ZombieShield.class);

        MinecraftForge.EVENT_BUS.register(ZombieShieldAI.class);

        MinecraftForge.EVENT_BUS.register(ZombieFlintAndSteel.class);

        MinecraftForge.EVENT_BUS.register(ZombieBlockBreak.class);

        MinecraftForge.EVENT_BUS.register(SpecialZombie.class);

        MinecraftForge.EVENT_BUS.register(ZombieSkillManager.class);

        MinecraftForge.EVENT_BUS.register(ZombiePlaceBlock.class);

        MinecraftForge.EVENT_BUS.register(ZombieOpenDoor.class);

        MinecraftForge.EVENT_BUS.register(ZombieHear.class);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
