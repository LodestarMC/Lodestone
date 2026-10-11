package team.lodestar.lodestone.modules.rendering.particle.standard;

import it.unimi.dsi.fastutil.floats.*;
import it.unimi.dsi.fastutil.ints.*;
import team.lodestar.lodestone.modules.rendering.particle.standard.data.GenericParticleData;
import team.lodestar.lodestone.modules.rendering.particle.standard.data.color.ColorParticleData;
import team.lodestar.lodestone.modules.rendering.particle.standard.data.spin.SpinParticleData;
import team.lodestar.lodestone.modules.rendering.particle.standard.renderer.*;

import java.awt.*;
import java.util.function.*;

public class LodestoneParticleConfig {

    public LodestoneParticleConfig(LodestoneParticleBody.Type type) {
        this.type = type;
    }

    public enum Animator {
        FIRST_INDEX, LAST_INDEX, WITH_AGE, WITH_AGE_INVERSE, RANDOM_SPRITE
    }

    protected final LodestoneParticleBody.Type type;

    protected Animator animator = Animator.WITH_AGE;
    protected LodestoneParticleRenderer renderer;

    protected Supplier<ColorParticleData> colorData = () -> ColorParticleData.create(Color.WHITE, Color.WHITE).build();
    protected Supplier<GenericParticleData> alphaData = () -> GenericParticleData.create(1, 0).build();
    protected Supplier<GenericParticleData> scaleData = () -> GenericParticleData.create(1, 0).build();
    protected Supplier<GenericParticleData> lengthData = () -> GenericParticleData.create(1, 0).build();
    protected Supplier<SpinParticleData> spinData = () -> SpinParticleData.create(0).build();

    protected Supplier<Integer> lifetimeSupplier = () -> 20;
    protected Supplier<Integer> delaySupplier = () -> 0;
    protected Supplier<Float> gravitySupplier = () -> 0f;
    protected Supplier<Float> frictionSupplier = () -> 1f;

    protected Int2IntFunction lifetimeModifier = i -> i;
    protected Int2IntFunction lifeDelayModifier = i -> i;
    protected Float2FloatFunction gravityModifier = f -> f;
    protected Float2FloatFunction frictionModifier = f -> f;

    public ColorParticleData getColorData() {
        return colorData.get();
    }

    public GenericParticleData getAlphaData() {
        return alphaData.get();
    }

    public GenericParticleData getScaleData() {
        return scaleData.get();
    }

    public GenericParticleData getLengthData() {
        return lengthData.get();
    }

    public SpinParticleData getSpinData() {
        return spinData.get();
    }

    public int getLifetime() {
        return lifetimeModifier.apply(lifetimeSupplier.get());
    }

    public int getLifeDelay() {
        return lifeDelayModifier.apply(delaySupplier.get());
    }

    public float getGravity() {
        return gravityModifier.apply(gravitySupplier.get());
    }

    public float getFriction() {
        return frictionModifier.apply(frictionSupplier.get());
    }
}