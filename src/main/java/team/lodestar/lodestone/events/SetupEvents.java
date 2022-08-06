package team.lodestar.lodestone.events;

import team.lodestar.lodestone.systems.block.LodestoneThrowawayBlockData;
import team.lodestar.lodestone.capability.LodestoneEntityDataCapability;
import team.lodestar.lodestone.capability.LodestonePlayerDataCapability;
import team.lodestar.lodestone.capability.LodestoneWorldDataCapability;
import team.lodestar.lodestone.handlers.PlacementAssistantHandler;
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
        LodestoneWorldDataCapability.registerCapabilities(event);
        LodestoneEntityDataCapability.registerCapabilities(event);
        LodestonePlayerDataCapability.registerCapabilities(event);
    }

    @SubscribeEvent
    public static void registerCommon(FMLCommonSetupEvent event) {
        PlacementAssistantHandler.registerPlacementAssistants(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void lateSetup(InterModEnqueueEvent event) {
        LodestoneThrowawayBlockData.wipeCache(event);
    }
}