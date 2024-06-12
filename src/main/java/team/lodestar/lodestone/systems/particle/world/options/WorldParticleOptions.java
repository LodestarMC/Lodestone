package team.lodestar.lodestone.systems.particle.world.options;

import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.*;
import net.minecraftforge.registries.*;
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
    public final LodestoneParticleBehavior behavior;
    public LodestoneParticleBehaviorComponent behaviorComponent;
    public ParticleRenderType renderType = LodestoneWorldParticleRenderType.ADDITIVE;
    public RenderHandler.LodestoneRenderLayer renderLayer = RenderHandler.DELAYED_RENDER;
    public boolean shouldCull;
    public final Collection<Consumer<LodestoneWorldParticle>> tickActors = new ArrayList<>();
    public final Collection<Consumer<LodestoneWorldParticle>> spawnActors = new ArrayList<>();
    public final Collection<Consumer<LodestoneWorldParticle>> renderActors = new ArrayList<>();

    public boolean noClip = false;

    public WorldParticleOptions(ParticleType<?> type, LodestoneParticleBehavior behavior) {
        this.type = type;
        this.behavior = behavior;
    }

    public WorldParticleOptions(ParticleType<?> type) {
        this(type, LodestoneParticleBehavior.BILLBOARD);
    }

    public WorldParticleOptions(RegistryObject<? extends LodestoneWorldParticleType> type, LodestoneParticleBehavior behavior) {
        this(type.get(), behavior);
    }

    public WorldParticleOptions(RegistryObject<? extends LodestoneWorldParticleType> type) {
        this(type.get());
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