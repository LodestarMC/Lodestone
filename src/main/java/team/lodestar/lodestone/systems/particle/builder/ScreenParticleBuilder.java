package team.lodestar.lodestone.systems.particle.builder;

import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.data.color.*;
import team.lodestar.lodestone.systems.particle.data.spin.*;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleOptions;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneScreenParticleRenderType;
import team.lodestar.lodestone.systems.particle.screen.GenericScreenParticle;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleType;

import java.util.Random;
import java.util.function.*;

public class ScreenParticleBuilder extends AbstractParticleBuilder<ScreenParticleOptions> {

    private static final Random RANDOM = new Random();

    final ScreenParticleType<?> type;
    final ScreenParticleOptions options;
    final ScreenParticleHolder target;

    public static ScreenParticleBuilder create(ScreenParticleType<?> type, ScreenParticleHolder target) {
        return new ScreenParticleBuilder(type, target);
    }

    protected ScreenParticleBuilder(ScreenParticleType<?> type, ScreenParticleHolder target) {
        this.type = type;
        this.options = new ScreenParticleOptions(type);
        this.target = target;
    }

    @Override
    public ScreenParticleOptions getParticleOptions() {
        return options;
    }

    public ScreenParticleBuilder setDiscardFunction(SimpleParticleOptions.ParticleDiscardFunctionType discardFunctionType) {
        options.discardFunctionType = discardFunctionType;
        return this;
    }

    public ScreenParticleBuilder setSpritePicker(SimpleParticleOptions.ParticleSpritePicker spritePicker) {
        options.spritePicker = spritePicker;
        return this;
    }

    public ScreenParticleBuilder setRenderType(LodestoneScreenParticleRenderType renderType) {
        options.renderType = renderType;
        return this;
    }

    public ScreenParticleBuilder setRandomMotion(double maxSpeed) {
        return setRandomMotion(maxSpeed, maxSpeed);
    }

    public ScreenParticleBuilder setRandomMotion(double maxXSpeed, double maxYSpeed) {
        this.maxXSpeed = maxXSpeed;
        this.maxYSpeed = maxYSpeed;
        return this;
    }

    public ScreenParticleBuilder addMotion(double vx, double vy) {
        this.xMotion += vx;
        this.yMotion += vy;
        return this;
    }

    public ScreenParticleBuilder setMotion(double vx, double vy) {
        this.xMotion = vx;
        this.yMotion = vy;
        return this;
    }

    public ScreenParticleBuilder setRandomOffset(double maxDistance) {
        return setRandomOffset(maxDistance, maxDistance);
    }

    public ScreenParticleBuilder setRandomOffset(double maxXDist, double maxYDist) {
        this.maxXOffset = maxXDist;
        this.maxYOffset = maxYDist;
        return this;
    }

    public ScreenParticleBuilder act(Consumer<ScreenParticleBuilder> particleBuilderConsumer) {
        particleBuilderConsumer.accept(this);
        return this;
    }

    public ScreenParticleBuilder addActor(Consumer<GenericScreenParticle> particleActor) {
        options.actor = particleActor;
        return this;
    }

    public ScreenParticleBuilder spawn(double x, double y) {
        double yaw = RANDOM.nextFloat() * Math.PI * 2, pitch = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xSpeed = RANDOM.nextFloat() * maxXSpeed, ySpeed = RANDOM.nextFloat() * maxYSpeed;
        this.xMotion += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
        this.yMotion += Math.sin(pitch) * ySpeed;
        double yaw2 = RANDOM.nextFloat() * Math.PI * 2, pitch2 = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xDist = RANDOM.nextFloat() * maxXOffset, yDist = RANDOM.nextFloat() * maxYOffset;
        double xPos = Math.sin(yaw2) * Math.cos(pitch2) * xDist;
        double yPos = Math.sin(pitch2) * yDist;
        ScreenParticleHandler.addParticle(target, options, x + xPos, y + yPos, xMotion, yMotion);
        return this;
    }

    public ScreenParticleBuilder repeat(double x, double y, int n) {
        for (int i = 0; i < n; i++) spawn(x, y);
        return this;
    }

    public ScreenParticleBuilder spawnOnStack(double xOffset, double yOffset) {
        options.tracksStack = true;
        options.stackTrackXOffset = xOffset;
        options.stackTrackYOffset = yOffset;
        spawn(ScreenParticleHandler.currentItemX + xOffset, ScreenParticleHandler.currentItemY + yOffset);
        return this;
    }

    public ScreenParticleBuilder repeatOnStack(double xOffset, double yOffset, int n) {
        for (int i = 0; i < n; i++) spawnOnStack(xOffset, yOffset);
        return this;
    }

    @Override
    public ScreenParticleBuilder modifyData(Supplier<GenericParticleData> dataType, Consumer<GenericParticleData> dataConsumer) {
        return (ScreenParticleBuilder) super.modifyData(dataType, dataConsumer);
    }

    @Override
    public ScreenParticleBuilder modifyData(Function<AbstractParticleBuilder<ScreenParticleOptions>, GenericParticleData> dataType, Consumer<GenericParticleData> dataConsumer) {
        return (ScreenParticleBuilder) super.modifyData(dataType, dataConsumer);
    }

    @Override
    public ScreenParticleBuilder modifyColorData(Consumer<ColorParticleData> dataConsumer) {
        return (ScreenParticleBuilder) super.modifyColorData(dataConsumer);
    }

    @Override
    public ScreenParticleBuilder setColorData(ColorParticleData colorData) {
        return (ScreenParticleBuilder) super.setColorData(colorData);
    }

    @Override
    public ScreenParticleBuilder setScaleData(GenericParticleData scaleData) {
        return (ScreenParticleBuilder) super.setScaleData(scaleData);
    }

    @Override
    public ScreenParticleBuilder setTransparencyData(GenericParticleData transparencyData) {
        return (ScreenParticleBuilder) super.setTransparencyData(transparencyData);
    }

    @Override
    public ScreenParticleBuilder setSpinData(SpinParticleData spinData) {
        return (ScreenParticleBuilder) super.setSpinData(spinData);
    }

    @Override
    public ScreenParticleBuilder multiplyGravity(float gravityMultiplier) {
        return (ScreenParticleBuilder) super.multiplyGravity(gravityMultiplier);
    }

    @Override
    public ScreenParticleBuilder modifyGravity(Function<Float, Supplier<Float>> gravityReplacement) {
        return (ScreenParticleBuilder) super.modifyGravity(gravityReplacement);
    }

    @Override
    public ScreenParticleBuilder setGravityStrength(float gravity) {
        return (ScreenParticleBuilder) super.setGravityStrength(gravity);
    }

    @Override
    public ScreenParticleBuilder setGravityStrength(Supplier<Float> gravityStrengthSupplier) {
        return (ScreenParticleBuilder) super.setGravityStrength(gravityStrengthSupplier);
    }

    @Override
    public ScreenParticleBuilder multiplyLifetime(float lifetimeMultiplier) {
        return (ScreenParticleBuilder) super.multiplyLifetime(lifetimeMultiplier);
    }

    @Override
    public ScreenParticleBuilder modifyLifetime(Function<Integer, Supplier<Integer>> lifetimeReplacement) {
        return (ScreenParticleBuilder) super.modifyLifetime(lifetimeReplacement);
    }

    @Override
    public ScreenParticleBuilder setLifetime(int lifetime) {
        return (ScreenParticleBuilder) super.setLifetime(lifetime);
    }

    @Override
    public ScreenParticleBuilder setLifetime(Supplier<Integer> lifetimeSupplier) {
        return (ScreenParticleBuilder) super.setLifetime(lifetimeSupplier);
    }

    @Override
    public ScreenParticleBuilder multiplyLifeDelay(float lifeDelayMultiplier) {
        return (ScreenParticleBuilder) super.multiplyLifeDelay(lifeDelayMultiplier);
    }

    @Override
    public ScreenParticleBuilder modifyLifeDelay(Function<Integer, Supplier<Integer>> lifeDelayReplacement) {
        return (ScreenParticleBuilder) super.modifyLifeDelay(lifeDelayReplacement);
    }

    @Override
    public ScreenParticleBuilder setLifeDelay(int lifeDelay) {
        return (ScreenParticleBuilder) super.setLifeDelay(lifeDelay);
    }

    @Override
    public ScreenParticleBuilder setLifeDelay(Supplier<Integer> lifeDelaySupplier) {
        return (ScreenParticleBuilder) super.setLifeDelay(lifeDelaySupplier);
    }
}