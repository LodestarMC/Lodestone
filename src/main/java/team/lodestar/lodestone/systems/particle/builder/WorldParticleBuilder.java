package team.lodestar.lodestone.systems.particle.builder;

import it.unimi.dsi.fastutil.floats.*;
import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.client.particle.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.joml.*;
import team.lodestar.lodestone.systems.particle.*;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.data.color.*;
import team.lodestar.lodestone.systems.particle.data.spin.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;
import team.lodestar.lodestone.systems.particle.world.options.*;
import team.lodestar.lodestone.systems.particle.world.type.*;
import team.lodestar.lodestone.systems.particle.*;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.data.color.*;
import team.lodestar.lodestone.systems.particle.data.spin.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;
import team.lodestar.lodestone.systems.particle.world.options.*;
import team.lodestar.lodestone.systems.particle.world.type.*;
import team.lodestar.lodestone.helpers.*;
import team.lodestar.lodestone.helpers.block.*;
import team.lodestar.lodestone.systems.rendering.buffer.LodestoneRenderLayer;

import java.lang.*;
import java.lang.Math;
import java.util.*;
import java.util.Random;
import java.util.function.*;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class WorldParticleBuilder extends AbstractParticleBuilder<WorldParticleOptions> {

    private static final Random RANDOM = new Random();

    final WorldParticleOptions options;

    boolean forceSpawn = false;
    double zMotion = 0;
    double maxZSpeed = 0;
    double maxZOffset = 0;

    public static WorldParticleBuilder create(Holder<? extends LodestoneWorldParticleType> particle) {
        return create(particle.value());
    }

    public static WorldParticleBuilder create(Supplier<? extends LodestoneWorldParticleType> particle) {
        return create(particle.get());
    }

    public static WorldParticleBuilder create(LodestoneWorldParticleType particle) {
        return create(new WorldParticleOptions(particle));
    }

    public static WorldParticleBuilder create(WorldParticleOptions options) {
        return new WorldParticleBuilder(options);
    }

    protected WorldParticleBuilder(WorldParticleOptions options) {
        this.options = options;
    }

    @Override
    public WorldParticleOptions getParticleOptions() {
        return options;
    }

    public <T extends LodestoneParticleBehavior> WorldParticleBuilder setBehavior(Class<T> targetClass, Function<T, LodestoneParticleBehavior> behaviorFunction) {
        var behavior = getBehavior(targetClass);
        return setBehavior(behavior != null ? behaviorFunction.apply(behavior) : null);
    }

    public <T extends LodestoneParticleBehavior> GenericParticleData getBehaviorData(Class<T> targetClass, Function<T, GenericParticleData> dataFunction) {
        var behavior = getBehavior(targetClass);
        return behavior != null ? dataFunction.apply(behavior) : null;
    }

    public WorldParticleBuilder setBehavior(LodestoneParticleBehavior behavior) {
        getParticleOptions().setBehavior(behavior);
        return this;
    }

    public <T extends LodestoneParticleBehavior> T getBehavior(Class<T> targetClass) {
        if (targetClass.isInstance(getParticleOptions().behavior)) {
            return targetClass.cast(getParticleOptions().behavior);
        }
        return null;
    }

    /**
     * @deprecated Use one of the following instead:
     * {@link #modifyColorData(Consumer)}
     * {@link #modifyScaleData(Consumer)}}
     * {@link #modifyLengthData(Consumer)}}
     * {@link #modifyTransparencyData(Consumer)}}
     * {@link #modifySpinData(Consumer)}}
     */
    @Deprecated(forRemoval = true, since = "1.7.2")
    public WorldParticleBuilder modifyData(GenericParticleData dataType, Consumer<GenericParticleData> dataConsumer) {
        if (dataType != null) {
            dataConsumer.accept(dataType);
        }
        return this;
    }

    /**
     * @deprecated Use one of the following instead:
     * {@link #modifyColorData(Consumer)}
     * {@link #modifyScaleData(Consumer)}}
     * {@link #modifyLengthData(Consumer)}}
     * {@link #modifyTransparencyData(Consumer)}}
     * {@link #modifySpinData(Consumer)}}
     */
    @Deprecated(forRemoval = true, since = "1.7.2")
    public WorldParticleBuilder modifyData(Supplier<GenericParticleData> dataType, Consumer<GenericParticleData> dataConsumer) {
        return modifyData(dataType.get(), dataConsumer);
    }

    /**
     * @deprecated Use one of the following instead:
     * {@link #modifyColorData(Consumer)}
     * {@link #modifyScaleData(Consumer)}}
     * {@link #modifyLengthData(Consumer)}}
     * {@link #modifyTransparencyData(Consumer)}}
     * {@link #modifySpinData(Consumer)}}
     */
    @Deprecated(forRemoval = true, since = "1.7.2")
    public WorldParticleBuilder modifyData(Function<WorldParticleBuilder, GenericParticleData> dataType, Consumer<GenericParticleData> dataConsumer) {
        modifyData(dataType.apply(this), dataConsumer);
        return this;
    }

    /**
     * @deprecated Use one of the following instead:
     * {@link #modifyColorData(Consumer)}
     * {@link #modifyScaleData(Consumer)}}
     * {@link #modifyLengthData(Consumer)}}
     * {@link #modifyTransparencyData(Consumer)}}
     * {@link #modifySpinData(Consumer)}}
     */
    @Deprecated(forRemoval = true, since = "1.7.2")
    public final WorldParticleBuilder modifyData(Collection<Supplier<GenericParticleData>> dataTypes, Consumer<GenericParticleData> dataConsumer) {
        for (Supplier<GenericParticleData> dataFunction : dataTypes) {
            dataConsumer.accept(dataFunction.get());
        }
        return this;
    }

    public WorldParticleBuilder enableNoClip() {
        return setNoClip(true);
    }

    public WorldParticleBuilder disableNoClip() {
        return setNoClip(false);
    }

    public WorldParticleBuilder setNoClip(boolean noClip) {
        getParticleOptions().noClip = noClip;
        return this;
    }

    public WorldParticleBuilder setRenderType(ParticleRenderType renderType) {
        getParticleOptions().renderType = renderType;
        return this;
    }

    public WorldParticleBuilder setRenderTarget(LodestoneRenderLayer renderLayer) {
        getParticleOptions().renderLayer = renderLayer;
        return this;
    }

    public WorldParticleBuilder enableForcedSpawn() {
        return setForceSpawn(true);
    }

    public WorldParticleBuilder disableForcedSpawn() {
        return setForceSpawn(false);
    }

    public WorldParticleBuilder setForceSpawn(boolean forceSpawn) {
        this.forceSpawn = forceSpawn;
        return this;
    }

    public WorldParticleBuilder setRandomMotion(double maxSpeed) {
        return setRandomMotion(maxSpeed, maxSpeed, maxSpeed);
    }

    public WorldParticleBuilder setRandomMotion(double maxHSpeed, double maxVSpeed) {
        return setRandomMotion(maxHSpeed, maxVSpeed, maxHSpeed);
    }

    public WorldParticleBuilder setRandomMotion(double maxXSpeed, double maxYSpeed, double maxZSpeed) {
        this.maxXSpeed = maxXSpeed;
        this.maxYSpeed = maxYSpeed;
        this.maxZSpeed = maxZSpeed;
        return this;
    }

    public WorldParticleBuilder addMotion(Vector3f motion) {
        return addMotion(motion.x(), motion.y(), motion.z());
    }

    public WorldParticleBuilder addMotion(Vec3 motion) {
        return addMotion(motion.x, motion.y, motion.z);
    }

    public WorldParticleBuilder addMotion(double vx, double vy, double vz) {
        this.xMotion += vx;
        this.yMotion += vy;
        this.zMotion += vz;
        return this;
    }

    public WorldParticleBuilder setMotion(Vector3f motion) {
        return setMotion(motion.x(), motion.y(), motion.z());
    }

    public WorldParticleBuilder setMotion(Vec3 motion) {
        return setMotion(motion.x, motion.y, motion.z);
    }

    public WorldParticleBuilder setMotion(double vx, double vy, double vz) {
        this.xMotion = vx;
        this.yMotion = vy;
        this.zMotion = vz;
        return this;
    }

    public WorldParticleBuilder setRandomOffset(double maxDistance) {
        return setRandomOffset(maxDistance, maxDistance, maxDistance);
    }

    public WorldParticleBuilder setRandomOffset(double maxHDist, double maxVDist) {
        return setRandomOffset(maxHDist, maxVDist, maxHDist);
    }

    public WorldParticleBuilder setRandomOffset(double maxXDist, double maxYDist, double maxZDist) {
        this.maxXOffset = maxXDist;
        this.maxYOffset = maxYDist;
        this.maxZOffset = maxZDist;
        return this;
    }

    public WorldParticleBuilder act(Consumer<WorldParticleBuilder> particleBuilderConsumer) {
        particleBuilderConsumer.accept(this);
        return this;
    }

    public WorldParticleBuilder addTickActor(Consumer<LodestoneWorldParticle> particleActor) {
        getParticleOptions().tickActors.add(particleActor);
        return this;
    }
    public WorldParticleBuilder addSpawnActor(Consumer<LodestoneWorldParticle> particleActor) {
        getParticleOptions().spawnActors.add(particleActor);
        return this;
    }
    public WorldParticleBuilder addRenderActor(Consumer<LodestoneWorldParticle> particleActor) {
        getParticleOptions().renderActors.add(particleActor);
        return this;
    }

    public WorldParticleBuilder clearActors() {
        return clearTickActors().clearSpawnActors().clearRenderActors();
    }

    public WorldParticleBuilder clearTickActors() {
        getParticleOptions().tickActors.clear();
        return this;
    }
    public WorldParticleBuilder clearSpawnActors() {
        getParticleOptions().spawnActors.clear();
        return this;
    }
    public WorldParticleBuilder clearRenderActors() {
        getParticleOptions().renderActors.clear();
        return this;
    }

    public WorldParticleBuilder setNaturalLighting() {
        return setLightLevel(-1);
    }

    public WorldParticleBuilder setFullBrightLighting() {
        return setLightLevel(RenderHelper.FULL_BRIGHT);
    }

    public WorldParticleBuilder setLightLevel(int particleLight) {
        options.particleLight = particleLight;
        return this;
    }

    public WorldParticleBuilder spawn(Level level, Vec3 pos) {
        return spawn(level, pos.x, pos.y, pos.z);
    }

    public WorldParticleBuilder spawn(Level level, double x, double y, double z) {
        double yaw = RANDOM.nextFloat() * Math.PI * 2, pitch = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xSpeed = RANDOM.nextFloat() * maxXSpeed, ySpeed = RANDOM.nextFloat() * maxYSpeed, zSpeed = RANDOM.nextFloat() * maxZSpeed;
        this.xMotion += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
        this.yMotion += Math.sin(pitch) * ySpeed;
        this.zMotion += Math.cos(yaw) * Math.cos(pitch) * zSpeed;
        double yaw2 = RANDOM.nextFloat() * Math.PI * 2, pitch2 = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xDist = RANDOM.nextFloat() * maxXOffset, yDist = RANDOM.nextFloat() * maxYOffset, zDist = RANDOM.nextFloat() * maxZOffset;
        double xPos = Math.sin(yaw2) * Math.cos(pitch2) * xDist;
        double yPos = Math.sin(pitch2) * yDist;
        double zPos = Math.cos(yaw2) * Math.cos(pitch2) * zDist;

        level.addParticle(getParticleOptions(), forceSpawn, x + xPos, y + yPos, z + zPos, xMotion, yMotion, zMotion);
        return this;
    }

    public WorldParticleBuilder repeat(Level level, Vec3 pos, int n) {
        return repeat(level, pos.x, pos.y, pos.z, n);
    }

    public WorldParticleBuilder repeat(Level level, double x, double y, double z, int n) {
        for (int i = 0; i < n; i++) spawn(level, x, y, z);
        return this;
    }

    public WorldParticleBuilder surroundBlock(Level level, BlockPos pos, Direction... directions) {
        if (directions.length == 0) {
            directions = Direction.values();
        }
        for (Direction direction : directions) {
            double yaw = RANDOM.nextFloat() * Math.PI * 2, pitch = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xSpeed = RANDOM.nextFloat() * maxXSpeed, ySpeed = RANDOM.nextFloat() * maxYSpeed, zSpeed = RANDOM.nextFloat() * maxZSpeed;
            this.xMotion += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
            this.yMotion += Math.sin(pitch) * ySpeed;
            this.zMotion += Math.cos(yaw) * Math.cos(pitch) * zSpeed;

            Direction.Axis direction$axis = direction.getAxis();
            double d0 = 0.5625D;
            double xPos = direction$axis == Direction.Axis.X ? 0.5D + d0 * (double) direction.getStepX() : RANDOM.nextDouble();
            double yPos = direction$axis == Direction.Axis.Y ? 0.5D + d0 * (double) direction.getStepY() : RANDOM.nextDouble();
            double zPos = direction$axis == Direction.Axis.Z ? 0.5D + d0 * (double) direction.getStepZ() : RANDOM.nextDouble();

            level.addParticle(getParticleOptions(), forceSpawn, pos.getX() + xPos, pos.getY() + yPos, pos.getZ() + zPos, xMotion, yMotion, zMotion);

        }
        return this;
    }

    public WorldParticleBuilder repeatSurroundBlock(Level level, BlockPos pos, int n) {
        for (int i = 0; i < n; i++) surroundBlock(level, pos);
        return this;
    }

    public WorldParticleBuilder repeatSurroundBlock(Level level, BlockPos pos, int n, Direction... directions) {
        for (int i = 0; i < n; i++) surroundBlock(level, pos, directions);
        return this;
    }

    public WorldParticleBuilder surroundVoxelShape(Level level, BlockPos pos, VoxelShape voxelShape, int max) {
        int[] c = new int[1];
        int perBoxMax = max / voxelShape.toAabbs().size();
        Supplier<Boolean> r = () -> {
            c[0]++;
            if (c[0] >= perBoxMax) {
                c[0] = 0;
                return true;
            }
            return false;
        };
        Vec3 v = BlockPosHelper.fromBlockPos(pos);
        voxelShape.forAllBoxes(
                (x1, y1, z1, x2, y2, z2) -> {
                    Vec3 b = v.add(x1, y1, z1);
                    Vec3 e = v.add(x2, y2, z2);
                    List<Runnable> runs = new ArrayList<>();
                    runs.add(() -> spawnLine(level, b, v.add(x2, y1, z1)));
                    runs.add(() -> spawnLine(level, b, v.add(x1, y2, z1)));
                    runs.add(() -> spawnLine(level, b, v.add(x1, y1, z2)));
                    runs.add(() -> spawnLine(level, v.add(x1, y2, z1), v.add(x2, y2, z1)));
                    runs.add(() -> spawnLine(level, v.add(x1, y2, z1), v.add(x1, y2, z2)));
                    runs.add(() -> spawnLine(level, e, v.add(x2, y2, z1)));
                    runs.add(() -> spawnLine(level, e, v.add(x1, y2, z2)));
                    runs.add(() -> spawnLine(level, e, v.add(x2, y1, z2)));
                    runs.add(() -> spawnLine(level, v.add(x2, y1, z1), v.add(x2, y1, z2)));
                    runs.add(() -> spawnLine(level, v.add(x1, y1, z2), v.add(x2, y1, z2)));
                    runs.add(() -> spawnLine(level, v.add(x2, y1, z1), v.add(x2, y2, z1)));
                    runs.add(() -> spawnLine(level, v.add(x1, y1, z2), v.add(x1, y2, z2)));
                    Collections.shuffle(runs);
                    for (Runnable runnable : runs) {
                        runnable.run();
                        if (r.get()) {
                            break;
                        }
                    }
                }
        );
        return this;
    }

    public WorldParticleBuilder surroundVoxelShape(Level level, BlockPos pos, BlockState state, int max) {
        VoxelShape voxelShape = state.getShape(level, pos);
        if (voxelShape.isEmpty()) {
            voxelShape = Shapes.block();
        }
        return surroundVoxelShape(level, pos, voxelShape, max);
    }

    public WorldParticleBuilder spawnAtRandomFace(Level level, BlockPos pos) {
        Direction direction = Direction.values()[RANDOM.nextInt(Direction.values().length)];
        double yaw = RANDOM.nextFloat() * Math.PI * 2, pitch = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xSpeed = RANDOM.nextFloat() * maxXSpeed, ySpeed = RANDOM.nextFloat() * maxYSpeed, zSpeed = RANDOM.nextFloat() * maxZSpeed;
        this.xMotion += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
        this.yMotion += Math.sin(pitch) * ySpeed;
        this.zMotion += Math.cos(yaw) * Math.cos(pitch) * zSpeed;

        Direction.Axis direction$axis = direction.getAxis();
        double d0 = 0.5625D;
        double xPos = direction$axis == Direction.Axis.X ? 0.5D + d0 * (double) direction.getStepX() : RANDOM.nextDouble();
        double yPos = direction$axis == Direction.Axis.Y ? 0.5D + d0 * (double) direction.getStepY() : RANDOM.nextDouble();
        double zPos = direction$axis == Direction.Axis.Z ? 0.5D + d0 * (double) direction.getStepZ() : RANDOM.nextDouble();

        level.addParticle(getParticleOptions(), forceSpawn, pos.getX() + xPos, pos.getY() + yPos, pos.getZ() + zPos, xMotion, yMotion, zMotion);
        return this;
    }

    public WorldParticleBuilder repeatRandomFace(Level level, BlockPos pos, int n) {
        for (int i = 0; i < n; i++) spawnAtRandomFace(level, pos);
        return this;
    }

    public WorldParticleBuilder createBlockOutline(Level level, BlockPos pos, BlockState state) {
        VoxelShape voxelShape = state.getShape(level, pos);
        voxelShape.forAllBoxes(
                (x1, y1, z1, x2, y2, z2) -> {
                    var v = BlockPosHelper.fromBlockPos(pos);
                    var b = v.add(x1, y1, z1);
                    var e = v.add(x2, y2, z2);
                    spawnLine(level, b, v.add(x2, y1, z1));
                    spawnLine(level, b, v.add(x1, y2, z1));
                    spawnLine(level, b, v.add(x1, y1, z2));
                    spawnLine(level, v.add(x1, y2, z1), v.add(x2, y2, z1));
                    spawnLine(level, v.add(x1, y2, z1), v.add(x1, y2, z2));
                    spawnLine(level, e, v.add(x2, y2, z1));
                    spawnLine(level, e, v.add(x1, y2, z2));
                    spawnLine(level, e, v.add(x2, y1, z2));
                    spawnLine(level, v.add(x2, y1, z1), v.add(x2, y1, z2));
                    spawnLine(level, v.add(x1, y1, z2), v.add(x2, y1, z2));
                    spawnLine(level, v.add(x2, y1, z1), v.add(x2, y2, z1));
                    spawnLine(level, v.add(x1, y1, z2), v.add(x1, y2, z2));
                }
        );
        return this;
    }

    public WorldParticleBuilder spawnLine(Level level, Vec3 one, Vec3 two) {
        double yaw = RANDOM.nextFloat() * Math.PI * 2, pitch = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xSpeed = RANDOM.nextFloat() * maxXSpeed, ySpeed = RANDOM.nextFloat() * maxYSpeed, zSpeed = RANDOM.nextFloat() * maxZSpeed;
        this.xMotion += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
        this.yMotion += Math.sin(pitch) * ySpeed;
        this.zMotion += Math.cos(yaw) * Math.cos(pitch) * zSpeed;
        Vec3 pos = one.lerp(two, RANDOM.nextDouble());
        level.addParticle(getParticleOptions(), forceSpawn, pos.x, pos.y, pos.z, xMotion, yMotion, zMotion);
        return this;
    }

    @Override
    public WorldParticleBuilder modifyColorData(Consumer<ColorParticleData> dataConsumer) {
        return (WorldParticleBuilder) super.modifyColorData(dataConsumer);
    }

    @Override
    public WorldParticleBuilder setColorData(ColorParticleDataWrapper colorData) {
        return (WorldParticleBuilder) super.setColorData(colorData);
    }

    @Override
    public WorldParticleBuilder modifyScaleData(Consumer<GenericParticleData> dataConsumer) {
        return (WorldParticleBuilder) super.modifyScaleData(dataConsumer);
    }

    @Override
    public WorldParticleBuilder setScaleData(GenericParticleDataWrapper scaleData) {
        return (WorldParticleBuilder) super.setScaleData(scaleData);
    }

    @Override
    public WorldParticleBuilder modifyLengthData(Consumer<GenericParticleData> dataConsumer) {
        return (WorldParticleBuilder) super.modifyLengthData(dataConsumer);
    }

    @Override
    public WorldParticleBuilder setLengthData(GenericParticleDataWrapper lengthData) {
        return (WorldParticleBuilder) super.setLengthData(lengthData);
    }

    @Override
    public WorldParticleBuilder setLifeDelay(int lifeDelay) {
        return (WorldParticleBuilder) super.setLifeDelay(lifeDelay);
    }

    @Override
    public WorldParticleBuilder setLifeDelay(Supplier<Integer> supplier) {
        return (WorldParticleBuilder) super.setLifeDelay(supplier);
    }

    @Override
    public WorldParticleBuilder setLifetime(int lifetime) {
        return (WorldParticleBuilder) super.setLifetime(lifetime);
    }

    @Override
    public WorldParticleBuilder setLifetime(Supplier<Integer> supplier) {
        return (WorldParticleBuilder) super.setLifetime(supplier);
    }

    @Override
    public WorldParticleBuilder setGravity(float gravity) {
        return (WorldParticleBuilder) super.setGravity(gravity);
    }

    @Override
    public WorldParticleBuilder setGravity(Supplier<Float> supplier) {
        return (WorldParticleBuilder) super.setGravity(supplier);
    }

    @Override
    public WorldParticleBuilder setFriction(float friction) {
        return (WorldParticleBuilder) super.setFriction(friction);
    }

    @Override
    public WorldParticleBuilder setFriction(Supplier<Float> supplier) {
        return (WorldParticleBuilder) super.setFriction(supplier);
    }

    @Override
    public WorldParticleBuilder multiplyLifeDelay(float multiplier) {
        return (WorldParticleBuilder) super.multiplyLifeDelay(multiplier);
    }

    @Override
    public WorldParticleBuilder modifyLifeDelay(Int2IntFunction modifier) {
        return (WorldParticleBuilder) super.modifyLifeDelay(modifier);
    }

    @Override
    public WorldParticleBuilder setLifeDelayModifier(float multiplier) {
        return (WorldParticleBuilder) super.setLifeDelayModifier(multiplier);
    }

    @Override
    public WorldParticleBuilder setLifeDelayModifier(Int2IntFunction modifier) {
        return (WorldParticleBuilder) super.setLifeDelayModifier(modifier);
    }

    @Override
    public WorldParticleBuilder multiplyLifetime(float multiplier) {
        return (WorldParticleBuilder) super.multiplyLifetime(multiplier);
    }

    @Override
    public WorldParticleBuilder modifyLifetime(Int2IntFunction modifier) {
        return (WorldParticleBuilder) super.modifyLifetime(modifier);
    }

    @Override
    public WorldParticleBuilder setLifetimeModifier(float multiplier) {
        return (WorldParticleBuilder) super.setLifetimeModifier(multiplier);
    }

    @Override
    public WorldParticleBuilder setLifetimeModifier(Int2IntFunction modifier) {
        return (WorldParticleBuilder) super.setLifetimeModifier(modifier);
    }

    @Override
    public WorldParticleBuilder multiplyGravity(float multiplier) {
        return (WorldParticleBuilder) super.multiplyGravity(multiplier);
    }

    @Override
    public WorldParticleBuilder modifyGravity(Float2FloatFunction modifier) {
        return (WorldParticleBuilder) super.modifyGravity(modifier);
    }

    @Override
    public WorldParticleBuilder setGravityModifier(float multiplier) {
        return (WorldParticleBuilder) super.setGravityModifier(multiplier);
    }

    @Override
    public WorldParticleBuilder setGravityModifier(Float2FloatFunction modifier) {
        return (WorldParticleBuilder) super.setGravityModifier(modifier);
    }

    @Override
    public WorldParticleBuilder multiplyFriction(float multiplier) {
        return (WorldParticleBuilder) super.multiplyFriction(multiplier);
    }

    @Override
    public WorldParticleBuilder modifyFriction(Float2FloatFunction modifier) {
        return (WorldParticleBuilder) super.modifyFriction(modifier);
    }

    @Override
    public WorldParticleBuilder setFrictionModifier(float multiplier) {
        return (WorldParticleBuilder) super.setFrictionModifier(multiplier);
    }

    @Override
    public WorldParticleBuilder setFrictionModifier(Float2FloatFunction modifier) {
        return (WorldParticleBuilder) super.setFrictionModifier(modifier);
    }

    @Override
    public WorldParticleBuilder setSpinData(SpinParticleDataWrapper spinData) {
        return (WorldParticleBuilder) super.setSpinData(spinData);
    }

    @Override
    public WorldParticleBuilder modifySpinData(Consumer<SpinParticleData> dataConsumer) {
        return (WorldParticleBuilder) super.modifySpinData(dataConsumer);
    }

    @Override
    public WorldParticleBuilder setTransparencyData(GenericParticleDataWrapper transparencyData) {
        return (WorldParticleBuilder) super.setTransparencyData(transparencyData);
    }

    @Override
    public WorldParticleBuilder modifyTransparencyData(Consumer<GenericParticleData> dataConsumer) {
        return (WorldParticleBuilder) super.modifyTransparencyData(dataConsumer);
    }

    @Override
    public WorldParticleBuilder setSpritePicker(SimpleParticleOptions.ParticleSpritePicker spritePicker) {
        return (WorldParticleBuilder) super.setSpritePicker(spritePicker);
    }
}