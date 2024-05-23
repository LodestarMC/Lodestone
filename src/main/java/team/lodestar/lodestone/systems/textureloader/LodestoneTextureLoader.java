package team.lodestar.lodestone.systems.textureloader;
/*TODO
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import team.lodestar.lodestone.helpers.ColorHelper;
import team.lodestar.lodestone.systems.easing.Easing;

import java.awt.*;


public class LodestoneTextureLoader {


    protected static final ColorLerp GRADIENT = (image, x, y, luminosity, s) -> ((y % 16) / 16f);
    protected static final ColorLerp LUMINOUS_GRADIENT = (image, x, y, luminosity, s) -> (((y % 16) / 16f) + luminosity / s) / 2f;
    protected static final ColorLerp LUMINOUS = (image, x, y, luminosity, s) -> luminosity / s;

    public static void registerTextureLoader(String loaderName, ResourceLocation targetPath, ResourceLocation inputImage, TextureModifier textureModifier, RegisterTextureAtlasSpriteLoadersEvent event) {
        IEventBus busMod = FMLJavaModLoadingContext.get().getModEventBus();
        event.register(loaderName, new LodestoneTextureAtlasSpriteLoader(textureModifier));
    }

    public static NativeImage applyGrayscale(NativeImage nativeimage) {
        for (int x = 0; x < nativeimage.getWidth(); x++) {
            for (int y = 0; y < nativeimage.getHeight(); y++) {
                int pixel = nativeimage.getPixelRGBA(x, y);
                int L = (int) (0.299D * ((pixel) & 0xFF) + 0.587D * ((pixel >> 8) & 0xFF) + 0.114D * ((pixel >> 16) & 0xFF));
                nativeimage.setPixelRGBA(x, y, FastColor.ABGR32.color((pixel >> 24) & 0xFF, L, L, L));
            }
        }
        return nativeimage;
    }

    public static NativeImage applyMultiColorGradient(Easing easing, NativeImage nativeimage, ColorLerp colorLerp, Color... colors) {
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
        for (int x = 0; x < nativeimage.getWidth(); x++) {
            for (int y = 0; y < nativeimage.getHeight(); y++) {
                int pixel = nativeimage.getPixelRGBA(x, y);
                int alpha = (pixel >> 24) & 0xFF;
                if (alpha == 0) {
                    continue;
                }
                int luminosity = (int) (0.299D * ((pixel) & 0xFF) + 0.587D * ((pixel >> 8) & 0xFF) + 0.114D * ((pixel >> 16) & 0xFF));
                float pct = luminosity / 255f; //this should probably be smth else
                float newLuminosity = Mth.lerp(pct, lowestLuminosity, highestLuminosity);
                float lerp = 1 - colorLerp.lerp(pixel, x, y, newLuminosity, highestLuminosity);
                float colorIndex = 2 * colorCount * lerp; //TODO: figure out why this * 2 is here

                int index = (int) Mth.clamp(colorIndex, 0, colorCount);
                Color color = colors[index];
                Color nextColor = index == colorCount ? color : colors[index + 1];
                Color transition = ColorHelper.colorLerp(easing, colorIndex - (int) (colorIndex), color, nextColor);
                nativeimage.setPixelRGBA(x, y, FastColor.ABGR32.color(alpha, transition.getBlue(), transition.getGreen(), transition.getRed()));
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

 */