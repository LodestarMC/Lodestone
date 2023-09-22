package team.lodestar.lodestone.systems.particle.builder;

import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.systems.particle.*;
import team.lodestar.lodestone.systems.particle.screen.*;

import java.util.Random;
import java.util.function.Consumer;

public class ScreenParticleBuilder extends AbstractParticleBuilder<ScreenParticleBuilder, ScreenParticleOptions> {

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
        for (int i = 0; i < n; i++) spawn(xOffset, yOffset);
        return this;
    }
}