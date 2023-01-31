package team.lodestar.lodestone.systems.particle.data;

import net.minecraft.util.Mth;
import team.lodestar.lodestone.systems.easing.Easing;

public class GenericParticleData {
    public final float startingValue, middleValue, endingValue;
    public final float coefficient;
    public final Easing startToMiddleEasing, middleToEndEasing;

    protected GenericParticleData(float startingValue, float middleValue, float endingValue, float coefficient, Easing startToMiddleEasing, Easing middleToEndEasing) {
        this.startingValue = startingValue;
        this.middleValue = middleValue;
        this.endingValue = endingValue;
        this.coefficient = coefficient;
        this.startToMiddleEasing = startToMiddleEasing;
        this.middleToEndEasing = middleToEndEasing;
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

    public static GenericParticleDataBuilder create(float value) {
        return new GenericParticleDataBuilder(value, value, -1);
    }

    public static GenericParticleDataBuilder create(float startingValue, float endingValue) {
        return new GenericParticleDataBuilder(startingValue, endingValue, -1);
    }

    public static GenericParticleDataBuilder create(float startingValue, float middleValue, float endingValue) {
        return new GenericParticleDataBuilder(startingValue, middleValue, endingValue);
    }

    public static class GenericParticleDataBuilder {
        protected final float startingValue, middleValue, endingValue;
        protected float coefficient = 1f;
        protected Easing startToMiddleEasing = Easing.LINEAR, middleToEndEasing = null;

        protected GenericParticleDataBuilder(float startingValue, float middleValue, float endingValue) {
            this.startingValue = startingValue;
            this.middleValue = middleValue;
            this.endingValue = endingValue;
        }

        public GenericParticleDataBuilder setCoefficient(float coefficient) {
            this.coefficient = coefficient;
            return this;
        }

        public GenericParticleDataBuilder setEasing(Easing easing) {
            this.startToMiddleEasing = easing;
            return this;
        }

        public GenericParticleDataBuilder setEasing(Easing easing, Easing middleToEndEasing) {
            this.startToMiddleEasing = easing;
            this.middleToEndEasing = easing;
            return this;
        }


        public GenericParticleData build() {
            return new GenericParticleData(startingValue, middleValue, endingValue, coefficient, startToMiddleEasing, middleToEndEasing);
        }
    }
}