package team.lodestar.lodestone.systems.particle.builder;

import team.lodestar.lodestone.registry.common.particle.*;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.world.options.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractParticleBuilder<T extends SimpleParticleOptions> {

    double xMotion = 0, yMotion = 0;
    double maxXSpeed = 0, maxYSpeed = 0;
    double maxXOffset = 0, maxYOffset = 0;


    public AbstractParticleBuilder<T> modifyData(Supplier<GenericParticleData> dataType, Consumer<GenericParticleData> dataConsumer) {
        dataConsumer.accept(dataType.get());
        return this;
    }

    public AbstractParticleBuilder<T> modifyData(Optional<GenericParticleData> dataType, Consumer<GenericParticleData> dataConsumer) {
        dataType.ifPresent(dataConsumer);
        return this;
    }

    public AbstractParticleBuilder<T> modifyData(Function<AbstractParticleBuilder<T>, GenericParticleData> dataType, Consumer<GenericParticleData> dataConsumer) {
        dataConsumer.accept(dataType.apply(this));
        return this;
    }

    public final AbstractParticleBuilder<T> modifyData(Collection<Supplier<GenericParticleData>> dataTypes, Consumer<GenericParticleData> dataConsumer) {
        for (Supplier<GenericParticleData> dataFunction : dataTypes) {
            dataConsumer.accept(dataFunction.get());
        }
        return this;
    }

    public AbstractParticleBuilder<T> modifyColorData(Consumer<ColorParticleData> dataConsumer) {
        dataConsumer.accept(getColorData());
        return this;
    }

    public AbstractParticleBuilder<T> setColorData(ColorParticleData colorData) {
        getParticleOptions().colorData = colorData;
        return this;
    }

    public ColorParticleData getColorData() {
        return getParticleOptions().colorData;
    }

    public AbstractParticleBuilder<T> setScaleData(GenericParticleData scaleData) {
        getParticleOptions().scaleData = scaleData;
        return this;
    }

    public GenericParticleData getScaleData() {
        return getParticleOptions().scaleData;
    }

    public AbstractParticleBuilder<T> setTransparencyData(GenericParticleData transparencyData) {
        getParticleOptions().transparencyData = transparencyData;
        return this;
    }

    public GenericParticleData getTransparencyData() {
        return getParticleOptions().transparencyData;
    }

    public AbstractParticleBuilder<T> setSpinData(SpinParticleData spinData) {
        getParticleOptions().spinData = spinData;
        return this;
    }

    public SpinParticleData getSpinData() {
        return getParticleOptions().spinData;
    }

    public AbstractParticleBuilder<T> multiplyGravity(float gravityMultiplier) {
        return modifyGravity(f -> () -> f * gravityMultiplier);
    }

    public AbstractParticleBuilder<T> modifyGravity(Function<Float, Supplier<Float>> gravityReplacement) {
        getParticleOptions().gravityStrengthSupplier = gravityReplacement.apply(getParticleOptions().gravityStrengthSupplier.get());
        return this;
    }

    public AbstractParticleBuilder<T> setGravityStrength(float gravity) {
        return setGravityStrength(() -> gravity);
    }

    public AbstractParticleBuilder<T> setGravityStrength(Supplier<Float> gravityStrengthSupplier) {
        getParticleOptions().gravityStrengthSupplier = gravityStrengthSupplier;
        return this;
    }

    public AbstractParticleBuilder<T> multiplyLifetime(float lifetimeMultiplier) {
        return modifyLifetime(i -> () -> (int) (i * lifetimeMultiplier));
    }

    public AbstractParticleBuilder<T> modifyLifetime(Function<Integer, Supplier<Integer>> lifetimeReplacement) {
        getParticleOptions().lifetimeSupplier = lifetimeReplacement.apply(getParticleOptions().lifetimeSupplier.get());
        return this;
    }

    public AbstractParticleBuilder<T> setLifetime(int lifetime) {
        return setLifetime(() -> lifetime);
    }

    public AbstractParticleBuilder<T> setLifetime(Supplier<Integer> lifetimeSupplier) {
        getParticleOptions().lifetimeSupplier = lifetimeSupplier;
        return this;
    }

    public AbstractParticleBuilder<T> multiplyLifeDelay(float lifeDelayMultiplier) {
        return modifyLifeDelay(i -> () -> (int) (i * lifeDelayMultiplier));
    }

    public AbstractParticleBuilder<T> modifyLifeDelay(Function<Integer, Supplier<Integer>> lifeDelayReplacement) {
        getParticleOptions().lifeDelaySupplier = lifeDelayReplacement.apply(getParticleOptions().lifeDelaySupplier.get());
        return this;
    }

    public AbstractParticleBuilder<T> setLifeDelay(int lifeDelay) {
        return setLifeDelay(() -> lifeDelay);
    }

    public AbstractParticleBuilder<T> setLifeDelay(Supplier<Integer> lifeDelaySupplier) {
        getParticleOptions().lifeDelaySupplier = lifeDelaySupplier;
        return this;
    }

    public AbstractParticleBuilder<T> setSpritePicker(SimpleParticleOptions.ParticleSpritePicker spritePicker) {
        getParticleOptions().spritePicker = spritePicker;
        return this;
    }

    public AbstractParticleBuilder<T> setDiscardFunction(SimpleParticleOptions.ParticleDiscardFunctionType discardFunctionType) {
        getParticleOptions().discardFunctionType = discardFunctionType;
        return this;
    }

    public abstract T getParticleOptions();
}