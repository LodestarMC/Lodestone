package team.lodestar.lodestone.setup.worldevent;

import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventRenderer;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

import java.util.HashMap;

/**
 * Register renderers in FMLClientSetupEvent.
 */
@SuppressWarnings("all")
public class LodestoneWorldEventRendererRegistry {
    public static HashMap<WorldEventType, WorldEventRenderer<WorldEventInstance>> RENDERERS = new HashMap<>();

    public static void registerRenderer(WorldEventType type, WorldEventRenderer renderer) {
        RENDERERS.put(type, renderer);
    }
}