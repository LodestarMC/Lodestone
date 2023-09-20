package team.lodestar.lodestone.helpers;

import team.lodestar.lodestone.systems.easing.*;

import java.util.*;

public class RandomHelper {

    public static float interpolateWithEasing(Easing easing, float pDelta, float pStart, float pEnd) {
        if (pDelta < 0.5f) {
            return easing.ease(pDelta*2f, pStart, pEnd, 1);
        }
        else {
            return easing.ease(1 - (pDelta-0.5f)*2f, pStart, pEnd, 1);
        }
    }

    public static float interpolateWithEasing(Easing easing, double pDelta, double pStart, double pEnd) {
        return interpolateWithEasing(easing, (float)pDelta, (float)pStart, (float)pEnd);
    }

    public static float randomBetween(Random pRandom, Easing easing, float min, float max) {
        return interpolateWithEasing(easing, pRandom.nextFloat(), min, max);
    }

    public static float randomBetween(Random pRandom, float min, float max) {
        return interpolateWithEasing(Easing.SINE_IN_OUT, pRandom.nextFloat(), min, max);
    }

    public static float randomBetween(Random pRandom, Easing easing, double min, double max) {
        return interpolateWithEasing(easing, pRandom.nextFloat(), min, max);
    }

    public static float randomBetween(Random pRandom, double min, double max) {
        return interpolateWithEasing(Easing.SINE_IN_OUT, pRandom.nextFloat(), min, max);
    }
}
