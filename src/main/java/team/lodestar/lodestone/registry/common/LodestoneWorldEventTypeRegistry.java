package team.lodestar.lodestone.registry.common;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

import java.util.HashMap;
import java.util.Optional;

public class LodestoneWorldEventTypeRegistry {

    public static HashMap<ResourceLocation, WorldEventType> EVENT_TYPES = new HashMap<>();

    public static WorldEventType registerEventType(WorldEventType eventType) {
        EVENT_TYPES.put(eventType.id, eventType);
        return eventType;
    }

    /*TODO

    public static void postRegistryEvent() {
        DeferredWorkQueue queue = DeferredWorkQueue.lookup(Optional.of(ModLoadingStage.COMMON_SETUP)).orElseThrow();

        queue.enqueueWork(ModLoadingContext.get().getActiveContainer(), () -> {
            LodestoneLib.LOGGER.info("Registering world event types...");
            FMLJavaModLoadingContext.get().getModEventBus().post(new WorldEventTypeRegistryEvent());
        });
    }

     */
}


