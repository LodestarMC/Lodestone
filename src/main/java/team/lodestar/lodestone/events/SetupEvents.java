package team.lodestar.lodestone.events;

import team.lodestar.lodestone.handlers.ThrowawayBlockDataHandler;
import team.lodestar.lodestone.registry.common.LodestoneCommandArgumentTypes;

public class SetupEvents {

    public static void registerCommon(FMLCommonSetupEvent event) {
        LodestoneCommandArgumentTypes.registerArgumentTypes();
    }

    public static void lateSetup(InterModEnqueueEvent event) {
        ThrowawayBlockDataHandler.wipeCache(event);
    }
}