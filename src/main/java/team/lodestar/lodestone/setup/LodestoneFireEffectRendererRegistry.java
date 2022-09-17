package team.lodestar.lodestone.setup;

import team.lodestar.lodestone.systems.fireeffect.FireEffectInstance;
import team.lodestar.lodestone.systems.fireeffect.FireEffectRenderer;
import team.lodestar.lodestone.systems.fireeffect.FireEffectType;

import java.util.HashMap;

/**
 * Register renderers in FMLClientSetupEvent.
 */
public class LodestoneFireEffectRendererRegistry {
    public static HashMap<FireEffectType, FireEffectRenderer<FireEffectInstance>> RENDERERS = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static void registerRenderer(FireEffectType type, FireEffectRenderer<? extends FireEffectInstance> renderer) {
        RENDERERS.put(type, (FireEffectRenderer<FireEffectInstance>) renderer);
    }
}
