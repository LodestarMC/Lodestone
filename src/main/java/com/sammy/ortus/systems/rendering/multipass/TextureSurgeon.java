package com.sammy.ortus.systems.rendering.multipass;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sammy.ortus.helpers.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;

public class TextureSurgeon {
    public static void operate(FrameBufferBackedDynamicTexture tex, ShaderInstance... shaders) {
        Minecraft mc = Minecraft.getInstance();
        RenderTarget frameBuffer = tex.getFrameBuffer();
        frameBuffer.clear(Minecraft.ON_OSX);
        frameBuffer.bindWrite(true);
        RenderSystem.clear(256, Minecraft.ON_OSX);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.setIdentity();
        for (ShaderInstance shader : shaders) {
            RenderHelper.blit(posestack, shader, 0, 0, 16, 16, 0, 0, tex.getWidth(), tex.getHeight(), tex.getWidth(), tex.getHeight());
        }
        posestack.popPose();
        RenderSystem.clear(256, Minecraft.ON_OSX);
        mc.getMainRenderTarget().bindWrite(true);

        tex.download();
        tex.getPixels().setPixelRGBA(1, 2, 0x00ffff);
        tex.upload();
    }
}