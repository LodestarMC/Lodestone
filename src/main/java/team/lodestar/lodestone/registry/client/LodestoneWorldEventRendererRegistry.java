package team.lodestar.lodestone.registry.client;

import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventRenderer;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

import java.util.HashMap;

/**
 * Register renderers in FMLClientSetupEvent.
 */
public class LodestoneWorldEventRendererRegistry {
    public static HashMap<WorldEventType, WorldEventRenderer<WorldEventInstance>> RENDERERS = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static void registerRenderer(WorldEventType type, WorldEventRenderer<? extends WorldEventInstance> renderer) {
        RENDERERS.put(type, (WorldEventRenderer<WorldEventInstance>) renderer);
    }
}
