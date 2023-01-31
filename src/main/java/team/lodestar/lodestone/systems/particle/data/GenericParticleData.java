package team.lodestar.lodestone.systems.particle.data;

import net.minecraft.util.Mth;
import team.lodestar.lodestone.systems.easing.Easing;

public class GenericParticleData {
    public final float startingValue, middleValue, endingValue;
    public final float coefficient;
    public final Easing startToMiddleEasing, middleToEndEasing;

    public GenericParticleData(float startingValue, float middleValue, float endingValue, float coefficient, Easing startToMiddleEasing, Easing middleToEndEasing) {
        this.startingValue = startingValue;
        this.middleValue = middleValue;
        this.endingValue = endingValue;
        this.coefficient = coefficient;
        this.startToMiddleEasing = startToMiddleEasing;
        this.middleToEndEasing = middleToEndEasing;
    }

    public GenericParticleData(float startingValue, float middleValue, float coefficient, Easing startToMiddleEasing) {
        this(startingValue, middleValue, -1, coefficient, startToMiddleEasing, null);
    }

    public GenericParticleData(float startingValue, float middleValue, float coefficient) {
        this(startingValue, middleValue, coefficient, Easing.LINEAR);
    }

    public GenericParticleData(float startingValue, float middleValue) {
        this(startingValue, middleValue, 1f);
    }

    public boolean isTrinary() {
        return endingValue != -1;
    }

    public float getProgress(float age, float lifetime) {
        return Mth.clamp((age * coefficient) / lifetime, 0, 1);
    }

    public float getValue(float age, float lifetime) {
        float progress = getProgress(age, lifetime);
        if (isTrinary()) {
            if (progress >= 0.5f) {
                return Mth.lerp(middleToEndEasing.ease(progress - 0.5f, 0, 1, 0.5f), middleValue, endingValue);
            } else {
                return Mth.lerp(startToMiddleEasing.ease(progress, 0, 1, 0.5f), startingValue, middleValue);
            }
        } else {
            return Mth.lerp(startToMiddleEasing.ease(progress, 0, 1, 1), startingValue, middleValue);
        }
    }
}