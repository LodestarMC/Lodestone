package team.lodestar.lodestone.systems.particle.data;

import team.lodestar.lodestone.systems.easing.Easing;

import java.util.Random;

public class SpinParticleData extends GenericParticleData {

    public final float spinOffset;

    protected SpinParticleData(float spinOffset, float startingValue, float middleValue, float endingValue, float coefficient, Easing startToMiddleEasing, Easing middleToEndEasing) {
        super(startingValue, middleValue, endingValue, coefficient, startToMiddleEasing, middleToEndEasing);
        this.spinOffset = spinOffset;
    }

    public SpinParticleDataBuilder scale(float scale) {
        return copy(create(startingValue * scale, middleValue * scale, endingValue < 0 ? endingValue : endingValue * scale));
    }

    public SpinParticleDataBuilder copy() {
        return copy(create(startingValue, middleValue, endingValue));
    }

    private SpinParticleDataBuilder copy(SpinParticleDataBuilder builder) {
        return builder.setSpinOffset(spinOffset).setCoefficient(coefficient).setEasing(startToMiddleEasing, middleToEndEasing);
    }

    public static SpinParticleDataBuilder create(float value) {
        return new SpinParticleDataBuilder(value, value, -1);
    }

    public static SpinParticleDataBuilder create(float startingValue, float endingValue) {
        return new SpinParticleDataBuilder(startingValue, endingValue, -1);
    }

    public static SpinParticleDataBuilder create(float startingValue, float middleValue, float endingValue) {
        return new SpinParticleDataBuilder(startingValue, middleValue, endingValue);
    }

    public static SpinParticleDataBuilder createRandomDirection(Random random, float value) {
        value *= random.nextBoolean() ? 1 : -1;
        return new SpinParticleDataBuilder(value, value, -1);
    }

    public static SpinParticleDataBuilder createRandomDirection(Random random, float startingValue, float endingValue) {
        startingValue *= random.nextBoolean() ? 1 : -1;
        endingValue *= random.nextBoolean() ? 1 : -1;
        return new SpinParticleDataBuilder(startingValue, endingValue, -1);
    }

    public static SpinParticleDataBuilder createRandomDirection(Random random, float startingValue, float middleValue, float endingValue) {
        startingValue *= random.nextBoolean() ? 1 : -1;
        endingValue *= random.nextBoolean() ? 1 : -1;
        endingValue *= random.nextBoolean() ? 1 : -1;
        return new SpinParticleDataBuilder(startingValue, middleValue, endingValue);
    }

    public static class SpinParticleDataBuilder extends GenericParticleDataBuilder {
        protected float spinOffset;

        protected SpinParticleDataBuilder(float startingValue, float middleValue, float endingValue) {
            super(startingValue, middleValue, endingValue);
        }

        public SpinParticleDataBuilder setSpinOffset(float spinOffset) {
            this.spinOffset = spinOffset;
            return this;
        }

        public SpinParticleDataBuilder randomSpinOffset(Random random) {
            this.spinOffset = random.nextFloat() * 6.28f;
            return this;
        }

        @Override
        public SpinParticleDataBuilder setCoefficient(float coefficient) {
            return (SpinParticleDataBuilder) super.setCoefficient(coefficient);
        }

        @Override
        public SpinParticleDataBuilder setEasing(Easing easing) {
            return (SpinParticleDataBuilder) super.setEasing(easing);
        }

        @Override
        public SpinParticleDataBuilder setEasing(Easing easing, Easing middleToEndEasing) {
            return (SpinParticleDataBuilder) super.setEasing(easing, middleToEndEasing);
        }

        @Override
        public SpinParticleData build() {
            return new SpinParticleData(spinOffset, startingValue, middleValue, endingValue, coefficient, startToMiddleEasing, middleToEndEasing);
        }
    }
}