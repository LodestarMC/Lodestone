package com.sammy.ortus.systems.postprocess;

import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.sammy.ortus.OrtusLib;
import com.sammy.ortus.helpers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

import static com.mojang.blaze3d.platform.GlConst.*;

/**
 * Abstract world space post-process pass.
 * If your shader needs the world space depth buffer, add a target called "depthMain" for it to automatically
 */
public abstract class PostProcessor {
    protected static final Minecraft MC = Minecraft.getInstance();

    public static final Collection<Pair<String, Consumer<Uniform>>> COMMON_UNIFORMS = Lists.newArrayList(
        Pair.of("cameraPos", u -> u.set(new Vector3f(MC.gameRenderer.getMainCamera().getPosition()))),
        Pair.of("lookVector", u -> u.set(MC.gameRenderer.getMainCamera().getLookVector())),
        Pair.of("upVector", u -> u.set(MC.gameRenderer.getMainCamera().getUpVector())),
        Pair.of("leftVector", u -> u.set(MC.gameRenderer.getMainCamera().getLeftVector())),
        Pair.of("invViewMat", u -> {
            Matrix4f invertedViewMatrix = new Matrix4f(PostProcessor.viewModelStack.last().pose());
            invertedViewMatrix.invert();
            u.set(invertedViewMatrix);
        }),
        Pair.of("invProjMat", u -> {
            Matrix4f invertedProjectionMatrix = new Matrix4f(RenderSystem.getProjectionMatrix());
            invertedProjectionMatrix.invert();
            u.set(invertedProjectionMatrix);
        }),
        Pair.of("nearPlaneDistance", u -> u.set(GameRenderer.PROJECTION_Z_NEAR)),
        Pair.of("farPlaneDistance", u -> u.set(MC.gameRenderer.getDepthFar())),
        Pair.of("fov", u -> u.set((float) Math.toRadians(MC.gameRenderer.getFov(MC.gameRenderer.getMainCamera(), MC.getFrameTime(), true) / 2D))),
        Pair.of("aspectRatio", u -> u.set((float) MC.getWindow().getWidth() / (float) MC.getWindow().getHeight()))
    );

    /**
     * Being updated every frame before calling applyPostProcess() by PostProcessHandler
     */
    public static PoseStack viewModelStack;

    private boolean initialized = false;
    protected PostChain postChain;
    protected EffectInstance[] effects;
    private RenderTarget tempDepthBuffer;
    private Collection<Pair<Uniform, Consumer<Uniform>>> defaultUniforms;

    private boolean isActive = true;

    protected double time;

    /**
     * Example: "octus:foo" points to octus:shaders/post/foo.json
     */
    public abstract ResourceLocation getPostChainLocation();

    public void init() {
        loadPostChain();

        if (postChain != null) {
            tempDepthBuffer = postChain.getTempTarget("depthMain");

            defaultUniforms = new ArrayList<>();
            for (EffectInstance e : effects) {
                for (Pair<String, Consumer<Uniform>> pair : COMMON_UNIFORMS) {
                    Uniform u = e.getUniform(pair.getFirst());
                    if (u != null) {
                        defaultUniforms.add(Pair.of(u, pair.getSecond()));
                    }
                }
            }
        }

        initialized = true;
    }

    /**
     * Load or reload the shader
     */
    public final void loadPostChain() {
        if (postChain != null) {
            postChain.close();
            postChain = null;
        }

        try {
            ResourceLocation file = getPostChainLocation();
            file = new ResourceLocation(file.getNamespace(), "shaders/post/" + file.getPath() + ".json");
            postChain = new PostChain(
                    MC.getTextureManager(),
                    MC.getResourceManager(),
                    MC.getMainRenderTarget(),
                    file
            );
            postChain.resize(MC.getWindow().getWidth(), MC.getWindow().getHeight());
            effects = postChain.passes.stream().map(PostPass::getEffect).toArray(EffectInstance[]::new);
        } catch (IOException | JsonParseException e) {
            OrtusLib.LOGGER.error("Failed to load post-processing shader: ", e);
        }
    }

    public final void copyDepthBuffer() {
        if (isActive) {
            if (postChain == null || tempDepthBuffer == null) return;

            tempDepthBuffer.copyDepthFrom(MC.getMainRenderTarget());

            // rebind the main framebuffer so that we don't mess up other things
            GlStateManager._glBindFramebuffer(GL_DRAW_FRAMEBUFFER, MC.getMainRenderTarget().frameBufferId);
        }
    }

    public void resize(int width, int height) {
        if (postChain != null) {
            postChain.resize(width, height);
            if (tempDepthBuffer != null)
                tempDepthBuffer.resize(width, height, Minecraft.ON_OSX);
        }
    }

    private void applyDefaultUniforms() {
        Arrays.stream(effects).forEach(e -> e.safeGetUniform("time").set((float) time));

        defaultUniforms.forEach(pair -> pair.getSecond().accept(pair.getFirst()));
    }

    public final void applyPostProcess() {
        if (isActive) {
            if (!initialized)
                init();

            if (postChain != null) {
                time += MC.getDeltaFrameTime() / 20.0;

                applyDefaultUniforms();

                beforeProcess(viewModelStack);
                if (!isActive) return;
                postChain.process(MC.getFrameTime());

                GlStateManager._glBindFramebuffer(GL_DRAW_FRAMEBUFFER, MC.getMainRenderTarget().frameBufferId);
                afterProcess();
            }
        }
    }

    /**
     * Set uniforms and bind textures here
     */
    public abstract void beforeProcess(PoseStack viewModelStack);

    /**
     * Unbind textures
     */
    public abstract void afterProcess();

    public final void setActive(boolean active) {
        this.isActive = active;

        if (!active)
            time = 0.0;
    }

    public final boolean isActive() {
        return isActive;
    }
}
