package com.sammy.ortus.systems.rendering.multipass;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sammy.ortus.OrtusLib;
import com.sammy.ortus.systems.rendering.VFXBuilders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextureSurgeon {
    private static boolean dumpTextures = false;

    public static ResourceLocation PATIENT_TEXTURE = OrtusLib.ortusPrefix("textures/patient_texture");

    public static void operate(DynamicTexture tex, VFXBuilders.ScreenVFXBuilder builder, ShaderInstance... shaders) {
        PoseStack posestack = RenderSystem.getModelViewStack();
        Minecraft mc = Minecraft.getInstance();
        DynamicTexture drawTo = new DynamicTexture(PATIENT_TEXTURE, tex.getWidth(), tex.getHeight());

        posestack.pushPose();
        posestack.setIdentity();
        drawTo.bindWrite();

        int size = 16;
        for (ShaderInstance shader : shaders) {
            builder.setShaderTexture(tex.getTextureLocation()).setShader(() -> shader).setPositionWithWidth(0, 0, size, size).draw(posestack);
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