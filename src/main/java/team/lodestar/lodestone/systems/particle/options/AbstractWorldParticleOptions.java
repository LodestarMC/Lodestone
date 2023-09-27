package team.lodestar.lodestone.systems.particle.options;

import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;
import team.lodestar.lodestone.systems.particle.*;

import java.util.*;
import java.util.function.*;


public abstract class AbstractWorldParticleOptions extends SimpleParticleOptions implements ParticleOptions {

    public final ParticleType<?> type;
    public ParticleRenderType renderType;
    public final Collection<Consumer<LodestoneWorldParticleActor>> actors = new ArrayList<>();
    public boolean noClip = false;

    public AbstractWorldParticleOptions(ParticleType<?> type) {
        this.type = type;
    }
}