package team.lodestar.lodestone.registry.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.events.types.worldevent.WorldEventTypeRegistryEvent;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

import java.util.HashMap;
import java.util.Optional;

public class LodestoneWorldEventTypeRegistry {

    public static HashMap<ResourceLocation, WorldEventType> EVENT_TYPES = new HashMap<>();

    public static WorldEventType registerEventType(WorldEventType eventType) {
        EVENT_TYPES.put(eventType.id, eventType);
        return eventType;
    }

    public static void postRegistryEvent(FMLCommonSetupEvent event) {
        DeferredWorkQueue queue = DeferredWorkQueue.lookup(Optional.of(ModLoadingStage.COMMON_SETUP)).orElseThrow();

        queue.enqueueWork(ModLoadingContext.get().getActiveContainer(), () -> {
            LodestoneLib.LOGGER.info("Registering world event types...");
            FMLJavaModLoadingContext.get().getModEventBus().post(new WorldEventTypeRegistryEvent());
        });
    }
}
