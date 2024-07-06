package team.lodestar.lodestone.systems.particle.world.options;

import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.*;
import team.lodestar.lodestone.handlers.*;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.render_types.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.components.*;
import team.lodestar.lodestone.systems.particle.world.type.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.*;


public class WorldParticleOptions extends SimpleParticleOptions implements ParticleOptions {

    public final ParticleType<?> type;
    public LodestoneParticleBehavior behavior = LodestoneParticleBehavior.BILLBOARD;
    public LodestoneBehaviorComponent behaviorComponent;
    public ParticleRenderType renderType = LodestoneWorldParticleRenderType.ADDITIVE;
    public RenderHandler.LodestoneRenderLayer renderLayer = RenderHandler.DELAYED_RENDER;
    public boolean shouldCull;
    public final Collection<Consumer<LodestoneWorldParticle>> tickActors = new ArrayList<>();
    public final Collection<Consumer<LodestoneWorldParticle>> spawnActors = new ArrayList<>();
    public final Collection<Consumer<LodestoneWorldParticle>> renderActors = new ArrayList<>();

    public boolean noClip = false;

    public WorldParticleOptions(ParticleType<?> type) {
        this.type = type;
    }

    public WorldParticleOptions(Supplier<? extends LodestoneWorldParticleType> type) {
        this(type.get());
    }

    public WorldParticleOptions setBehavior(LodestoneBehaviorComponent behaviorComponent) {
        if (behaviorComponent == null) {
            return this;
        }
        this.behavior = behaviorComponent.getBehaviorType();
        this.behaviorComponent = behaviorComponent;
        return this;
    }

    public WorldParticleOptions setBehaviorIfDefault(LodestoneBehaviorComponent behaviorComponent) {
        if (!behavior.equals(LodestoneParticleBehavior.BILLBOARD)) {
            return this;
        }
        return setBehavior(behaviorComponent);
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}