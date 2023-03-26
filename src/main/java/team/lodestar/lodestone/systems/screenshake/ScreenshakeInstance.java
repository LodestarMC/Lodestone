package team.lodestar.lodestone.systems.screenshake;

import net.minecraft.util.RandomSource;
import team.lodestar.lodestone.systems.easing.Easing;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;

import java.util.Random;

public class ScreenshakeInstance {
    public int progress;
    public final int duration;
    public float intensity1, intensity2, intensity3;
    public Easing intensityCurveStartEasing = Easing.LINEAR, intensityCurveEndEasing = Easing.LINEAR;

    public ScreenshakeInstance(int duration) {
        this.duration = duration;
    }

    public ScreenshakeInstance setIntensity(float intensity) {
        return setIntensity(intensity, intensity);
    }

    public ScreenshakeInstance setIntensity(float intensity1, float intensity2) {
        return setIntensity(intensity1, intensity2, intensity2);
    }

    public ScreenshakeInstance setIntensity(float intensity1, float intensity2, float intensity3) {
        this.intensity1 = intensity1;
        this.intensity2 = intensity2;
        this.intensity3 = intensity3;
        return this;
    }

    public ScreenshakeInstance setEasing(Easing easing) {
        return setEasing(easing, easing);
    }

    public ScreenshakeInstance setEasing(Easing intensityCurveStartEasing, Easing intensityCurveEndEasing) {
        this.intensityCurveStartEasing = intensityCurveStartEasing;
        this.intensityCurveEndEasing = intensityCurveEndEasing;
        return this;
    }

    public float updateIntensity(Camera camera, RandomSource random) {
        progress++;
        float percentage = progress / (float) duration;
        if (intensity2 != intensity3) {
            if (percentage >= 0.5f) {
                return Mth.lerp(intensityCurveEndEasing.ease(percentage - 0.5f, 0, 1, 0.5f), intensity2, intensity1);
            } else {
                return Mth.lerp(intensityCurveStartEasing.ease(percentage, 0, 1, 0.5f), intensity1, intensity2);
            }
        } else {
            return Mth.lerp(intensityCurveStartEasing.ease(percentage, 0, 1, 1), intensity1, intensity2);
        }
    }
}