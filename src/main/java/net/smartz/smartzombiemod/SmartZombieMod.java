package net.smartz.smartzombiemod;

import net.minecraftforge.fml.config.ModConfig;
import net.smartz.attributes.ZombieProgression;
import net.smartz.attributes.ZombieConfig;
import net.smartz.skill.ZombieSkillManager;
import net.smartz.skill.SpecialZombie;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.smartz.skill.ZombieBlockBreak;
import net.smartz.skill.ZombieShield;
import net.smartz.skill.ZombieShieldAI;
import net.smartz.skill.ZombieFlintAndSteel;
import org.slf4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(SmartZombieMod.MOD_ID)
public class SmartZombieMod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "smartzombiemod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public SmartZombieMod(FMLJavaModLoadingContext context) {

        context.registerConfig(
                ModConfig.Type.COMMON,
                ZombieConfig.SPEC
        );

        MinecraftForge.EVENT_BUS.register(
                ZombieProgression.class
        );

        MinecraftForge.EVENT_BUS.register(
                ZombieShield.class
        );

        MinecraftForge.EVENT_BUS.register(
                ZombieShieldAI.class
        );

        MinecraftForge.EVENT_BUS.register(
                ZombieFlintAndSteel.class
        );

        MinecraftForge.EVENT_BUS.register(
                ZombieBlockBreak.class
        );

        MinecraftForge.EVENT_BUS.register(
                SpecialZombie.class
        );

        MinecraftForge.EVENT_BUS.register(
                ZombieSkillManager.class
        );

        MinecraftForge.EVENT_BUS.register(this);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
