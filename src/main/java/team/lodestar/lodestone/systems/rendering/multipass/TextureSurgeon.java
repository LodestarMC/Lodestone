package team.lodestar.lodestone.systems.rendering.multipass;

import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import team.lodestar.lodestone.LodestoneLib;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import org.antlr.v4.runtime.misc.Triple;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

/**
* Uh do not touch this class, it's an incomplete mess.
**/
public class TextureSurgeon {
    private static boolean dumpTextures = false;

    public static ResourceLocation PATIENT_TEXTURE = LodestoneLib.lodestonePath("textures/patient_texture");

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
                LodestoneLib.LOGGER.error("Failed to dump texture maps with error.", e);
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

    public static class TextureSurgeonTest { //TODO:  make it work please
        public static final ConcurrentHashMap<Triple<ResourceLocation, Integer, Integer>, DynamicTexture> DRAW_TO_TEXTURES = new ConcurrentHashMap<>();
        public static final ConcurrentHashMap<Triple<ResourceLocation, Integer, Integer>, DynamicTexture> SOURCE_TEXTURES = new ConcurrentHashMap<>();
        public static ResourceLocation DEFAULT_PATIENT_TEXTURE = LodestoneLib.lodestonePath("textures/vfx/patient_texture.png");

        boolean shouldDumpTextures;
        ResourceLocation drawToLocation = DEFAULT_PATIENT_TEXTURE;
        DynamicTexture drawToTexture;
        DynamicTexture sourceTexture;
        Matrix4f oldProjection;

        public TextureSurgeonTest shouldDumpTextures() {
            this.shouldDumpTextures = true;
            return this;
        }

        public TextureSurgeonTest setSourceTexture(ResourceLocation sourceTexture, int size) {
            return this.setSourceTexture(sourceTexture, size, size);
        }
        public TextureSurgeonTest setSourceTexture(ResourceLocation sourceTexture, int width, int height) {
            return setSourceTexture(SOURCE_TEXTURES.computeIfAbsent(new Triple<>(sourceTexture, width, height), p -> new DynamicTexture(sourceTexture, width, height)));
        }
        public TextureSurgeonTest setSourceTexture(DynamicTexture texture) {
            this.sourceTexture = texture;
            return this;
        }

        public TextureSurgeonTest updateRenderTarget(boolean clearBuffer) {
            return updateRenderTarget(drawToLocation, sourceTexture, clearBuffer);
        }

        public TextureSurgeonTest updateRenderTarget(ResourceLocation drawToLocation, DynamicTexture sourceTexture, boolean clearBuffer) {
            drawToTexture = DRAW_TO_TEXTURES.computeIfAbsent(new Triple<>(drawToLocation, sourceTexture.getWidth(), sourceTexture.getHeight()), p -> new DynamicTexture(drawToLocation, p.b, p.c));
            RenderTarget frameBuffer = drawToTexture.getFrameBuffer();
            if (clearBuffer) {
                frameBuffer.clear(Minecraft.ON_OSX);
            }
            frameBuffer.bindWrite(true);
            return this;
        }

        public TextureSurgeonTest begin(boolean clearBuffer) {
            updateRenderTarget(clearBuffer);

            oldProjection = RenderSystem.getProjectionMatrix();
            Matrix4f matrix4f = Matrix4f.orthographic(0.0F, 16, 0, 16, 1000.0F, ForgeHooksClient.getGuiFarPlane());
            RenderSystem.setProjectionMatrix(matrix4f);

            PoseStack posestack = RenderSystem.getModelViewStack();
            posestack.pushPose();
            posestack.setIdentity();
            posestack.translate(0.0D, 0.0D, 1000F - ForgeHooksClient.getGuiFarPlane());
            return this;
        }

        public TextureSurgeonTest operateWithShaders(VFXBuilders.ScreenVFXBuilder builder, ShaderInstance... shaders) {
            PoseStack posestack = RenderSystem.getModelViewStack();
            builder.setPositionWithWidth(0, 0, 16, 16);
            RenderSystem.enableBlend();
            return operate(() -> {
                for (int i = 0; i < shaders.length; i++) {
                    begin(i == 0);
                    ShaderInstance shader = shaders[i];
                    builder.setShaderTexture(sourceTexture.getTextureLocation()).setShader(shader).draw(posestack);
                    end(i == shaders.length - 1);
                    if (i == 0) {
                        dumpTexture(); //for some reason without this here it doesn't render at all
                        setSourceTexture(drawToTexture);
                    }
                }
            });
        }

        public TextureSurgeonTest operate(Runnable drawCall) {
            drawCall.run();
            return this;
        }

        public TextureSurgeonTest end(boolean clearBuffer) {
            Minecraft mc = Minecraft.getInstance();
            PoseStack posestack = RenderSystem.getModelViewStack();
            posestack.popPose();
            if (clearBuffer) {
                RenderSystem.applyModelViewMatrix();

                RenderSystem.setProjectionMatrix(oldProjection);
                RenderSystem.clear(256, Minecraft.ON_OSX);

                if (shouldDumpTextures) {
                    dumpTexture();
                }
                mc.getMainRenderTarget().bindWrite(true);
            }
            return this;
        }

        protected void dumpTexture() {
            try {
                Path outputFolder = Paths.get("texture_dump");
                outputFolder = Files.createDirectories(outputFolder);
                drawToTexture.saveTextureToFile(outputFolder);
            } catch (IOException e) {
                LodestoneLib.LOGGER.error("Failed to dump texture maps with error.", e);
            }
        }
    }
}