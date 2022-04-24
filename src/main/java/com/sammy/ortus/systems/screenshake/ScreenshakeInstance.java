package com.sammy.ortus.systems.screenshake;

import net.minecraft.client.Camera;
import net.minecraft.util.Mth;

import java.util.Random;

public class ScreenshakeInstance {
    public int time;
    public float currentFalloff;

    public float intensity;
    public float falloffTransformSpeed;
    public int timeBeforeFastFalloff;
    public float slowFalloff;
    public float fastFalloff;
    public ScreenshakeInstance(float intensity, float falloffTransformSpeed, int timeBeforeFastFalloff, float slowFalloff, float fastFalloff) {
        this.intensity = intensity;
        this.falloffTransformSpeed = falloffTransformSpeed;
        this.timeBeforeFastFalloff = timeBeforeFastFalloff;
        this.slowFalloff = slowFalloff;
        this.fastFalloff = fastFalloff;
    }

    public float tick(Camera camera, Random random)
    {
        time++;
        currentFalloff = Mth.lerp(falloffTransformSpeed, currentFalloff, time >= timeBeforeFastFalloff ? fastFalloff : slowFalloff);
        return updateIntensity(camera, currentFalloff);
    }
    public float updateIntensity(Camera camera, float falloff)
    {
        return intensity=intensity-(intensity*falloff);
    }
}
