package com.sammy.ortus.handlers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.sammy.ortus.config.ClientConfig;
import com.sammy.ortus.helpers.RenderHelper;
import com.sammy.ortus.systems.rendering.ExtendedShaderInstance;
import com.sammy.ortus.setup.OrtusRenderTypeRegistry;
import com.sammy.ortus.systems.rendering.ShaderUniformHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashMap;

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

    public static void onClientSetup(FMLClientSetupEvent event) {
        EARLY_DELAYED_RENDER = MultiBufferSource.immediateWithBuffers(EARLY_BUFFERS, new BufferBuilder(256));
        DELAYED_RENDER = MultiBufferSource.immediateWithBuffers(BUFFERS, new BufferBuilder(256));
        LATE_DELAYED_RENDER = MultiBufferSource.immediateWithBuffers(LATE_BUFFERS, new BufferBuilder(256));
    }

    public static void renderLast(RenderLevelLastEvent event) {
        event.getPoseStack().pushPose();
        if (ClientConfig.DELAYED_PARTICLE_RENDERING.getConfigValue()) {
            RenderSystem.getModelViewStack().pushPose();
            RenderSystem.getModelViewStack().setIdentity();
            if (PARTICLE_MATRIX != null) {
                RenderSystem.getModelViewStack().mulPoseMatrix(PARTICLE_MATRIX);
            }
            RenderSystem.applyModelViewMatrix();
            DELAYED_RENDER.endBatch(OrtusRenderTypeRegistry.ADDITIVE_PARTICLE);
            RenderSystem.getModelViewStack().popPose();
            RenderSystem.applyModelViewMatrix();
        }
        endBatches(EARLY_DELAYED_RENDER, EARLY_BUFFERS);
        endBatches(DELAYED_RENDER, BUFFERS);
        endBatches(LATE_DELAYED_RENDER, LATE_BUFFERS);
        event.getPoseStack().popPose();
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
}