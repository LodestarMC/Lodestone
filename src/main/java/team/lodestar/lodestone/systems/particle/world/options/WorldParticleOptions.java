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

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;


public class WorldParticleOptions<T extends LodestoneParticleBehavior<T>> extends SimpleParticleOptions implements ParticleOptions {

    public final ParticleType<?> type;
    public final T behavior;
    public ParticleRenderType renderType = LodestoneWorldParticleRenderType.ADDITIVE;
    public RenderHandler.LodestoneRenderLayer renderLayer = RenderHandler.DELAYED_RENDER;
    public boolean shouldCull;
    public final Collection<Consumer<LodestoneWorldParticle<T>>> tickActors = new ArrayList<>();
    public final Collection<Consumer<LodestoneWorldParticle<T>>> spawnActors = new ArrayList<>();
    public final Collection<Consumer<LodestoneWorldParticle<T>>> renderActors = new ArrayList<>();

    public boolean noClip = false;

    public WorldParticleOptions(ParticleType<?> type, T behavior) {
        this.type = type;
        this.behavior = behavior;
    }

    public WorldParticleOptions(ParticleType<?> type) {
        this(type, null);
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