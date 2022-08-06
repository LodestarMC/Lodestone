package team.lodestar.lodestone.setup;

import team.lodestar.lodestone.systems.fireeffect.FireEffectInstance;
import team.lodestar.lodestone.systems.fireeffect.FireEffectRenderer;
import team.lodestar.lodestone.systems.fireeffect.FireEffectType;

import java.util.HashMap;

/**
 * Register renderers in FMLClientSetupEvent.
 */
@SuppressWarnings("all")
public class LodestoneFireEffectRendererRegistry {
    public static HashMap<FireEffectType, FireEffectRenderer<FireEffectInstance>> RENDERERS = new HashMap<>();

    public static void registerRenderer(FireEffectType type, FireEffectRenderer renderer) {
        RENDERERS.put(type, renderer);
    }
}