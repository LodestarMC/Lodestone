package team.lodestar.lodestone.handlers;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.*;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.ModList;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.helpers.RenderHelper;
import team.lodestar.lodestone.setup.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.rendering.ExtendedShaderInstance;
import team.lodestar.lodestone.systems.rendering.ShaderUniformHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashMap;

/**
 * A handler responsible for all the backend rendering processes.
 * To have additive transparency work in a minecraft environment, we need to buffer our rendering till after clouds and water have rendered.
 * This happens for particles, as well as all of our custom RenderTypes
 */
public class RenderHandler {
    public static HashMap<RenderType, BufferBuilder> BUFFERS = new HashMap<>();
    public static HashMap<RenderType, BufferBuilder> PARTICLE_BUFFERS = new HashMap<>();
    public static boolean LARGER_BUFFER_SOURCES = ModList.get().isLoaded("rubidium");

    public static HashMap<RenderType, ShaderUniformHandler> UNIFORM_HANDLERS = new HashMap<>();
    public static MultiBufferSource.BufferSource DELAYED_RENDER;
    public static MultiBufferSource.BufferSource DELAYED_PARTICLE_RENDER;

    public static Matrix4f PARTICLE_MATRIX;

    public static float FOG_NEAR;
    public static float FOG_FAR;
    public static FogShape FOG_SHAPE;
    public static float FOG_RED, FOG_GREEN, FOG_BLUE;

    public static void onClientSetup(FMLClientSetupEvent event) {
        int size = LARGER_BUFFER_SOURCES ? 262144 : 256;
        DELAYED_RENDER = MultiBufferSource.immediateWithBuffers(BUFFERS, new BufferBuilder(size));
        DELAYED_PARTICLE_RENDER = MultiBufferSource.immediateWithBuffers(PARTICLE_BUFFERS, new BufferBuilder(size));
    }

    public static void cacheFogData(EntityViewRenderEvent.RenderFogEvent event) {
        FOG_NEAR = event.getNearPlaneDistance();
        FOG_FAR = event.getFarPlaneDistance();
        FOG_SHAPE = event.getFogShape();
    }

    public static void cacheFogData(EntityViewRenderEvent.FogColors event) {
        FOG_RED = event.getRed();
        FOG_GREEN = event.getGreen();
        FOG_BLUE = event.getBlue();
    }

    public static void beginBufferedRendering(PoseStack poseStack) {
        poseStack.pushPose();
        LightTexture lightTexture = Minecraft.getInstance().gameRenderer.lightTexture();
        lightTexture.turnOnLightLayer();
        RenderSystem.activeTexture(org.lwjgl.opengl.GL13.GL_TEXTURE2);
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);

        float fogRed = RenderSystem.getShaderFogColor()[0];
        float fogGreen = RenderSystem.getShaderFogColor()[1];
        float fogBlue = RenderSystem.getShaderFogColor()[2];
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

    public static void renderBufferedBatches(PoseStack poseStack) {
        endBatches(DELAYED_RENDER, BUFFERS);
    }

    public static void renderBufferedParticles(PoseStack poseStack) {
        if (ClientConfig.DELAYED_PARTICLE_RENDERING.getConfigValue()) {
            RenderSystem.getModelViewStack().pushPose();
            RenderSystem.getModelViewStack().setIdentity();
            if (PARTICLE_MATRIX != null) {
                RenderSystem.getModelViewStack().mulPoseMatrix(PARTICLE_MATRIX);
            }
            RenderSystem.applyModelViewMatrix();
            DELAYED_PARTICLE_RENDER.endBatch(LodestoneRenderTypeRegistry.ADDITIVE_PARTICLE);
            DELAYED_PARTICLE_RENDER.endBatch(LodestoneRenderTypeRegistry.TRANSPARENT_PARTICLE);
            endBatches(DELAYED_PARTICLE_RENDER, PARTICLE_BUFFERS);
            RenderSystem.getModelViewStack().popPose();
            RenderSystem.applyModelViewMatrix();
        }
    }

    public static void endBufferedRendering(PoseStack poseStack) {
        LightTexture lightTexture = Minecraft.getInstance().gameRenderer.lightTexture();
        RenderSystem.setShaderFogStart(FOG_NEAR);
        RenderSystem.setShaderFogEnd(FOG_FAR);
        RenderSystem.setShaderFogShape(FOG_SHAPE);
        RenderSystem.setShaderFogColor(FOG_RED, FOG_GREEN, FOG_BLUE);

        poseStack.popPose();
        lightTexture.turnOffLightLayer();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(true);
    }

    public static void endBatches(MultiBufferSource.BufferSource source, HashMap<RenderType, BufferBuilder> buffers) {
        for (RenderType type : buffers.keySet()) {
            ShaderInstance instance = RenderHelper.getShader(type);
            if (UNIFORM_HANDLERS.containsKey(type)) {
                ShaderUniformHandler handler = UNIFORM_HANDLERS.get(type);
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
        int size = LARGER_BUFFER_SOURCES ? 262144 : type.bufferSize();
        HashMap<RenderType, BufferBuilder> buffers = BUFFERS;
        if (type.name.contains("particle")) {
            buffers = PARTICLE_BUFFERS;
        }
        buffers.put(type, new BufferBuilder(size));
    }

//    public static void copyDepthBuffer() {
//        if (COPIED_DEPTH_BUFFER) {
//            return;
//        }
//        if (PARTICLE_DEPTH_BUFFER == null) {
//            Window window = Minecraft.getInstance().getWindow();
//            PARTICLE_DEPTH_BUFFER = new TextureTarget(window.getWidth(), window.getHeight(), true, Minecraft.ON_OSX);
//            PARTICLE_DEPTH_BUFFER.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
//            PARTICLE_DEPTH_BUFFER.clear(ON_OSX);
//            return;
//        }
//        RenderTarget mainRenderTarget = Minecraft.getInstance().getMainRenderTarget();
//        PARTICLE_DEPTH_BUFFER.copyDepthFrom(mainRenderTarget);
//        GlStateManager._glBindFramebuffer(GL_DRAW_FRAMEBUFFER, mainRenderTarget.frameBufferId);
//        COPIED_DEPTH_BUFFER = true;
//    }
}