package team.lodestar.lodestone.systems.particle.builder;

import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractParticleBuilder<T extends AbstractParticleBuilder<T, Y>, Y extends SimpleParticleOptions> {

    double xMotion = 0, yMotion = 0;
    double maxXSpeed = 0, maxYSpeed = 0;
    double maxXOffset = 0, maxYOffset = 0;

    public T modifyData(Supplier<GenericParticleData> dataType, Consumer<GenericParticleData> dataConsumer) {
        dataConsumer.accept(dataType.get());
        return wrapper();
    }

    public final T modifyData(Collection<Supplier<GenericParticleData>> dataTypes, Consumer<GenericParticleData> dataConsumer) {
        for (Supplier<GenericParticleData> dataFunction : dataTypes) {
            dataConsumer.accept(dataFunction.get());
        }
        return wrapper();
    }

    public T setColorData(ColorParticleData colorData) {
        getParticleOptions().colorData = colorData;
        return wrapper();
    }

    public ColorParticleData getColorData() {
        return getParticleOptions().colorData;
    }

    public T setScaleData(GenericParticleData scaleData) {
        getParticleOptions().scaleData = scaleData;
        return wrapper();
    }

    public GenericParticleData getScaleData() {
        return getParticleOptions().scaleData;
    }

    public T setTransparencyData(GenericParticleData transparencyData) {
        getParticleOptions().transparencyData = transparencyData;
        return wrapper();
    }

    public GenericParticleData getTransparencyData() {
        return getParticleOptions().transparencyData;
    }

    public T setSpinData(SpinParticleData spinData) {
        getParticleOptions().spinData = spinData;
        return wrapper();
    }

    public SpinParticleData getSpinData() {
        return getParticleOptions().spinData;
    }

    public T multiplyGravity(float gravityMultiplier) {
        return modifyGravity(f -> () -> f * gravityMultiplier);
    }

    public T modifyGravity(Function<Float, Supplier<Float>> gravityReplacement) {
        getParticleOptions().gravityStrengthSupplier = gravityReplacement.apply(getParticleOptions().gravityStrengthSupplier.get());
        return wrapper();
    }

    public T setGravityStrength(float gravity) {
        return setGravityStrength(() -> gravity);
    }

    public T setGravityStrength(Supplier<Float> gravityStrengthSupplier) {
        getParticleOptions().gravityStrengthSupplier = gravityStrengthSupplier;
        return wrapper();
    }

    public T multiplyLifetime(float lifetimeMultiplier) {
        return modifyLifetime(i -> () -> (int) (i * lifetimeMultiplier));
    }

    public T modifyLifetime(Function<Integer, Supplier<Integer>> lifetimeReplacement) {
        getParticleOptions().lifetimeSupplier = lifetimeReplacement.apply(getParticleOptions().lifetimeSupplier.get());
        return wrapper();
    }

    public T setLifetime(int lifetime) {
        return setLifetime(() -> lifetime);
    }

    public T setLifetime(Supplier<Integer> lifetimeSupplier) {
        getParticleOptions().lifetimeSupplier = lifetimeSupplier;
        return wrapper();
    }

    public T setSpritePicker(SimpleParticleOptions.ParticleSpritePicker spritePicker) {
        getParticleOptions().spritePicker = spritePicker;
        return wrapper();
    }

    public T setDiscardFunction(SimpleParticleOptions.ParticleDiscardFunctionType discardFunctionType) {
        getParticleOptions().discardFunctionType = discardFunctionType;
        return wrapper();
    }

    public T wrapper() {
        return (T) this;
    }

    public abstract Y getParticleOptions();
}