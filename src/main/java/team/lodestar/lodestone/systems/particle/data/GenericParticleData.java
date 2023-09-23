package team.lodestar.lodestone.systems.particle.data;

import net.minecraft.util.Mth;
import team.lodestar.lodestone.systems.easing.Easing;

public class GenericParticleData {
    public final float startingValue, middleValue, endingValue;
    public final float coefficient;
    public final Easing startToMiddleEasing, middleToEndEasing;

    public float valueMultiplier = 1;
    public float coefficientMultiplier = 1;

    protected GenericParticleData(float startingValue, float middleValue, float endingValue, float coefficient, Easing startToMiddleEasing, Easing middleToEndEasing) {
        this.startingValue = startingValue;
        this.middleValue = middleValue;
        this.endingValue = endingValue;
        this.coefficient = coefficient;
        this.startToMiddleEasing = startToMiddleEasing;
        this.middleToEndEasing = middleToEndEasing;
    }

    public void multiplyCoefficient(float coefficientMultiplier) {
        this.coefficientMultiplier = coefficientMultiplier;
    }

    public void multiplyValue(float valueMultiplier) {
        this.valueMultiplier = valueMultiplier;
    }

    public boolean isTrinary() {
        return endingValue != -1;
    }

    public float getProgress(float age, float lifetime) {
        return Mth.clamp((age * coefficient * coefficientMultiplier) / lifetime, 0, 1);
    }

    public float getValue(float age, float lifetime) {
        float progress = getProgress(age, lifetime);
        float result;
        if (isTrinary()) {
            if (progress >= 0.5f) {
                result = Mth.lerp(middleToEndEasing.ease(progress - 0.5f, 0, 1, 0.5f), middleValue, endingValue);
            } else {
                result = Mth.lerp(startToMiddleEasing.ease(progress, 0, 1, 0.5f), startingValue, middleValue);
            }
        } else {
            result = Mth.lerp(startToMiddleEasing.ease(progress, 0, 1, 1), startingValue, middleValue);
        }
        return result * valueMultiplier;
    }

    public static GenericParticleDataBuilder create(float value) {
        return new GenericParticleDataBuilder(value, value, -1);
    }

    public static GenericParticleDataBuilder create(float startingValue, float endingValue) {
        return new GenericParticleDataBuilder(startingValue, endingValue, -1);
    }

    public static GenericParticleDataBuilder create(float startingValue, float middleValue, float endingValue) {
        return new GenericParticleDataBuilder(startingValue, middleValue, endingValue);
    }
}