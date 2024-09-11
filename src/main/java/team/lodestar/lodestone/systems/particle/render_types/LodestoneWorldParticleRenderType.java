package team.lodestar.lodestone.systems.particle.render_types;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.*;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.registry.client.*;
import team.lodestar.lodestone.systems.rendering.*;
import team.lodestar.lodestone.systems.rendering.rendeertype.*;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

import java.util.function.*;

public class LodestoneWorldParticleRenderType implements ParticleRenderType {

    public static final Function<LodestoneWorldParticleRenderType, LodestoneWorldParticleRenderType> DEPTH_FADE = Util.memoize(LodestoneWorldParticleRenderType::addDepthFade);

    public static final LodestoneWorldParticleRenderType ADDITIVE = new LodestoneWorldParticleRenderType(
            LodestoneRenderTypes.ADDITIVE_PARTICLE, LodestoneShaders.PARTICLE, TextureAtlas.LOCATION_PARTICLES,
            LodestoneRenderTypes.ADDITIVE_FUNCTION);

    public static final LodestoneWorldParticleRenderType TRANSPARENT = new LodestoneWorldParticleRenderType(
            LodestoneRenderTypes.TRANSPARENT_PARTICLE, LodestoneShaders.PARTICLE, TextureAtlas.LOCATION_PARTICLES,
            LodestoneRenderTypes.TRANSPARENT_FUNCTION);

    public static final LodestoneWorldParticleRenderType LUMITRANSPARENT = new LodestoneWorldParticleRenderType(
            LodestoneRenderTypes.LUMITRANSPARENT_PARTICLE, LodestoneShaders.PARTICLE, TextureAtlas.LOCATION_PARTICLES,
            LodestoneRenderTypes.TRANSPARENT_FUNCTION);

    public static final LodestoneWorldParticleRenderType TERRAIN_SHEET = new LodestoneWorldParticleRenderType(
            LodestoneRenderTypes.TRANSPARENT_BLOCK_PARTICLE, LodestoneShaders.PARTICLE, TextureAtlas.LOCATION_BLOCKS,
            LodestoneRenderTypes.TRANSPARENT_FUNCTION);

    public static final LodestoneWorldParticleRenderType ADDITIVE_TERRAIN_SHEET = new LodestoneWorldParticleRenderType(
            LodestoneRenderTypes.ADDITIVE_BLOCK_PARTICLE, LodestoneShaders.PARTICLE, TextureAtlas.LOCATION_BLOCKS,
            LodestoneRenderTypes.ADDITIVE_FUNCTION);

    public final LodestoneRenderType renderType;
    protected final Supplier<ShaderInstance> shader;
    protected final ResourceLocation texture;
    protected final Runnable blendFunction;

    public LodestoneWorldParticleRenderType(LodestoneRenderType renderType, ShaderHolder shaderHolder, ResourceLocation texture, GlStateManager.SourceFactor srcAlpha, GlStateManager.DestFactor dstAlpha) {
        this(renderType, shaderHolder.getInstance(), texture, srcAlpha, dstAlpha);
    }

    public LodestoneWorldParticleRenderType(LodestoneRenderType renderType, Supplier<ShaderInstance> shader, ResourceLocation texture, GlStateManager.SourceFactor srcAlpha, GlStateManager.DestFactor dstAlpha) {
        this(renderType, shader, texture, () -> RenderSystem.blendFunc(srcAlpha, dstAlpha));
    }

    public LodestoneWorldParticleRenderType(LodestoneRenderType renderType, ShaderHolder shaderHolder, ResourceLocation texture, Runnable blendFunction) {
        this(renderType, shaderHolder.getInstance(), texture, blendFunction);
    }

    public LodestoneWorldParticleRenderType(LodestoneRenderType renderType, Supplier<ShaderInstance> shader, ResourceLocation texture, Runnable blendFunction) {
        this.renderType = renderType;
        this.shader = shader;
        this.texture = texture;
        this.blendFunction = blendFunction;
    }

    @Override
    public BufferBuilder begin(Tesselator tesselator, TextureManager manager) {
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        blendFunction.run();
        RenderSystem.setShader(shader);
        RenderSystem.setShaderTexture(0, texture);
        return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    public LodestoneWorldParticleRenderType withDepthFade() {
        return DEPTH_FADE.apply(this);
    }

    private static LodestoneWorldParticleRenderType addDepthFade(LodestoneWorldParticleRenderType original) {
        final LodestoneRenderType renderType = LodestoneRenderTypes.copyAndStore(original, original.renderType, original.equals(LUMITRANSPARENT) ? ShaderUniformHandler.LUMITRANSPARENT_DEPTH_FADE : ShaderUniformHandler.DEPTH_FADE);
        return new LodestoneWorldParticleRenderType(renderType, original.shader, original.texture, original.blendFunction);
    }


}