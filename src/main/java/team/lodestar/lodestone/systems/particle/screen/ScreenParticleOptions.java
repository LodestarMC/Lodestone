package team.lodestar.lodestone.systems.particle.screen;

import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;

public class ScreenParticleOptions extends SimpleParticleOptions {

    public final ScreenParticleType<?> type;
    public LodestoneScreenParticleRenderType renderType = LodestoneScreenParticleRenderType.ADDITIVE;

    public ScreenParticleOptions(ScreenParticleType<?> type) {
        this.type = type;
    }
}