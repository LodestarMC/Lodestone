package com.sammy.ortus.systems.rendering.multipass;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.sammy.ortus.helpers.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraftforge.client.ForgeHooksClient;

public class TextureSurgeon {
    public static void operate(FrameBufferBackedDynamicTexture tex, ShaderInstance... shaders) {
        Minecraft mc = Minecraft.getInstance();
        RenderTarget frameBuffer = tex.getFrameBuffer();
        frameBuffer.clear(Minecraft.ON_OSX);
        frameBuffer.bindWrite(true);

        int size = 16;

        RenderSystem.clear(256, Minecraft.ON_OSX);

        Matrix4f oldProjection = RenderSystem.getProjectionMatrix();

        Matrix4f matrix4f = Matrix4f.orthographic(0.0F, size, 0, size, 1000.0F, ForgeHooksClient.getGuiFarPlane());
        RenderSystem.setProjectionMatrix(matrix4f);

        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.setIdentity();
        posestack.translate(0.0D, 0.0D, 1000F - ForgeHooksClient.getGuiFarPlane());
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
        for (ShaderInstance shader : shaders) {
            RenderHelper.blit(posestack, shader, 0, 0, 16, 16, 0, 0, tex.getWidth(), tex.getHeight(), tex.getWidth(), tex.getHeight());
        }

        posestack.popPose();
        RenderSystem.applyModelViewMatrix();

        RenderSystem.setProjectionMatrix(oldProjection);

        RenderSystem.clear(256, Minecraft.ON_OSX);
        mc.getMainRenderTarget().bindWrite(true);

        tex.download();
        tex.getPixels().setPixelRGBA(1, 2, 0x00ffff);
        tex.upload();
    }
}