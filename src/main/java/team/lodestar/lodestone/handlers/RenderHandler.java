package team.lodestar.lodestone.handlers;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.*;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.ModList;
import team.lodestar.lodestone.helpers.RenderHelper;
import team.lodestar.lodestone.systems.rendering.shader.ExtendedShaderInstance;
import team.lodestar.lodestone.systems.rendering.rendeertype.ShaderUniformHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.*;

import static team.lodestar.lodestone.systems.rendering.StateShards.NORMAL_TRANSPARENCY;

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

    public static PoseStack MAIN_POSE_STACK;
    public static Matrix4f MATRIX4F;

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
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);

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

    public static void renderBufferedParticles(boolean transparentOnly) {
        renderBufferedBatches(DELAYED_PARTICLE_RENDER, PARTICLE_BUFFERS, transparentOnly);
    }

    public static void renderBufferedBatches(boolean transparentOnly) {
        renderBufferedBatches(DELAYED_RENDER, BUFFERS, transparentOnly);
    }

    private static void renderBufferedBatches(MultiBufferSource.BufferSource bufferSource, HashMap<RenderType, BufferBuilder> buffer, boolean transparentOnly) {
        Collection<RenderType> transparentRenderTypes = new ArrayList<>();
        for (RenderType renderType : buffer.keySet()) {
            RenderStateShard.TransparencyStateShard transparency = RenderHelper.getTransparencyShard(renderType);
            if (transparency.equals(NORMAL_TRANSPARENCY)) {
                transparentRenderTypes.add(renderType);
            }
        }
        if (transparentOnly) {
            endBatches(bufferSource, transparentRenderTypes);
        }
        else {
            Collection<RenderType> nonTransparentRenderTypes = new ArrayList<>(buffer.keySet());
            nonTransparentRenderTypes.removeIf(transparentRenderTypes::contains);
            endBatches(bufferSource, nonTransparentRenderTypes);
        }
    }

    public static void endBufferedRendering(PoseStack poseStack) {
        RenderSystem.setShaderFogStart(FOG_NEAR);
        RenderSystem.setShaderFogEnd(FOG_FAR);
        RenderSystem.setShaderFogShape(FOG_SHAPE);
        RenderSystem.setShaderFogColor(FOG_RED, FOG_GREEN, FOG_BLUE);
        poseStack.popPose();
    }

    public static void endBatches(MultiBufferSource.BufferSource source, Collection<RenderType> renderTypes) {
        for (RenderType type : renderTypes) {
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
    }

    public static void addRenderType(RenderType type) {
        int size = LARGER_BUFFER_SOURCES ? 262144 : type.bufferSize();
        HashMap<RenderType, BufferBuilder> buffers = type.name.contains("particle") ? PARTICLE_BUFFERS : BUFFERS;
        buffers.put(type, new BufferBuilder(size));
    }
}