package team.lodestar.lodestone.helpers;

import team.lodestar.lodestone.systems.easing.*;

import java.util.*;

public class RandomHelper {

    public static int randomBetween(Random pRandom, int min, int max) {
        return randomBetween(pRandom, Easing.SINE_IN_OUT, min, max);
    }

    public static int randomBetween(Random pRandom, Easing easing, int min, int max) {
        return Math.round(EasingHelper.weightedEasingLerp(easing, pRandom.nextFloat(), min, max));
    }

    public static float randomBetween(Random pRandom, float min, float max) {
        return randomBetween(pRandom, Easing.SINE_IN_OUT, min, max);
    }

    public static float randomBetween(Random pRandom, Easing easing, float min, float max) {
        return EasingHelper.weightedEasingLerp(easing, pRandom.nextFloat(), min, max);
    }

    public static double randomBetween(Random pRandom, double min, double max) {
        return randomBetween(pRandom, Easing.SINE_IN_OUT, min, max);
    }

    public static double randomBetween(Random pRandom, Easing easing, double min, double max) {
        return EasingHelper.weightedEasingLerp(easing, pRandom.nextFloat(), min, max);
    }
}
