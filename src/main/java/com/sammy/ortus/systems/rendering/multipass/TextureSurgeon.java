package com.sammy.ortus.systems.rendering.multipass;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sammy.ortus.OrtusLib;
import com.sammy.ortus.systems.rendering.VFXBuilders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextureSurgeon {
    private static boolean dumpTextures = false;

    public static void operate(DynamicTexture tex, VFXBuilders.ScreenVFXBuilder builder, ShaderInstance... shaders) {
        Minecraft mc = Minecraft.getInstance();
        RenderTarget frameBuffer = tex.getFrameBuffer();
        frameBuffer.clear(Minecraft.ON_OSX);
        frameBuffer.bindWrite(true);
        RenderSystem.clear(256, Minecraft.ON_OSX);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.setIdentity();
        int size = 16;
        for (ShaderInstance shader : shaders) {
            RenderSystem.setShaderTexture(0, tex.getTextureLocation());
            builder.setShader(() -> shader).setPositionWithWidth(0, 0, size, size).begin().blit(posestack).end();
        }
        if (dumpTextures) {
            try {
                Path outputFolder = Paths.get("texture_dump");
                outputFolder = Files.createDirectories(outputFolder);
                tex.saveTextureToFile(outputFolder);
            } catch (IOException e) {
                OrtusLib.LOGGER.error("Failed to dump texture maps with error.", e);
            }
        }
        posestack.popPose();
        mc.getMainRenderTarget().bindWrite(true);
    }

    public static void enableTextureDump() {
        dumpTextures = true;
    }

    public static void disableTextureDump() {
        dumpTextures = false;
    }
}