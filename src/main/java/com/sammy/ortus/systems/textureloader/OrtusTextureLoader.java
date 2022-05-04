package com.sammy.ortus.systems.textureloader;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.datafixers.util.Pair;
import com.sammy.ortus.helpers.ColorHelper;
import com.sammy.ortus.systems.easing.Easing;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.Mth;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;

public class OrtusTextureLoader {

    protected static final ColorLerp GRADIENT = (image, x, y, luminosity, s) -> ((y % 16) / 16f);
    protected static final ColorLerp LUMINOUS_GRADIENT = (image, x, y, luminosity, s) -> (((y % 16) / 16f) + luminosity / s) / 2f;
    protected static final ColorLerp LUMINOUS = (image, x, y, luminosity, s) -> luminosity / s;

    public static void copyTextureWithChanges(ResourceLocation loaderName, ResourceLocation targetPath, ResourceLocation sourcePath, TextureModifier modifier) {
        IEventBus busMod = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForgeClient.registerTextureAtlasSpriteLoader(loaderName, (atlas, resourceManager, textureInfo, resource, atlasWidth, atlasHeight, spriteX, spriteY, mipmapLevel, image) -> {
            Resource r = null;
            try {
                r = resourceManager.getResource(sourcePath);
                image = modifier.modifyTexture(NativeImage.read(r.getInputStream()));
            } catch (Throwable throwable1) {
                if (r != null) {
                    try {
                        r.close();
                    } catch (Throwable throwable) {
                        throwable1.addSuppressed(throwable);
                    }
                }

                throwable1.printStackTrace();
            }
            TextureAtlasSprite.Info info = null;
            if (r != null) {
                try {
                    AnimationMetadataSection section = r.getMetadata(AnimationMetadataSection.SERIALIZER);
                    if (section == null) {
                        section = AnimationMetadataSection.EMPTY;
                    }
                    Pair<Integer, Integer> pair = section.getFrameSize(image.getWidth(), image.getHeight());
                    info = new TextureAtlasSprite.Info(textureInfo.name(), pair.getFirst(), pair.getSecond(), section);
                    r.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return new TextureAtlasSprite(atlas, info == null ? textureInfo : info, mipmapLevel, atlasWidth, atlasHeight, spriteX, spriteY, image) {
            };
        });
        busMod.addListener((Consumer<TextureStitchEvent.Pre>) event -> event.addSprite(targetPath));
    }

    public static NativeImage grayscale(NativeImage nativeimage) {
        for (int x = 0; x < nativeimage.getWidth(); x++) {
            for (int y = 0; y < nativeimage.getHeight(); y++) {
                int pixel = nativeimage.getPixelRGBA(x, y);
                int L = (int) (0.299D * ((pixel) & 0xFF) + 0.587D * ((pixel >> 8) & 0xFF) + 0.114D * ((pixel >> 16) & 0xFF));
                nativeimage.setPixelRGBA(x, y, NativeImage.combine((pixel >> 24) & 0xFF, L, L, L));
            }
        }
        return nativeimage;
    }

    public static NativeImage multiColorGradient(Easing easing, NativeImage nativeimage, ColorLerp colorLerp, Color... colors) {
        int colorCount = colors.length - 1;
        int lowestLuminosity = 255;
        int highestLuminosity = 0;
        for (int x = 0; x < nativeimage.getWidth(); x++) {
            for (int y = 0; y < nativeimage.getHeight(); y++) {
                int pixel = nativeimage.getPixelRGBA(x, y);
                int alpha = (pixel >> 24) & 0xFF;
                if (alpha == 0) {
                    continue;
                }
                int luminosity = (int) (0.299D * ((pixel) & 0xFF) + 0.587D * ((pixel >> 8) & 0xFF) + 0.114D * ((pixel >> 16) & 0xFF));
                if (luminosity < lowestLuminosity) {
                    lowestLuminosity = luminosity;
                }
                if (luminosity > highestLuminosity) {
                    highestLuminosity = luminosity;
                }
            }
        }
        int scale = highestLuminosity - lowestLuminosity;
        for (int x = 0; x < nativeimage.getWidth(); x++) {
            for (int y = 0; y < nativeimage.getHeight(); y++) {
                int pixel = nativeimage.getPixelRGBA(x, y);
                int alpha = (pixel >> 24) & 0xFF;
                if (alpha == 0) {
                    continue;
                }
                int luminosity = (int) (0.299D * ((pixel) & 0xFF) + 0.587D * ((pixel >> 8) & 0xFF) + 0.114D * ((pixel >> 16) & 0xFF));

                float pct = luminosity/255f;
                int newLuminosity = (int) Mth.lerp(pct, lowestLuminosity, highestLuminosity);
                float lerp = 1 - colorLerp.lerp(pixel, x, y, newLuminosity, scale);
                float colorIndex = colorCount * lerp;

                int index = (int) Mth.clamp(colorIndex, 0, colorCount);
                Color color = colors[index];
                Color nextColor = index == colorCount ? color : colors[index + 1];
                Color transition = ColorHelper.colorLerp(easing, colorIndex - (int) (colorIndex), color, nextColor);
                nativeimage.setPixelRGBA(x, y, NativeImage.combine(alpha, transition.getBlue(), transition.getGreen(), transition.getRed()));
            }
        }
        return nativeimage;
    }

    public interface ColorLerp {
        float lerp(int pixel, int x, int y, float luminosity, float luminosityScale);
    }

    public interface TextureModifier {
        NativeImage modifyTexture(NativeImage nativeImage);
    }
}