package com.sammy.ortus.events;

import com.sammy.ortus.capability.OrtusEntityDataCapability;
import com.sammy.ortus.capability.OrtusPlayerDataCapability;
import com.sammy.ortus.capability.OrtusWorldDataCapability;
import com.sammy.ortus.handlers.PlacementAssistantHandler;
import com.sammy.ortus.systems.block.OrtusThrowawayBlockData;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupEvents {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        OrtusWorldDataCapability.registerCapabilities(event);
        OrtusEntityDataCapability.registerCapabilities(event);
        OrtusPlayerDataCapability.registerCapabilities(event);
    }

    @SubscribeEvent
    public static void registerCommon(FMLCommonSetupEvent event) {
        PlacementAssistantHandler.registerPlacementAssistants(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void lateSetup(InterModEnqueueEvent event) {
        OrtusThrowawayBlockData.wipeCache(event);
    }
}