package team.lodestar.lodestone.systems.particle.screen;

import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;

import java.util.function.Consumer;

public class ScreenParticleOptions extends SimpleParticleOptions {

    public final ScreenParticleType<?> type;
    public LodestoneScreenParticleRenderType renderType = LodestoneScreenParticleRenderType.ADDITIVE;
    public Consumer<GenericScreenParticle> actor;

    public boolean tracksStack;
    public double stackTrackXOffset;
    public double stackTrackYOffset;

    public ScreenParticleOptions(ScreenParticleType<?> type) {
        this.type = type;
    }
}