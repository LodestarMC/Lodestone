package team.lodestar.lodestone.systems.particle.data;

import team.lodestar.lodestone.systems.easing.*;

public class GenericParticleDataBuilder {
    protected float startingValue, middleValue, endingValue;
    protected float coefficient = 1f;
    protected Easing startToMiddleEasing = Easing.LINEAR, middleToEndEasing = Easing.LINEAR;

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
