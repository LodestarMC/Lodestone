package team.lodestar.lodestone.systems.particle.data.spin;

import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.data.GenericParticleDataBuilder;

import java.util.Random;

public class SpinParticleDataBuilder extends GenericParticleDataBuilder {
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
