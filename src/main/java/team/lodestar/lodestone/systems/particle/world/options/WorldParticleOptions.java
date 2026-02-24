package team.lodestar.lodestone.systems.particle.world.options;

import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;
import team.lodestar.lodestone.handlers.rendering.*;
import team.lodestar.lodestone.helpers.*;
import team.lodestar.lodestone.systems.particle.*;
import team.lodestar.lodestone.systems.particle.render_types.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;
import team.lodestar.lodestone.systems.particle.world.type.*;
import team.lodestar.lodestone.systems.rendering.buffer.*;

import java.util.function.*;


public class WorldParticleOptions extends SimpleParticleOptions<LodestoneWorldParticle> implements ParticleOptions {

    public final ParticleType<?> type;

    public LodestoneParticleBehavior behavior = BillboardParticleBehavior.INSTANCE;
    public ParticleRenderType renderType = LodestoneWorldParticleRenderType.ADDITIVE;
    public LodestoneRenderLayer renderLayer = LodestoneRenderHandler.DEFERRED_RENDER;

    public int particleLight = RenderHelper.FULL_BRIGHT;

    public boolean noClip = false;

    public WorldParticleOptions(ParticleType<?> type) {
        this.type = type;
    }

    public WorldParticleOptions(Supplier<? extends LodestoneWorldParticleType> type) {
        this(type.get());
    }

    public WorldParticleOptions setBehavior(LodestoneParticleBehavior behavior) {
        this.behavior = behavior;
        return this;
    }

    public WorldParticleOptions setBehaviorIfDefault(LodestoneParticleBehavior newBehavior) {
        if (behavior.equals(BillboardParticleBehavior.INSTANCE)) {
            return setBehavior(newBehavior);
        }
        return this;
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}