package com.sammy.ortus.handlers;

import com.sammy.ortus.config.ClientConfig;
import com.sammy.ortus.systems.screenshake.ScreenshakeInstance;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.Random;

public class ScreenshakeHandler {

    public static final ArrayList<ScreenshakeInstance> INSTANCES = new ArrayList<>();
    public static float intensity;
    public static float yawOffset;
    public static float pitchOffset;

    public static void cameraTick(Camera camera, Random random) {
        if (intensity >= 0.1) {
            yawOffset = randomizeOffset(random);
            pitchOffset = randomizeOffset(random);
            camera.setRotation(camera.getYRot() + yawOffset, camera.getXRot() + pitchOffset);
        }
    }

    public static void clientTick(Camera camera, Random random) {
        intensity = (float) (INSTANCES.stream().mapToDouble(i1 -> i1.tick(camera, random)).sum() * ClientConfig.SCREENSHAKE_INTENSITY.getValue());
        INSTANCES.removeIf(i -> i.intensity <= 0.001f);
    }

    public static void addScreenshake(ScreenshakeInstance instance) {
        INSTANCES.add(instance);
    }

    public static float randomizeOffset(Random random) {
        return Mth.nextFloat(random, -intensity * 2, intensity * 2);
    }
}
