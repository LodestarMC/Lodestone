package com.sammy.ortus.registry.worldevent;

import com.sammy.ortus.systems.fireeffect.FireEffectType;
import com.sammy.ortus.systems.worldevent.WorldEventInstance;
import com.sammy.ortus.systems.worldevent.WorldEventRenderer;
import com.sammy.ortus.systems.worldevent.WorldEventType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashMap;

/**
 * Register renderers in FMLClientSetupEvent.
 */
@SuppressWarnings("all")
public class OrtusWorldEventRenderers {
    public static HashMap<WorldEventType, WorldEventRenderer<WorldEventInstance>> RENDERERS = new HashMap<>();

    private static void registerRenderer(WorldEventType type, WorldEventRenderer renderer) {
        RENDERERS.put(type, renderer);
    }
}