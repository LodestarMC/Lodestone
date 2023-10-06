package team.lodestar.lodestone.helpers;

import net.minecraft.util.Mth;
import team.lodestar.lodestone.systems.easing.Easing;

import java.util.Random;

public class RandomHelper {

    public static float weightedEasingLerp(Easing easing, float pDelta, float pStart, float pEnd) {
        float distanceFromMiddle = Mth.abs(0.5f - pDelta) / 0.5f;
        float middleBasedDelta = easing.ease(1 - distanceFromMiddle, 0, 1, 1);
        float pMiddle = (pStart + pEnd) / 2f;
        if (pDelta < 0.5f) {
            return Mth.lerp(middleBasedDelta, pStart, pMiddle);
        } else {
            return Mth.lerp(1 - middleBasedDelta, pMiddle, pEnd);
        }
    }

    public static float interpolateWithEasing(Easing easing, double pDelta, double pStart, double pEnd) {
        return weightedEasingLerp(easing, (float) pDelta, (float) pStart, (float) pEnd);
    }

    public static int randomBetween(Random pRandom, int min, int max) {
        return randomBetween(pRandom, Easing.SINE_IN_OUT, min, max);
    }

    public static int randomBetween(Random pRandom, Easing easing, int min, int max) {
        return Math.round(weightedEasingLerp(easing, pRandom.nextFloat(), min, max));
    }

    public static float randomBetween(Random pRandom, float min, float max) {
        return randomBetween(pRandom, Easing.SINE_IN_OUT, min, max);
    }

    public static float randomBetween(Random pRandom, Easing easing, float min, float max) {
        return weightedEasingLerp(easing, pRandom.nextFloat(), min, max);
    }

    public static double randomBetween(Random pRandom, double min, double max) {
        return randomBetween(pRandom, Easing.SINE_IN_OUT, min, max);
    }

    public static double randomBetween(Random pRandom, Easing easing, double min, double max) {
        return interpolateWithEasing(easing, pRandom.nextFloat(), min, max);
    }
}
