package team.lodestar.lodestone.systems.particle.render_types;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.*;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.handlers.*;
import team.lodestar.lodestone.registry.client.*;
import team.lodestar.lodestone.systems.rendering.*;
import team.lodestar.lodestone.systems.rendering.rendeertype.*;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

import java.util.function.*;

public class LodestoneWorldParticleRenderType implements ParticleRenderType {

    public static final Function<LodestoneWorldParticleRenderType, LodestoneWorldParticleRenderType> DEPTH_FADE = Util.memoize(LodestoneWorldParticleRenderType::addDepthFade);

    public static final LodestoneWorldParticleRenderType ADDITIVE = new LodestoneWorldParticleRenderType(
            LodestoneRenderTypeRegistry.ADDITIVE_PARTICLE, LodestoneShaderRegistry.PARTICLE, TextureAtlas.LOCATION_PARTICLES,
            LodestoneRenderTypeRegistry.ADDITIVE_FUNCTION);

    public static final LodestoneWorldParticleRenderType TRANSPARENT = new LodestoneWorldParticleRenderType(
            LodestoneRenderTypeRegistry.TRANSPARENT_PARTICLE, LodestoneShaderRegistry.PARTICLE, TextureAtlas.LOCATION_PARTICLES,
            LodestoneRenderTypeRegistry.TRANSPARENT_FUNCTION);

    public static final LodestoneWorldParticleRenderType LUMITRANSPARENT = new LodestoneWorldParticleRenderType(
            LodestoneRenderTypeRegistry.LUMITRANSPARENT_PARTICLE, LodestoneShaderRegistry.PARTICLE, TextureAtlas.LOCATION_PARTICLES,
            LodestoneRenderTypeRegistry.TRANSPARENT_FUNCTION);

    public static final LodestoneWorldParticleRenderType TERRAIN_SHEET = new LodestoneWorldParticleRenderType(
            LodestoneRenderTypeRegistry.TRANSPARENT_BLOCK_PARTICLE, LodestoneShaderRegistry.PARTICLE, TextureAtlas.LOCATION_BLOCKS,
            LodestoneRenderTypeRegistry.TRANSPARENT_FUNCTION);

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
    public void begin(BufferBuilder builder, TextureManager manager) {
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        blendFunction.run();
        RenderSystem.setShader(shader);
        RenderSystem.setShaderTexture(0, texture);
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    @Override
    public void end(Tesselator pTesselator) {
        pTesselator.end();
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
    }

    public LodestoneWorldParticleRenderType withDepthFade() {
        return DEPTH_FADE.apply(this);
    }

    private static LodestoneWorldParticleRenderType addDepthFade(LodestoneWorldParticleRenderType original) {
        final LodestoneRenderType renderType = LodestoneRenderTypeRegistry.copyAndStore(original, original.renderType, original.equals(LUMITRANSPARENT) ? ShaderUniformHandler.LUMITRANSPARENT_DEPTH_FADE : ShaderUniformHandler.DEPTH_FADE);
        return new LodestoneWorldParticleRenderType(renderType, original.shader, original.texture, original.blendFunction);
    }
}