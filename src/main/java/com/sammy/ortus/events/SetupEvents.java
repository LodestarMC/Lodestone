package com.sammy.ortus.events;

import com.sammy.ortus.capability.EntityDataCapability;
import com.sammy.ortus.capability.PlayerDataCapability;
import com.sammy.ortus.capability.WorldDataCapability;
import com.sammy.ortus.handlers.RenderHandler;
import com.sammy.ortus.handlers.ScreenParticleHandler;
import com.sammy.ortus.registry.OrtusParticles;
import com.sammy.ortus.registry.OrtusScreenParticles;
import com.sammy.ortus.systems.block.OrtusBlockProperties;
import com.sammy.ortus.systems.block.OrtusThrowawayBlockData;
import com.sammy.ortus.systems.rendering.particle.type.OrtusScreenParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupEvents {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        WorldDataCapability.registerCapabilities(event);
        EntityDataCapability.registerCapabilities(event);
        PlayerDataCapability.registerCapabilities(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void lateSetup(FMLCommonSetupEvent event) {
        OrtusThrowawayBlockData.wipeCache(event);
    }
}