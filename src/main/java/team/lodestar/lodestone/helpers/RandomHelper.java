package team.lodestar.lodestone.helpers;

import team.lodestar.lodestone.systems.easing.*;

import java.util.*;

public class RandomHelper {

    public static float interpolateWithEasing(Easing easing, float pDelta, float pStart, float pEnd) {
        if (pDelta < 0.5f) {
            return easing.ease(pDelta, pStart, pEnd-pStart, 0.5f);
        }
        else {
            return easing.ease(1-pDelta, pStart, pEnd-pStart, 0.5f);
        }
    }

    public static float interpolateWithEasing(Easing easing, double pDelta, double pStart, double pEnd) {
        return interpolateWithEasing(easing, (float)pDelta, (float)pStart, (float)pEnd);
    }

    public static int randomBetween(Random pRandom, int min, int max) {
        return randomBetween(pRandom, Easing.SINE_IN_OUT, min, max);
    }

    public static int randomBetween(Random pRandom, Easing easing, int min, int max) {
        return Math.round(interpolateWithEasing(easing, pRandom.nextFloat(), min, max));
    }

    public static float randomBetween(Random pRandom, float min, float max) {
        return randomBetween(pRandom, Easing.SINE_IN_OUT, min, max);
    }

    public static float randomBetween(Random pRandom, Easing easing, float min, float max) {
        return interpolateWithEasing(easing, pRandom.nextFloat(), min, max);
    }

    public static double randomBetween(Random pRandom, double min, double max) {
        return randomBetween(pRandom, Easing.SINE_IN_OUT, min, max);
    }

    public static double randomBetween(Random pRandom, Easing easing, double min, double max) {
        return interpolateWithEasing(easing, pRandom.nextFloat(), min, max);
    }
}
