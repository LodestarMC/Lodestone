package com.sammy.ortus.setup.worldevent;

import com.sammy.ortus.systems.worldevent.WorldEventInstance;
import com.sammy.ortus.systems.worldevent.WorldEventRenderer;
import com.sammy.ortus.systems.worldevent.WorldEventType;

import java.util.HashMap;

/**
 * Register renderers in FMLClientSetupEvent.
 */
@SuppressWarnings("all")
public class OrtusWorldEventRenderers {
    public static HashMap<WorldEventType, WorldEventRenderer<WorldEventInstance>> RENDERERS = new HashMap<>();

    public static void registerRenderer(WorldEventType type, WorldEventRenderer renderer) {
        RENDERERS.put(type, renderer);
    }
}