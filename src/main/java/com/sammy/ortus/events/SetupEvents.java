package com.sammy.ortus.events;

import com.sammy.ortus.capability.EntityDataCapability;
import com.sammy.ortus.capability.PlayerDataCapability;
import com.sammy.ortus.capability.WorldDataCapability;
import com.sammy.ortus.systems.block.OrtusThrowawayBlockData;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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