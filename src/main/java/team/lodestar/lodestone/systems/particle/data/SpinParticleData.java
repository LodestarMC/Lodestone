package team.lodestar.lodestone.systems.particle.data;

import team.lodestar.lodestone.systems.easing.Easing;

public class SpinParticleData extends GenericParticleData {

    public final float spinOffset;

    protected SpinParticleData(float spinOffset, float startingValue, float middleValue, float endingValue, float coefficient, Easing startToMiddleEasing, Easing middleToEndEasing) {
        super(startingValue, middleValue, endingValue, coefficient, startToMiddleEasing, middleToEndEasing);
        this.spinOffset = spinOffset;
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

    public static class SpinParticleDataBuilder extends GenericParticleDataBuilder {
        protected float spinOffset;

        protected SpinParticleDataBuilder(float startingValue, float middleValue, float endingValue) {
            super(startingValue, middleValue, endingValue);
        }

        public SpinParticleDataBuilder setSpinOffset(float spinOffset) {
            this.spinOffset = spinOffset;
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