package team.lodestar.lodestone.handlers;

import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;
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
            //TODO: make this perlin noise based rather than just random gibberish
            yawOffset = randomizeOffset(random);
            pitchOffset = randomizeOffset(random);
            camera.setRotation(camera.getYRot() + yawOffset, camera.getXRot() + pitchOffset);
        }
    }

    public static void clientTick(Camera camera, Random random) {
        double sum = Math.min(INSTANCES.stream().mapToDouble(i1 -> i1.updateIntensity(camera, random)).sum(), ClientConfig.SCREENSHAKE_INTENSITY.getConfigValue());

        intensity = (float) Math.pow(sum, 3);
        INSTANCES.removeIf(i -> i.progress >= i.duration);
    }

    public static void addScreenshake(ScreenshakeInstance instance) {
        INSTANCES.add(instance);
    }

    public static float randomizeOffset(Random random) {
        return Mth.nextFloat(random, -intensity * 2, intensity * 2);
    }
}