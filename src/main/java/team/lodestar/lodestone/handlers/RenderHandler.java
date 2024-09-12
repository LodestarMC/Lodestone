package team.lodestar.lodestone.handlers;

import com.mojang.blaze3d.pipeline.*;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30C;
import team.lodestar.lodestone.helpers.RenderHelper;
import team.lodestar.lodestone.systems.rendering.rendeertype.ShaderUniformHandler;
import team.lodestar.lodestone.systems.rendering.shader.ExtendedShaderInstance;

import java.util.*;

import static team.lodestar.lodestone.systems.rendering.StateShards.NORMAL_TRANSPARENCY;

/**
 * A handler responsible for all the backend rendering processes.
 * To have additive transparency work in a minecraft environment, we need to buffer our rendering till after clouds and water have rendered.
 * This happens for particles, as well as all of our custom RenderTypes
 */
public class RenderHandler {
    public static final SequencedMap<RenderType, ByteBufferBuilder> BUFFERS = new LinkedHashMap<>();
    public static final SequencedMap<RenderType, ByteBufferBuilder> PARTICLE_BUFFERS = new LinkedHashMap<>();
    public static final SequencedMap<RenderType, ByteBufferBuilder> LATE_BUFFERS = new LinkedHashMap<>();
    public static final SequencedMap<RenderType, ByteBufferBuilder> LATE_PARTICLE_BUFFERS = new LinkedHashMap<>();
    public static final HashMap<RenderType, ShaderUniformHandler> UNIFORM_HANDLERS = new HashMap<>();
    public static final Collection<RenderType> TRANSPARENT_RENDER_TYPES = new ArrayList<>();

    public static RenderTarget LODESTONE_DEPTH_CACHE;
    public static LodestoneRenderLayer DELAYED_RENDER = new LodestoneRenderLayer(BUFFERS, PARTICLE_BUFFERS);
    public static LodestoneRenderLayer LATE_DELAYED_RENDER = new LodestoneRenderLayer(LATE_BUFFERS, LATE_PARTICLE_BUFFERS);

    public static Matrix4f MAIN_PROJ;
    public static Matrix4f MATRIX4F;

    public static float FOG_NEAR;
    public static float FOG_FAR;
    public static FogShape FOG_SHAPE;
    public static float FOG_RED, FOG_GREEN, FOG_BLUE;

    public static void onClientSetup(FMLClientSetupEvent event) {
        DELAYED_RENDER = new LodestoneRenderLayer(BUFFERS, PARTICLE_BUFFERS);
        LATE_DELAYED_RENDER = new LodestoneRenderLayer(LATE_BUFFERS, LATE_PARTICLE_BUFFERS);
    }

    public static void resize(int width, int height) {
        if (LODESTONE_DEPTH_CACHE != null) {
            LODESTONE_DEPTH_CACHE.resize(width, height, Minecraft.ON_OSX);
        }
    }

    public static void endBatches() {
        copyDepthBuffer(LODESTONE_DEPTH_CACHE);
        endBatches(DELAYED_RENDER);
        endBatches(LATE_DELAYED_RENDER);
    }

    public static void endBatches(LodestoneRenderLayer renderLayer) {
        Matrix4f last = new Matrix4f(RenderSystem.getModelViewMatrix());
        beginBufferedRendering();
        renderBufferedParticles(renderLayer, true);
        if (RenderHandler.MATRIX4F != null) {
            RenderSystem.getModelViewMatrix().set(MATRIX4F);
        }
        renderBufferedBatches(renderLayer, true);
        renderBufferedBatches(renderLayer, false);
        RenderSystem.getModelViewMatrix().set(last);
        renderBufferedParticles(renderLayer, false);

        endBufferedRendering();
    }

    public static void cacheFogData(ViewportEvent.RenderFog event) {
        FOG_NEAR = event.getNearPlaneDistance();
        FOG_FAR = event.getFarPlaneDistance();
        FOG_SHAPE = event.getFogShape();
    }

    public static void cacheFogData(ViewportEvent.ComputeFogColor event) {
        FOG_RED = event.getRed();
        FOG_GREEN = event.getGreen();
        FOG_BLUE = event.getBlue();
    }

    public static void beginBufferedRendering() {
        float[] shaderFogColor = RenderSystem.getShaderFogColor();
        float fogRed = shaderFogColor[0];
        float fogGreen = shaderFogColor[1];
        float fogBlue = shaderFogColor[2];
        float shaderFogStart = RenderSystem.getShaderFogStart();
        float shaderFogEnd = RenderSystem.getShaderFogEnd();
        FogShape shaderFogShape = RenderSystem.getShaderFogShape();

        RenderSystem.setShaderFogStart(FOG_NEAR);
        RenderSystem.setShaderFogEnd(FOG_FAR);
        RenderSystem.setShaderFogShape(FOG_SHAPE);
        RenderSystem.setShaderFogColor(FOG_RED, FOG_GREEN, FOG_BLUE);

        FOG_RED = fogRed;
        FOG_GREEN = fogGreen;
        FOG_BLUE = fogBlue;

        FOG_NEAR = shaderFogStart;
        FOG_FAR = shaderFogEnd;
        FOG_SHAPE = shaderFogShape;
    }

    public static void endBufferedRendering() {
        RenderSystem.setShaderFogStart(FOG_NEAR);
        RenderSystem.setShaderFogEnd(FOG_FAR);
        RenderSystem.setShaderFogShape(FOG_SHAPE);
        RenderSystem.setShaderFogColor(FOG_RED, FOG_GREEN, FOG_BLUE);
    }

    public static void renderBufferedParticles(LodestoneRenderLayer renderLayer, boolean transparentOnly) {
        renderBufferedBatches(renderLayer.getParticleTarget(), renderLayer.getParticleBuffers(), transparentOnly);
    }

    public static void renderBufferedBatches(LodestoneRenderLayer renderLayer, boolean transparentOnly) {
        renderBufferedBatches(renderLayer.getTarget(), renderLayer.getBuffers(), transparentOnly);
    }

    private static void renderBufferedBatches(MultiBufferSource.BufferSource bufferSource, SequencedMap<RenderType, ByteBufferBuilder> buffer, boolean transparentOnly) {
        if (transparentOnly) {
            endBatches(bufferSource, TRANSPARENT_RENDER_TYPES);
        } else {
            Collection<RenderType> nonTransparentRenderTypes = new ArrayList<>(buffer.keySet());
            nonTransparentRenderTypes.removeIf(TRANSPARENT_RENDER_TYPES::contains);
            endBatches(bufferSource, nonTransparentRenderTypes);
        }
    }

    public static void endBatches(MultiBufferSource.BufferSource source, Collection<RenderType> renderTypes) {
        for (RenderType type : renderTypes) {
            Optional<ShaderInstance> optional = RenderHelper.getShader(type);
            if (optional.isPresent()) {
                ShaderInstance instance = optional.get();
                if (UNIFORM_HANDLERS.containsKey(type)) {
                    ShaderUniformHandler handler = UNIFORM_HANDLERS.get(type);
                    handler.updateShaderData(instance);
                }
                instance.setSampler("SceneDepthBuffer", LODESTONE_DEPTH_CACHE.getDepthTextureId());
                instance.safeGetUniform("InvProjMat").set(new Matrix4f(RenderSystem.getProjectionMatrix()).invert());

                source.endBatch(type);
                if (instance instanceof ExtendedShaderInstance extendedShaderInstance) {
                    extendedShaderInstance.setUniformDefaults();
                }
            }
            else {
                source.endBatch(type);
            }
        }
    }

    //TODO: offer some actual option here to decide if particle or not
    public static void addRenderType(RenderType renderType) {
        final boolean isParticle = renderType.name.contains("particle");
        SequencedMap<RenderType, ByteBufferBuilder> buffers = isParticle ? PARTICLE_BUFFERS : BUFFERS;
        SequencedMap<RenderType, ByteBufferBuilder> lateBuffers = isParticle ? LATE_PARTICLE_BUFFERS : LATE_BUFFERS;
        buffers.put(renderType, new ByteBufferBuilder(786432));
        lateBuffers.put(renderType, new ByteBufferBuilder(786432));
        if (NORMAL_TRANSPARENCY.equals(RenderHelper.getTransparencyShard(renderType))) {
            TRANSPARENT_RENDER_TYPES.add(renderType);
        }
    }

    public static void copyDepthBuffer(RenderTarget tempRenderTarget) {
        setupDepthBuffer();
        enableStencil();
        if (tempRenderTarget == null) return;
        RenderTarget mainRenderTarget = Minecraft.getInstance().getMainRenderTarget();
        tempRenderTarget.copyDepthFrom(mainRenderTarget);
        GlStateManager._glBindFramebuffer(GL30C.GL_DRAW_FRAMEBUFFER, mainRenderTarget.frameBufferId);
    }

    public static void setupDepthBuffer() {
        if (LODESTONE_DEPTH_CACHE == null) {
            LODESTONE_DEPTH_CACHE = new TextureTarget(Minecraft.getInstance().getMainRenderTarget().width, Minecraft.getInstance().getMainRenderTarget().height, true, Minecraft.ON_OSX);
        }
    }

    public static void enableStencil() {
        if (Minecraft.getInstance().getMainRenderTarget().isStencilEnabled()) {
            LODESTONE_DEPTH_CACHE.enableStencil();
        }
    }

    public static class LodestoneRenderLayer {

        protected final SequencedMap<RenderType, ByteBufferBuilder> buffers;
        protected final SequencedMap<RenderType, ByteBufferBuilder> particleBuffers;

        protected final MultiBufferSource.BufferSource target;
        protected final MultiBufferSource.BufferSource particleTarget;

        public LodestoneRenderLayer(SequencedMap<RenderType, ByteBufferBuilder> buffers, SequencedMap<RenderType, ByteBufferBuilder> particleBuffers) {
            this.buffers = buffers;
            this.particleBuffers = particleBuffers;
            this.target = MultiBufferSource.immediateWithBuffers(buffers, new ByteBufferBuilder(786432));
            this.particleTarget = MultiBufferSource.immediateWithBuffers(particleBuffers, new ByteBufferBuilder(786432));
        }

        public SequencedMap<RenderType, ByteBufferBuilder> getBuffers() {
            return buffers;
        }

        public SequencedMap<RenderType, ByteBufferBuilder> getParticleBuffers() {
            return particleBuffers;
        }

        public MultiBufferSource.BufferSource getTarget() {
            return target;
        }

        public MultiBufferSource.BufferSource getParticleTarget() {
            return particleTarget;
        }
    }
}