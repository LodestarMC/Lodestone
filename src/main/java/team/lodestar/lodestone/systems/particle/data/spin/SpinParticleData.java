package team.lodestar.lodestone.systems.particle.data.spin;

import net.minecraft.util.RandomSource;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;

import java.util.Random;

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

    public static SpinParticleDataBuilder createRandomDirection(RandomSource random, float value) {
        value *= random.nextBoolean() ? 1 : -1;
        return new SpinParticleDataBuilder(value, value, -1);
    }

    public static SpinParticleDataBuilder createRandomDirection(RandomSource random, float startingValue, float endingValue) {
        final int direction = random.nextBoolean() ? 1 : -1;
        startingValue *= direction;
        endingValue *= direction;
        return new SpinParticleDataBuilder(startingValue, endingValue, -1);
    }

    public static SpinParticleDataBuilder createRandomDirection(RandomSource random, float startingValue, float middleValue, float endingValue) {
        final int direction = random.nextBoolean() ? 1 : -1;
        startingValue *= direction;
        middleValue *= direction;
        endingValue *= direction;
        return new SpinParticleDataBuilder(startingValue, middleValue, endingValue);
    }
}