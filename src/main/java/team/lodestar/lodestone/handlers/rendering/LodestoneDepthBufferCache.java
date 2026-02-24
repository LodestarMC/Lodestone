package team.lodestar.lodestone.handlers.rendering;

import com.mojang.blaze3d.pipeline.*;
import com.mojang.blaze3d.platform.*;
import net.minecraft.client.*;
import org.lwjgl.opengl.*;

public class LodestoneDepthBufferCache {

    public static RenderTarget LODESTONE_DEPTH_BUFFER;

    public static void copyDepthBuffer() {
        copyDepthBuffer(LODESTONE_DEPTH_BUFFER);
    }

    public static void copyDepthBuffer(RenderTarget copyTo) {
        setupDepthBuffer();
        enableStencil();
        if (copyTo == null) return;
        var mainRenderTarget = Minecraft.getInstance().getMainRenderTarget();
        copyTo.copyDepthFrom(mainRenderTarget);
        GlStateManager._glBindFramebuffer(GL30C.GL_DRAW_FRAMEBUFFER, mainRenderTarget.frameBufferId);
    }

    public static void setupDepthBuffer() {
        if (LODESTONE_DEPTH_BUFFER == null) {
            LODESTONE_DEPTH_BUFFER = new TextureTarget(Minecraft.getInstance().getMainRenderTarget().width, Minecraft.getInstance().getMainRenderTarget().height, true, Minecraft.ON_OSX);
        }
    }

    public static void enableStencil() {
        if (Minecraft.getInstance().getMainRenderTarget().isStencilEnabled()) {
            LODESTONE_DEPTH_BUFFER.enableStencil();
        }
    }

    public static void resize(int width, int height) {
        if (LODESTONE_DEPTH_BUFFER != null) {
            LODESTONE_DEPTH_BUFFER.resize(width, height, Minecraft.ON_OSX);
        }
    }
}