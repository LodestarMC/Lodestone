package team.lodestar.lodestone.handlers;

import net.minecraft.client.*;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.*;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

import java.util.*;

public class ScreenshakeHandler {

    private static final ArrayList<ScreenshakeInstance> INSTANCES = new ArrayList<>();

    private static float intensity;

    public static void cameraSetup(Camera camera) {
        if (intensity >= 0.1) {
            var random = Minecraft.getInstance().level.getRandom();
            float yawOffset = randomizeOffset(random);
            float pitchOffset = randomizeOffset(random);
            camera.setRotation(camera.getYRot() + yawOffset, camera.getXRot() + pitchOffset);
        }
    }

    public static void clientTick(Camera camera) {
        var random = Minecraft.getInstance().level.getRandom();
        double sum = Math.min(INSTANCES.stream().mapToDouble(s -> s.updateIntensity(camera, random)).sum(), ClientConfig.SCREENSHAKE_INTENSITY.getConfigValue());

        intensity = (float) Math.pow(sum, 3);
        INSTANCES.removeIf(i -> i.progress >= i.duration);
    }

    public static void addScreenshake(ScreenshakeInstance instance) {
        INSTANCES.add(instance);
    }

    public static float randomizeOffset(RandomSource random) {
        return Mth.nextFloat(random, -intensity * 2, intensity * 2);
    }
}