package team.lodestar.lodestone.systems.particle.screen;

import team.lodestar.lodestone.systems.particle.*;
import team.lodestar.lodestone.systems.particle.render_types.*;

public class ScreenParticleOptions extends SimpleParticleOptions<LodestoneScreenParticle> {

    public final ScreenParticleType type;
    public LodestoneScreenParticleRenderType renderType = LodestoneScreenParticleRenderType.ADDITIVE;

    public boolean tracksStack;
    public double stackTrackXOffset;
    public double stackTrackYOffset;

    public ScreenParticleOptions(ScreenParticleType type) {
        this.type = type;
    }
}