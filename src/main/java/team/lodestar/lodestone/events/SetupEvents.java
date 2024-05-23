package team.lodestar.lodestone.events;

import team.lodestar.lodestone.handlers.PlacementAssistantHandler;
import team.lodestar.lodestone.handlers.ThrowawayBlockDataHandler;

public class SetupEvents {

    @SubscribeEvent
    public static void registerCommon(FMLCommonSetupEvent event) {
        PlacementAssistantHandler.registerPlacementAssistants(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void lateSetup(InterModEnqueueEvent event) {
        ThrowawayBlockDataHandler.wipeCache(event);
    }
}