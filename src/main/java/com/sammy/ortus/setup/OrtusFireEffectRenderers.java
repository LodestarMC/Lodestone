package com.sammy.ortus.setup;

import com.sammy.ortus.systems.fireeffect.FireEffectInstance;
import com.sammy.ortus.systems.fireeffect.FireEffectRenderer;
import com.sammy.ortus.systems.fireeffect.FireEffectType;

import java.util.HashMap;

/**
 * Register renderers in FMLClientSetupEvent.
 */
@SuppressWarnings("all")
public class OrtusFireEffectRenderers {
    public static HashMap<FireEffectType, FireEffectRenderer<FireEffectInstance>> RENDERERS = new HashMap<>();

    public static void registerRenderer(FireEffectType type, FireEffectRenderer renderer) {
        RENDERERS.put(type, renderer);
    }
}