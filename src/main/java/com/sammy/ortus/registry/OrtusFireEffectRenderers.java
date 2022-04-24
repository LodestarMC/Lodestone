package com.sammy.ortus.registry;

import com.sammy.ortus.systems.fireeffect.FireEffectInstance;
import com.sammy.ortus.systems.fireeffect.FireEffectRenderer;
import com.sammy.ortus.systems.fireeffect.FireEffectType;
import com.sammy.ortus.systems.worldevent.WorldEventInstance;
import com.sammy.ortus.systems.worldevent.WorldEventRenderer;

import java.util.HashMap;

/**
 * Register renderers in FMLClientSetupEvent.
 */
@SuppressWarnings("all")
public class OrtusFireEffectRenderers {
    public static HashMap<FireEffectType, FireEffectRenderer<FireEffectInstance>> RENDERERS = new HashMap<>();

    private static void registerRenderer(FireEffectType type, FireEffectRenderer renderer) {
        RENDERERS.put(type, renderer);
    }
}