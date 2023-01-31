package team.lodestar.lodestone.systems.particle.data;

import team.lodestar.lodestone.systems.easing.Easing;

public class SpinParticleData extends GenericParticleData {

    public final float spinOffset;

    public SpinParticleData(float spinOffset, float startingValue, float middleValue, float endingValue, float coefficient, Easing startToMiddleEasing, Easing middleToEndEasing) {
        super(startingValue, middleValue, endingValue, coefficient, startToMiddleEasing, middleToEndEasing);
        this.spinOffset = spinOffset;
    }

    public SpinParticleData(float spinOffset, float startingValue, float middleValue, float coefficient, Easing startToMiddleEasing) {
        super(startingValue, middleValue, coefficient, startToMiddleEasing);
        this.spinOffset = spinOffset;
    }

    public SpinParticleData(float spinOffset, float startingValue, float middleValue, float coefficient) {
        super(startingValue, middleValue, coefficient);
        this.spinOffset = spinOffset;
    }

    public SpinParticleData(float spinOffset, float startingValue, float middleValue) {
        super(startingValue, middleValue);
        this.spinOffset = spinOffset;
    }

    public SpinParticleData(float startingValue, float middleValue) {
        this(0, startingValue, middleValue);
    }
}
