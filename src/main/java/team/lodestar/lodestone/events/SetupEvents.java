package team.lodestar.lodestone.events;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import team.lodestar.lodestone.capability.LodestoneEntityDataCapability;
import team.lodestar.lodestone.capability.LodestonePlayerDataCapability;
import team.lodestar.lodestone.capability.LodestoneWorldDataCapability;
import team.lodestar.lodestone.handlers.PlacementAssistantHandler;
import team.lodestar.lodestone.handlers.ThrowawayBlockDataHandler;
import team.lodestar.lodestone.registry.common.LodestoneArgumentTypeRegistry;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
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
        LodestoneArgumentTypeRegistry.registerArgumentTypes();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void lateSetup(InterModEnqueueEvent event) {
        ThrowawayBlockDataHandler.wipeCache(event);
    }
}