package team.lodestar.lodestone.systems.particle.options;

import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import team.lodestar.lodestone.systems.particle.LodestoneWorldParticleActor;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;

import java.util.*;
import java.util.function.Consumer;


public abstract class AbstractWorldParticleOptions extends SimpleParticleOptions implements ParticleOptions {

    public final ParticleType<?> type;
    public ParticleRenderType renderType;
    public final Collection<Consumer<LodestoneWorldParticleActor>> actors = new ArrayList<>();
    public boolean noClip = false;

    public AbstractWorldParticleOptions(ParticleType<?> type) {
        this.type = type;
    }
}