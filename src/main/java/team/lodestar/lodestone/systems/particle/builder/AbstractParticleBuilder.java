package team.lodestar.lodestone.systems.particle.builder;

import it.unimi.dsi.fastutil.floats.*;
import it.unimi.dsi.fastutil.ints.*;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.data.color.*;
import team.lodestar.lodestone.systems.particle.data.spin.*;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.data.color.*;
import team.lodestar.lodestone.systems.particle.data.spin.*;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractParticleBuilder<T extends SimpleParticleOptions> {

    double xMotion = 0, yMotion = 0;
    double maxXSpeed = 0, maxYSpeed = 0;
    double maxXOffset = 0, maxYOffset = 0;

    public AbstractParticleBuilder<T> modifyColorData(Consumer<ColorParticleData> dataConsumer) {
        dataConsumer.accept(getColorData());
        return this;
    }

    public AbstractParticleBuilder<T> setColorData(ColorParticleDataWrapper colorData) {
        getParticleOptions().colorData = colorData.unwrap();
        return this;
    }

    public ColorParticleData getColorData() {
        return getParticleOptions().colorData;
    }

    public AbstractParticleBuilder<T> modifyScaleData(Consumer<GenericParticleData> dataConsumer) {
        dataConsumer.accept(getScaleData());
        return this;
    }

    public AbstractParticleBuilder<T> setScaleData(GenericParticleDataWrapper scaleData) {
        getParticleOptions().scaleData = scaleData.unwrap();
        return this;
    }

    public GenericParticleData getScaleData() {
        return getParticleOptions().scaleData;
    }

    public AbstractParticleBuilder<T> modifyLengthData(Consumer<GenericParticleData> dataConsumer) {
        dataConsumer.accept(getLengthData());
        return this;
    }

    public AbstractParticleBuilder<T> setLengthData(GenericParticleDataWrapper lengthData) {
        getParticleOptions().lengthData = lengthData.unwrap();
        return this;
    }

    public GenericParticleData getLengthData() {
        return getParticleOptions().lengthData;
    }

    public AbstractParticleBuilder<T> modifyTransparencyData(Consumer<GenericParticleData> dataConsumer) {
        dataConsumer.accept(getTransparencyData());
        return this;
    }

    public AbstractParticleBuilder<T> setTransparencyData(GenericParticleDataWrapper transparencyData) {
        getParticleOptions().transparencyData = transparencyData.unwrap();
        return this;
    }

    public GenericParticleData getTransparencyData() {
        return getParticleOptions().transparencyData;
    }

    public AbstractParticleBuilder<T> modifySpinData(Consumer<SpinParticleData> dataConsumer) {
        dataConsumer.accept(getSpinData());
        return this;
    }

    public AbstractParticleBuilder<T> setSpinData(SpinParticleDataWrapper spinData) {
        getParticleOptions().spinData = spinData.unwrap();
        return this;
    }

    public SpinParticleData getSpinData() {
        return getParticleOptions().spinData;
    }

    public AbstractParticleBuilder<T> setLifeDelay(int lifeDelay) {
        return setLifeDelay(() -> lifeDelay);
    }

    public AbstractParticleBuilder<T> setLifeDelay(Supplier<Integer> supplier) {
        getParticleOptions().lifeDelaySupplier = supplier;
        return this;
    }

    public AbstractParticleBuilder<T> setLifetime(int lifetime) {
        return setLifetime(() -> lifetime);
    }

    public AbstractParticleBuilder<T> setLifetime(Supplier<Integer> supplier) {
        getParticleOptions().lifetimeSupplier = supplier;
        return this;
    }

    public AbstractParticleBuilder<T> setGravity(float gravity) {
        return setGravity(() -> gravity);
    }

    public AbstractParticleBuilder<T> setGravity(Supplier<Float> supplier) {
        getParticleOptions().gravitySupplier = supplier;
        return this;
    }

    public AbstractParticleBuilder<T> setFriction(float friction) {
        return setFriction(() -> friction);
    }

    public AbstractParticleBuilder<T> setFriction(Supplier<Float> supplier) {
        getParticleOptions().frictionSupplier = supplier;
        return this;
    }

    public AbstractParticleBuilder<T> multiplyLifeDelay(float multiplier) {
        return modifyLifeDelay(f -> (int) (f * multiplier));
    }

    public AbstractParticleBuilder<T> modifyLifeDelay(Int2IntFunction modifier) {
        return setLifeDelayModifier(getParticleOptions().lifeDelayModifier.andThenInt(modifier));
    }

    public AbstractParticleBuilder<T> setLifeDelayModifier(float multiplier) {
        return setLifeDelayModifier(f -> (int) (f * multiplier));
    }

    public AbstractParticleBuilder<T> setLifeDelayModifier(Int2IntFunction modifier) {
        getParticleOptions().lifeDelayModifier = modifier;
        return this;
    }

    public AbstractParticleBuilder<T> multiplyLifetime(float multiplier) {
        return modifyLifetime(f -> (int) (f * multiplier));
    }

    public AbstractParticleBuilder<T> modifyLifetime(Int2IntFunction modifier) {
        return setLifetimeModifier(getParticleOptions().lifetimeModifier.andThenInt(modifier));
    }

    public AbstractParticleBuilder<T> setLifetimeModifier(float multiplier) {
        return setLifetimeModifier(f -> (int) (f * multiplier));
    }

    public AbstractParticleBuilder<T> setLifetimeModifier(Int2IntFunction modifier) {
        getParticleOptions().lifetimeModifier = modifier;
        return this;
    }

    public AbstractParticleBuilder<T> multiplyGravity(float multiplier) {
        return modifyGravity(f -> f * multiplier);
    }

    public AbstractParticleBuilder<T> modifyGravity(Float2FloatFunction modifier) {
        return setGravityModifier(getParticleOptions().gravityModifier.andThenFloat(modifier));
    }

    public AbstractParticleBuilder<T> setGravityModifier(float multiplier) {
        return setGravityModifier(f -> (int) (f * multiplier));
    }

    public AbstractParticleBuilder<T> setGravityModifier(Float2FloatFunction modifier) {
        getParticleOptions().gravityModifier = modifier;
        return this;
    }

    public AbstractParticleBuilder<T> multiplyFriction(float multiplier) {
        return modifyFriction(f -> f * multiplier);
    }

    public AbstractParticleBuilder<T> modifyFriction(Float2FloatFunction modifier) {
        return setFrictionModifier(getParticleOptions().frictionModifier.andThenFloat(modifier));
    }

    public AbstractParticleBuilder<T> setFrictionModifier(float multiplier) {
        return setFrictionModifier(f -> (int) (f * multiplier));
    }
    
    public AbstractParticleBuilder<T> setFrictionModifier(Float2FloatFunction modifier) {
        getParticleOptions().frictionModifier = modifier;
        return this;
    }

    public AbstractParticleBuilder<T> setSpritePicker(SimpleParticleOptions.ParticleSpritePicker spritePicker) {
        getParticleOptions().spritePicker = spritePicker;
        return this;
    }

    public abstract T getParticleOptions();
}