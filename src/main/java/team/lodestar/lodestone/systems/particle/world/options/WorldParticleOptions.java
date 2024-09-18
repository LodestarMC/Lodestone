package team.lodestar.lodestone.systems.particle.world.options;

import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.helpers.RenderHelper;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
import team.lodestar.lodestone.systems.particle.world.behaviors.LodestoneParticleBehavior;
import team.lodestar.lodestone.systems.particle.world.behaviors.components.LodestoneBehaviorComponent;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;


public class WorldParticleOptions extends SimpleParticleOptions implements ParticleOptions {

    public final ParticleType<?> type;
    public LodestoneParticleBehavior behavior = LodestoneParticleBehavior.BILLBOARD;
    public LodestoneBehaviorComponent behaviorComponent;
    public ParticleRenderType renderType = LodestoneWorldParticleRenderType.ADDITIVE; //TODO this is it
    public RenderHandler.LodestoneRenderLayer renderLayer = RenderHandler.DELAYED_RENDER;
    public boolean shouldCull;
    public final Collection<Consumer<LodestoneWorldParticle>> tickActors = new ArrayList<>();
    public final Collection<Consumer<LodestoneWorldParticle>> spawnActors = new ArrayList<>();
    public final Collection<Consumer<LodestoneWorldParticle>> renderActors = new ArrayList<>();
    public int particleLight = RenderHelper.FULL_BRIGHT;

    public boolean noClip = false;

    public WorldParticleOptions(ParticleType<?> type) {
        this.type = type;
    }

    public WorldParticleOptions(RegistryObject<? extends LodestoneWorldParticleType> type) {
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

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
    }

    @Override
    public String writeToString() {
        return "";
    }
}