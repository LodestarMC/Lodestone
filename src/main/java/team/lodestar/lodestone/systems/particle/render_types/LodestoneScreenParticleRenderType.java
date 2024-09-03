package team.lodestar.lodestone.systems.particle.render_types;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import org.lwjgl.opengl.GL11;
import team.lodestar.lodestone.registry.client.*;

import java.util.function.Supplier;

public interface LodestoneScreenParticleRenderType {
    LodestoneScreenParticleRenderType ADDITIVE = new LodestoneScreenParticleRenderType() {
        @Override
        public void begin(BufferBuilder builder, TextureManager manager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            RenderSystem.setShader(LodestoneShaders.SCREEN_PARTICLE.getInstance());
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
    };
    LodestoneScreenParticleRenderType TRANSPARENT = new LodestoneScreenParticleRenderType() {
        @Override
        public void begin(BufferBuilder builder, TextureManager manager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.setShader(LodestoneShaders.SCREEN_PARTICLE.getInstance());
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
    };

    LodestoneScreenParticleRenderType LUMITRANSPARENT = new LodestoneScreenParticleRenderType() {
        @Override
        public void begin(BufferBuilder builder, TextureManager manager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            Supplier<ShaderInstance> instance = LodestoneShaders.SCREEN_PARTICLE.getInstance();
            RenderSystem.setShader(instance);
            instance.get().safeGetUniform("LumiTransparency").set(1f);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
            Supplier<ShaderInstance> instance = LodestoneShaders.SCREEN_PARTICLE.getInstance();
            instance.get().safeGetUniform("LumiTransparency").set(0f);
        }
    };

    void begin(BufferBuilder pBuilder, TextureManager pTextureManager);

    void end(Tesselator pTesselator);
}