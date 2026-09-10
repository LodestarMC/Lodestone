package team.lodestar.lodestone.modules.rendering;

import com.mojang.blaze3d.pipeline.*;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.neoforged.neoforge.client.event.*;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30C;
import team.lodestar.lodestone.helpers.RenderHelper;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderLayer;
import team.lodestar.lodestone.systems.rendering.rendeertype.LodestoneRenderType;
import team.lodestar.lodestone.systems.rendering.shader.ExtendedShaderInstance;

import java.util.*;

/**
 * A handler responsible for all the backend rendering processes.
 * To have additive transparency work in a minecraft environment, we need to buffer our rendering till after clouds and water have rendered.
 * This happens for particles, as well as all of our custom RenderTypes
 */
public class LodestoneRenderingSystem {

    public static RenderTarget LODESTONE_DEPTH_CACHE;

    public static LodestoneRenderLayer DEFERRED_RENDER = new LodestoneRenderLayer();
    public static LodestoneRenderLayer LATE_DEFERRED_RENDER = new LodestoneRenderLayer();

    public static Matrix4f MODEL_VIEW;

    public static float FOG_NEAR, FOG_FAR;
    public static float FOG_RED, FOG_GREEN, FOG_BLUE;
    public static FogShape FOG_SHAPE;

    public static void resize(int width, int height) {
        if (LODESTONE_DEPTH_CACHE != null) {
            LODESTONE_DEPTH_CACHE.resize(width, height, Minecraft.ON_OSX);
        }
    }

    public static void render() {
        copyDepthBuffer(LODESTONE_DEPTH_CACHE);
        applyCachedFogData();
        DEFERRED_RENDER.endBatches();
        LATE_DEFERRED_RENDER.endBatches();
        restoreFogData();
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

    public static void cacheFogData(ViewportEvent.RenderFog event) {
        FOG_NEAR = event.getNearPlaneDistance();
        FOG_FAR = event.getFarPlaneDistance();
        FOG_SHAPE = event.getFogShape();
    }

    public static void cacheFogColors(ViewportEvent.ComputeFogColor event) {
        FOG_RED = event.getRed();
        FOG_GREEN = event.getGreen();
        FOG_BLUE = event.getBlue();
    }

    public static void applyCachedFogData() {
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

    public static void restoreFogData() {
        RenderSystem.setShaderFogStart(FOG_NEAR);
        RenderSystem.setShaderFogEnd(FOG_FAR);
        RenderSystem.setShaderFogShape(FOG_SHAPE);
        RenderSystem.setShaderFogColor(FOG_RED, FOG_GREEN, FOG_BLUE);
    }

    public static void updateUniforms(RenderType renderType) {
        Optional<ShaderInstance> optional = RenderHelper.getShader(renderType);
        if (optional.isEmpty()) {
            return;
        }
        ShaderInstance shader = optional.get();
        if (renderType instanceof LodestoneRenderType lodestoneRenderType) {
            var data = lodestoneRenderType.getUniformData();
            if (data != null) {
                data.applyData(shader);
            }
        }
        shader.setSampler("SceneDepthBuffer", LodestoneRenderingSystem.LODESTONE_DEPTH_CACHE.getDepthTextureId());
        shader.setSampler("SceneDiffuseBuffer", Minecraft.getInstance().getMainRenderTarget().getColorTextureId());
        shader.safeGetUniform("InvProjMat").set(new Matrix4f(RenderSystem.getProjectionMatrix()).invert());
    }

    public static void resetUniforms(RenderType renderType) {
        var optional = RenderHelper.getShader(renderType);
        if (optional.isEmpty()) {
            return;
        }
        if (!(optional.get() instanceof ExtendedShaderInstance shader)) {
            return;
        }
        shader.applyUniformDefaults();
    }
}