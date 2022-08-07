package team.lodestar.lodestone.handlers;

import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.platform.Window;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.helpers.RenderHelper;
import team.lodestar.lodestone.setup.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.rendering.ExtendedShaderInstance;
import team.lodestar.lodestone.systems.rendering.ShaderUniformHandler;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashMap;

import static com.mojang.blaze3d.platform.GlConst.GL_DRAW_FRAMEBUFFER;
import static net.minecraft.client.Minecraft.ON_OSX;

/**
 * A handler responsible for all the backend rendering processes.
 * To have additive transparency work in a minecraft environment, we need to buffer our rendering till after clouds and water have rendered.
 * This happens for particles, as well as all of our custom RenderTypes
 */
public class RenderHandler {
    public static HashMap<RenderType, BufferBuilder> EARLY_BUFFERS = new HashMap<>();
    public static HashMap<RenderType, BufferBuilder> BUFFERS = new HashMap<>();
    public static HashMap<RenderType, BufferBuilder> LATE_BUFFERS = new HashMap<>();
    public static HashMap<RenderType, ShaderUniformHandler> HANDLERS = new HashMap<>();
    public static MultiBufferSource.BufferSource EARLY_DELAYED_RENDER;
    public static MultiBufferSource.BufferSource DELAYED_RENDER;
    public static MultiBufferSource.BufferSource LATE_DELAYED_RENDER;
    public static MultiBufferSource.BufferSource BLOOM_BUFFER;
    public static Matrix4f PARTICLE_MATRIX = null;
    public static boolean COPIED_DEPTH_BUFFER = false;
    public static RenderTarget PARTICLE_DEPTH_BUFFER = null;

    public static void onClientSetup(FMLClientSetupEvent event) {
        EARLY_DELAYED_RENDER = MultiBufferSource.immediateWithBuffers(EARLY_BUFFERS, new BufferBuilder(256));
        DELAYED_RENDER = MultiBufferSource.immediateWithBuffers(BUFFERS, new BufferBuilder(256));
        LATE_DELAYED_RENDER = MultiBufferSource.immediateWithBuffers(LATE_BUFFERS, new BufferBuilder(256));
    }
    public static void resize(int width, int height) {
        if (PARTICLE_DEPTH_BUFFER != null) {
            PARTICLE_DEPTH_BUFFER.resize(width, height, Minecraft.ON_OSX);
        }
    }
    public static void renderLast(RenderLevelLastEvent event) {
        copyDepthBuffer();
        event.getPoseStack().pushPose();
        if (ClientConfig.DELAYED_PARTICLE_RENDERING.getConfigValue()) {
            RenderSystem.getModelViewStack().pushPose();
            RenderSystem.getModelViewStack().setIdentity();
            if (PARTICLE_MATRIX != null) {
                RenderSystem.getModelViewStack().mulPoseMatrix(PARTICLE_MATRIX);
            }
            RenderSystem.applyModelViewMatrix();
            DELAYED_RENDER.endBatch(LodestoneRenderTypeRegistry.TRANSPARENT_PARTICLE);
            DELAYED_RENDER.endBatch(LodestoneRenderTypeRegistry.ADDITIVE_PARTICLE);
            RenderSystem.getModelViewStack().popPose();
            RenderSystem.applyModelViewMatrix();
        }
        endBatches(EARLY_DELAYED_RENDER, EARLY_BUFFERS);
        endBatches(DELAYED_RENDER, BUFFERS);
        endBatches(LATE_DELAYED_RENDER, LATE_BUFFERS);
        event.getPoseStack().popPose();

        COPIED_DEPTH_BUFFER = false;
    }

    public static void endBatches(MultiBufferSource.BufferSource source, HashMap<RenderType, BufferBuilder> buffers) {
        for (RenderType type : buffers.keySet()) {
            ShaderInstance instance = RenderHelper.getShader(type);
            if (HANDLERS.containsKey(type)) {
                ShaderUniformHandler handler = HANDLERS.get(type);
                handler.updateShaderData(instance);
            }
            source.endBatch(type);
            if (instance instanceof ExtendedShaderInstance extendedShaderInstance) {
                extendedShaderInstance.setUniformDefaults();
            }
        }
        source.endBatch();
    }

    public static void addRenderType(RenderType type) {
        RenderHandler.EARLY_BUFFERS.put(type, new BufferBuilder(type.bufferSize()));
        RenderHandler.BUFFERS.put(type, new BufferBuilder(type.bufferSize()));
        RenderHandler.LATE_BUFFERS.put(type, new BufferBuilder(type.bufferSize()));
    }

    public static void copyDepthBuffer() {
        if (COPIED_DEPTH_BUFFER || PARTICLE_DEPTH_BUFFER == null) {
            Window window = Minecraft.getInstance().getWindow();
            PARTICLE_DEPTH_BUFFER = new MainTarget(window.getWidth(), window.getHeight());
            PARTICLE_DEPTH_BUFFER.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
            PARTICLE_DEPTH_BUFFER.clear(ON_OSX);
            return;
        }
        RenderTarget mainRenderTarget = Minecraft.getInstance().getMainRenderTarget();
        PARTICLE_DEPTH_BUFFER.copyDepthFrom(mainRenderTarget);
        GlStateManager._glBindFramebuffer(GL_DRAW_FRAMEBUFFER, mainRenderTarget.frameBufferId);
        COPIED_DEPTH_BUFFER = true;
    }
}