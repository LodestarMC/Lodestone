package com.sammy.ortus.systems.postprocess;

import com.google.gson.JsonParseException;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sammy.ortus.OrtusLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;

import static com.mojang.blaze3d.platform.GlConst.*;

//TODO: move to ortus lib

/**
 * Abstract world space post-process pass.
 * If your shader needs the world space depth buffer, add a target called "depthMain" for it to automatically
 */
public abstract class PostProcessor {
    protected static final Minecraft MC = Minecraft.getInstance();

    private boolean initialized = false;
    protected PostChain postChain;
    protected EffectInstance[] effects;
    private RenderTarget tempDepthBuffer;
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
            GlStateManager._glBindFramebuffer(GL_DRAW_FRAMEBUFFER, Minecraft.getInstance().getMainRenderTarget().frameBufferId);
        }
    }

    public void resize(int width, int height) {
        if (postChain != null) {
            postChain.resize(width, height);
            if (tempDepthBuffer != null)
                tempDepthBuffer.resize(width, height, Minecraft.ON_OSX);
        }
    }

    public final void applyPostProcess(PoseStack viewModelStack) {
        if (isActive) {
            if (!initialized)
                init();

            if (postChain != null) {
                time += MC.getDeltaFrameTime() / 20.0;

                beforeProcess(viewModelStack);
                if (!isActive) return;
                postChain.process(MC.getFrameTime());
                GlStateManager._glBindFramebuffer(GL_DRAW_FRAMEBUFFER, Minecraft.getInstance().getMainRenderTarget().frameBufferId);
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
