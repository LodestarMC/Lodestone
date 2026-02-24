package team.lodestar.lodestone.systems.particle.builder;

import it.unimi.dsi.fastutil.floats.*;
import it.unimi.dsi.fastutil.ints.*;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.data.color.*;
import team.lodestar.lodestone.systems.particle.data.spin.*;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleOptions;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneScreenParticleRenderType;
import team.lodestar.lodestone.systems.particle.screen.LodestoneScreenParticle;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleType;
import java.util.*;
import java.util.function.*;

public class ScreenParticleBuilder extends AbstractParticleBuilder<ScreenParticleOptions> {

    private static final Random RANDOM = new Random();

    final ScreenParticleType type;
    final ScreenParticleOptions options;
    final ScreenParticleHolder target;

    public static ScreenParticleBuilder create(ScreenParticleType type, ScreenParticleHolder target) {
        return new ScreenParticleBuilder(type, target);
    }

    protected ScreenParticleBuilder(ScreenParticleType type, ScreenParticleHolder target) {
        this.type = type;
        this.options = new ScreenParticleOptions(type);
        this.target = target;
    }

    @Override
    public ScreenParticleOptions getParticleOptions() {
        return options;
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

    public ScreenParticleBuilder addTickActor(Consumer<LodestoneScreenParticle> particleActor) {
        getParticleOptions().tickActors.add(particleActor);
        return this;
    }
    public ScreenParticleBuilder addSpawnActor(Consumer<LodestoneScreenParticle> particleActor) {
        getParticleOptions().spawnActors.add(particleActor);
        return this;
    }
    public ScreenParticleBuilder addRenderActor(Consumer<LodestoneScreenParticle> particleActor) {
        getParticleOptions().renderActors.add(particleActor);
        return this;
    }

    public ScreenParticleBuilder clearActors() {
        return clearTickActor().clearSpawnActors().clearRenderActors();
    }

    public ScreenParticleBuilder clearTickActor() {
        getParticleOptions().tickActors.clear();
        return this;
    }
    public ScreenParticleBuilder clearSpawnActors() {
        getParticleOptions().spawnActors.clear();
        return this;
    }
    public ScreenParticleBuilder clearRenderActors() {
        getParticleOptions().renderActors.clear();
        return this;
    }

    public ScreenParticleBuilder spawn(double x, double y) {
        double yaw = RANDOM.nextFloat() * Math.PI * 2, pitch = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xSpeed = RANDOM.nextFloat() * maxXSpeed, ySpeed = RANDOM.nextFloat() * maxYSpeed;
        this.xMotion += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
        this.yMotion += Math.sin(pitch) * ySpeed;
        double yaw2 = RANDOM.nextFloat() * Math.PI * 2, pitch2 = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xDist = RANDOM.nextFloat() * maxXOffset, yDist = RANDOM.nextFloat() * maxYOffset;
        double xPos = Math.sin(yaw2) * Math.cos(pitch2) * xDist;
        double yPos = Math.sin(pitch2) * yDist;
        target.addParticle(options, x + xPos, y + yPos, xMotion, yMotion);
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
    public ScreenParticleBuilder modifyColorData(Consumer<ColorParticleData> dataConsumer) {
        return (ScreenParticleBuilder) super.modifyColorData(dataConsumer);
    }

    @Override
    public ScreenParticleBuilder setColorData(ColorParticleDataWrapper colorData) {
        return (ScreenParticleBuilder) super.setColorData(colorData);
    }

    @Override
    public ScreenParticleBuilder modifyScaleData(Consumer<GenericParticleData> dataConsumer) {
        return (ScreenParticleBuilder) super.modifyScaleData(dataConsumer);
    }

    @Override
    public ScreenParticleBuilder setScaleData(GenericParticleDataWrapper scaleData) {
        return (ScreenParticleBuilder) super.setScaleData(scaleData);
    }

    @Override
    public ScreenParticleBuilder modifyLengthData(Consumer<GenericParticleData> dataConsumer) {
        return (ScreenParticleBuilder) super.modifyLengthData(dataConsumer);
    }

    @Override
    public ScreenParticleBuilder setLengthData(GenericParticleDataWrapper lengthData) {
        return (ScreenParticleBuilder) super.setLengthData(lengthData);
    }

    @Override
    public ScreenParticleBuilder setLifeDelay(int lifeDelay) {
        return (ScreenParticleBuilder) super.setLifeDelay(lifeDelay);
    }

    @Override
    public ScreenParticleBuilder setLifeDelay(Supplier<Integer> supplier) {
        return (ScreenParticleBuilder) super.setLifeDelay(supplier);
    }

    @Override
    public ScreenParticleBuilder setLifetime(int lifetime) {
        return (ScreenParticleBuilder) super.setLifetime(lifetime);
    }

    @Override
    public ScreenParticleBuilder setLifetime(Supplier<Integer> supplier) {
        return (ScreenParticleBuilder) super.setLifetime(supplier);
    }

    @Override
    public ScreenParticleBuilder setGravity(float gravity) {
        return (ScreenParticleBuilder) super.setGravity(gravity);
    }

    @Override
    public ScreenParticleBuilder setGravity(Supplier<Float> supplier) {
        return (ScreenParticleBuilder) super.setGravity(supplier);
    }

    @Override
    public ScreenParticleBuilder setFriction(float friction) {
        return (ScreenParticleBuilder) super.setFriction(friction);
    }

    @Override
    public ScreenParticleBuilder setFriction(Supplier<Float> supplier) {
        return (ScreenParticleBuilder) super.setFriction(supplier);
    }

    @Override
    public ScreenParticleBuilder multiplyLifeDelay(float multiplier) {
        return (ScreenParticleBuilder) super.multiplyLifeDelay(multiplier);
    }

    @Override
    public ScreenParticleBuilder modifyLifeDelay(Int2IntFunction modifier) {
        return (ScreenParticleBuilder) super.modifyLifeDelay(modifier);
    }

    @Override
    public ScreenParticleBuilder setLifeDelayModifier(float multiplier) {
        return (ScreenParticleBuilder) super.setLifeDelayModifier(multiplier);
    }

    @Override
    public ScreenParticleBuilder setLifeDelayModifier(Int2IntFunction modifier) {
        return (ScreenParticleBuilder) super.setLifeDelayModifier(modifier);
    }

    @Override
    public ScreenParticleBuilder multiplyLifetime(float multiplier) {
        return (ScreenParticleBuilder) super.multiplyLifetime(multiplier);
    }

    @Override
    public ScreenParticleBuilder modifyLifetime(Int2IntFunction modifier) {
        return (ScreenParticleBuilder) super.modifyLifetime(modifier);
    }

    @Override
    public ScreenParticleBuilder setLifetimeModifier(float multiplier) {
        return (ScreenParticleBuilder) super.setLifetimeModifier(multiplier);
    }

    @Override
    public ScreenParticleBuilder setLifetimeModifier(Int2IntFunction modifier) {
        return (ScreenParticleBuilder) super.setLifetimeModifier(modifier);
    }

    @Override
    public ScreenParticleBuilder multiplyGravity(float multiplier) {
        return (ScreenParticleBuilder) super.multiplyGravity(multiplier);
    }

    @Override
    public ScreenParticleBuilder modifyGravity(Float2FloatFunction modifier) {
        return (ScreenParticleBuilder) super.modifyGravity(modifier);
    }

    @Override
    public ScreenParticleBuilder setGravityModifier(float multiplier) {
        return (ScreenParticleBuilder) super.setGravityModifier(multiplier);
    }

    @Override
    public ScreenParticleBuilder setGravityModifier(Float2FloatFunction modifier) {
        return (ScreenParticleBuilder) super.setGravityModifier(modifier);
    }

    @Override
    public ScreenParticleBuilder multiplyFriction(float multiplier) {
        return (ScreenParticleBuilder) super.multiplyFriction(multiplier);
    }

    @Override
    public ScreenParticleBuilder modifyFriction(Float2FloatFunction modifier) {
        return (ScreenParticleBuilder) super.modifyFriction(modifier);
    }

    @Override
    public ScreenParticleBuilder setFrictionModifier(float multiplier) {
        return (ScreenParticleBuilder) super.setFrictionModifier(multiplier);
    }

    @Override
    public ScreenParticleBuilder setFrictionModifier(Float2FloatFunction modifier) {
        return (ScreenParticleBuilder) super.setFrictionModifier(modifier);
    }

    @Override
    public ScreenParticleBuilder setSpinData(SpinParticleDataWrapper spinData) {
        return (ScreenParticleBuilder) super.setSpinData(spinData);
    }

    @Override
    public ScreenParticleBuilder modifySpinData(Consumer<SpinParticleData> dataConsumer) {
        return (ScreenParticleBuilder) super.modifySpinData(dataConsumer);
    }

    @Override
    public ScreenParticleBuilder setTransparencyData(GenericParticleDataWrapper transparencyData) {
        return (ScreenParticleBuilder) super.setTransparencyData(transparencyData);
    }

    @Override
    public ScreenParticleBuilder modifyTransparencyData(Consumer<GenericParticleData> dataConsumer) {
        return (ScreenParticleBuilder) super.modifyTransparencyData(dataConsumer);
    }

    @Override
    public ScreenParticleBuilder setSpritePicker(SimpleParticleOptions.ParticleSpritePicker spritePicker) {
        return (ScreenParticleBuilder) super.setSpritePicker(spritePicker);
    }
}