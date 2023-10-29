package team.lodestar.lodestone.systems.particle.render_types;

import com.mojang.datafixers.util.*;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;
import team.lodestar.lodestone.setup.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.setup.LodestoneShaderRegistry;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import org.lwjgl.opengl.GL11;
import team.lodestar.lodestone.systems.rendering.shader.*;

public class LodestoneWorldParticleRenderType implements ParticleRenderType {

    public static final LodestoneWorldParticleRenderType ADDITIVE = new LodestoneWorldParticleRenderType(
            LodestoneRenderTypeRegistry.ADDITIVE_PARTICLE, LodestoneShaderRegistry.PARTICLE, TextureAtlas.LOCATION_PARTICLES,
            GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

    public static final LodestoneWorldParticleRenderType TRANSPARENT = new LodestoneWorldParticleRenderType(
            LodestoneRenderTypeRegistry.TRANSPARENT_PARTICLE, LodestoneShaderRegistry.PARTICLE, TextureAtlas.LOCATION_PARTICLES,
            GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

    public static final LodestoneWorldParticleRenderType LUMITRANSPARENT = new LodestoneWorldParticleRenderType(
            LodestoneRenderTypeRegistry.LUMITRANSPARENT_PARTICLE, LodestoneShaderRegistry.PARTICLE, TextureAtlas.LOCATION_PARTICLES,
            GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

    public static final LodestoneWorldParticleRenderType TERRAIN_SHEET = new LodestoneWorldParticleRenderType(
            LodestoneRenderTypeRegistry.TRANSPARENT_BLOCK_PARTICLE, LodestoneShaderRegistry.PARTICLE, TextureAtlas.LOCATION_BLOCKS,
            GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

    public final RenderType renderType;
    protected final ShaderHolder shaderHolder;
    protected final ResourceLocation texture;
    protected final GlStateManager.SourceFactor srcAlpha;
    protected final GlStateManager.DestFactor dstAlpha;

    public LodestoneWorldParticleRenderType(RenderType renderType, ShaderHolder shaderHolder, ResourceLocation texture, GlStateManager.SourceFactor srcAlpha, GlStateManager.DestFactor dstAlpha) {
        this.renderType = renderType;
        this.shaderHolder = shaderHolder;
        this.texture = texture;
        this.srcAlpha = srcAlpha;
        this.dstAlpha = dstAlpha;
    }

    @Override
    public void begin(BufferBuilder builder, TextureManager manager) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(shaderHolder.getInstance());
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
}