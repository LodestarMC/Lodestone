package team.lodestar.lodestone.helpers;

import net.minecraft.util.*;
import team.lodestar.lodestone.systems.easing.*;

public class EasingHelper {
    public static float weightedEasingLerp(Easing easing, float pDelta, float pStart, float pMiddle, float pEnd) {
        float distanceFromMiddle = Mth.abs(0.5f - pDelta) / 0.5f;
        float middleBasedDelta = easing.ease(1 - distanceFromMiddle, 0, 1, 1);
        if (pDelta < 0.5f) {
            return Mth.lerp(middleBasedDelta, pStart, pMiddle);
        }
        else {
            return Mth.lerp(1-middleBasedDelta, pMiddle, pEnd);
        }
    }
    public static float weightedEasingLerp(Easing easing, float pDelta, float pStart, float pEnd) {
        return weightedEasingLerp(easing, pDelta, pStart, (pStart + pEnd) / 2f, pEnd);
    }

    public static float weightedEasingLerp(Easing easing, double pDelta, double pStart, double pEnd) {
        return weightedEasingLerp(easing, (float)pDelta, (float)pStart, (float)pEnd);
    }
}
