package team.lodestar.lodestone.handlers.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import org.joml.Matrix4f;
import team.lodestar.lodestone.systems.rendering.*;
import team.lodestar.lodestone.systems.rendering.buffer.LodestoneRenderLayer;
import team.lodestar.lodestone.systems.rendering.shader.LodestoneCoreShaderInstance;

import java.util.*;
import java.util.function.*;

/**
 * A handler responsible for all the backend rendering processes.
 * To have additive transparency work in a minecraft environment, we need to buffer our rendering till after clouds and water have rendered.
 * This happens for particles, as well as all of our custom RenderTypes
 */
public class LodestoneRenderHandler {

    public static LodestoneRenderLayer DEFERRED_RENDER = new LodestoneRenderLayer();
    public static LodestoneRenderLayer LATE_DEFERRED_RENDER = new LodestoneRenderLayer();

    public static Matrix4f MODEL_VIEW;

    public static void render() {
        LodestoneDepthBufferCache.copyDepthBuffer();
        DEFERRED_RENDER.endBatches();
        LATE_DEFERRED_RENDER.endBatches();
    }

    public static void cacheModelViewMatrix(Matrix4f modelViewMatrix) {
        MODEL_VIEW = new Matrix4f(modelViewMatrix);
    }

    public static void restoreModelViewMatrix() {
        setModelViewMatrix(MODEL_VIEW);
    }

    public static void clearModelViewMatrix() {
        setModelViewMatrix(new Matrix4f());
    }

    public static void setModelViewMatrix(Matrix4f modelViewMatrix) {
        RenderSystem.getModelViewMatrix().set(modelViewMatrix);
    }

    public static void updateUniforms(RenderType renderType) {
        var optional = getShader(renderType);
        if (optional.isPresent()) {
            ShaderInstance shader = optional.get();
            if (renderType instanceof LodestoneRenderType lodestoneRenderType) {
                var handler = lodestoneRenderType.getUniformHandler();
                if (handler != null) {
                    handler.updateShaderData(shader);
                }
            }
            shader.setSampler("SceneDepthBuffer", LodestoneDepthBufferCache.LODESTONE_DEPTH_BUFFER.getDepthTextureId());
            shader.setSampler("SceneDiffuseBuffer", Minecraft.getInstance().getMainRenderTarget().getColorTextureId());
            shader.safeGetUniform("InvProjMat").set(new Matrix4f(RenderSystem.getProjectionMatrix()).invert());
        }
    }

    public static void restoreUniformValues(RenderType renderType) {
        var optional = getShader(renderType);
        if (optional.isEmpty()) {
            return;
        }
        if (optional.get() instanceof LodestoneCoreShaderInstance shader) {
            shader.restoreUniformValues();
        }
    }

    public static Optional<ShaderInstance> getShader(RenderType type) {
        if (type instanceof LodestoneRenderType renderType) {
            Optional<Supplier<ShaderInstance>> shader = renderType.state.shaderState.shader;
            if (shader.isPresent()) {
                return Optional.ofNullable(shader.get().get());
            }
        }
        return Optional.empty();
    }
}