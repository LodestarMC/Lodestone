package team.lodestar.lodestone.systems.particle.world.options;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.*;
import org.lwjgl.opengl.GL11;
import team.lodestar.lodestone.handlers.*;
import team.lodestar.lodestone.registry.client.LodestoneShaderRegistry;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.render_types.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.components.*;
import team.lodestar.lodestone.systems.particle.world.type.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.*;

import static net.minecraft.client.particle.ParticleRenderType.PARTICLE_SHEET_OPAQUE;


public class WorldParticleOptions extends SimpleParticleOptions implements ParticleOptions {

    public final ParticleType<?> type;
    public LodestoneParticleBehavior behavior = LodestoneParticleBehavior.BILLBOARD;
    public LodestoneBehaviorComponent behaviorComponent;
    public ParticleRenderType renderType = FabricLoader.getInstance().isModLoaded("iris") ? LodestoneWorldParticleRenderType.IRIS_ADDITIVE
            : LodestoneWorldParticleRenderType.ADDITIVE; //TODO this is it
    public RenderHandler.LodestoneRenderLayer renderLayer = RenderHandler.DELAYED_RENDER;
    public boolean shouldCull;
    public final Collection<Consumer<LodestoneWorldParticle>> tickActors = new ArrayList<>();
    public final Collection<Consumer<LodestoneWorldParticle>> spawnActors = new ArrayList<>();
    public final Collection<Consumer<LodestoneWorldParticle>> renderActors = new ArrayList<>();



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