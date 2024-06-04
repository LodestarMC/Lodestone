package team.lodestar.lodestone.systems.particle.builder;

import net.minecraft.client.particle.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.joml.*;
import team.lodestar.lodestone.handlers.*;
import team.lodestar.lodestone.helpers.*;
import team.lodestar.lodestone.systems.particle.*;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.data.color.*;
import team.lodestar.lodestone.systems.particle.data.spin.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;
import team.lodestar.lodestone.systems.particle.world.options.*;
import team.lodestar.lodestone.systems.particle.world.type.*;

import java.lang.*;
import java.lang.Math;
import java.util.*;
import java.util.Random;
import java.util.function.*;

public class WorldParticleBuilder<T extends WorldParticleOptions> extends AbstractParticleBuilder<T> {

    private static final Random RANDOM = new Random();

    final T options;

    boolean forceSpawn = false;
    double zMotion = 0;
    double maxZSpeed = 0;
    double maxZOffset = 0;

    public static WorldParticleBuilder<WorldParticleOptions> create(ParticleType<WorldParticleOptions> particle) {
        return create(particle, null);
    }
    public static WorldParticleBuilder<WorldParticleOptions> create(ParticleType<WorldParticleOptions> particle, LodestoneParticleBehavior behavior) {
        return create(new WorldParticleOptions(particle, behavior));
    }

    public static<T extends WorldParticleOptions> WorldParticleBuilder<T> create(T options) {
        return new WorldParticleBuilder<>(options);
    }

    protected WorldParticleBuilder(T options) {
        this.options = options;
    }

    @Override
    public T getParticleOptions() {
        return options;
    }

    public WorldParticleBuilder<T> enableNoClip() {
        return setNoClip(true);
    }

    public WorldParticleBuilder<T> disableNoClip() {
        return setNoClip(false);
    }

    public WorldParticleBuilder<T> setNoClip(boolean noClip) {
        getParticleOptions().noClip = noClip;
        return this;
    }

    public WorldParticleBuilder<T> setRenderType(ParticleRenderType renderType) {
        getParticleOptions().renderType = renderType;
        return this;
    }

    public WorldParticleBuilder<T> setRenderTarget(RenderHandler.LodestoneRenderLayer renderLayer) {
        getParticleOptions().renderLayer = renderLayer;
        return this;
    }

    public WorldParticleBuilder<T> enableForcedSpawn() {
        return setForceSpawn(true);
    }

    public WorldParticleBuilder<T> disableForcedSpawn() {
        return setForceSpawn(false);
    }

    public WorldParticleBuilder<T> setForceSpawn(boolean forceSpawn) {
        this.forceSpawn = forceSpawn;
        return this;
    }

    public WorldParticleBuilder<T> enableCull() {
        return setShouldCull(true);
    }

    public WorldParticleBuilder<T> disableCull() {
        return setShouldCull(false);
    }

    public WorldParticleBuilder<T> setShouldCull(boolean shouldCull) {
        getParticleOptions().shouldCull = shouldCull;
        return this;
    }

    public WorldParticleBuilder<T> setRandomMotion(double maxSpeed) {
        return setRandomMotion(maxSpeed, maxSpeed, maxSpeed);
    }

    public WorldParticleBuilder<T> setRandomMotion(double maxHSpeed, double maxVSpeed) {
        return setRandomMotion(maxHSpeed, maxVSpeed, maxHSpeed);
    }

    public WorldParticleBuilder<T> setRandomMotion(double maxXSpeed, double maxYSpeed, double maxZSpeed) {
        this.maxXSpeed = maxXSpeed;
        this.maxYSpeed = maxYSpeed;
        this.maxZSpeed = maxZSpeed;
        return this;
    }

    public WorldParticleBuilder<T> addMotion(Vector3f motion) {
        return addMotion(motion.x(), motion.y(), motion.z());
    }

    public WorldParticleBuilder<T> addMotion(Vec3 motion) {
        return addMotion(motion.x, motion.y, motion.z);
    }

    public WorldParticleBuilder<T> addMotion(double vx, double vy, double vz) {
        this.xMotion += vx;
        this.yMotion += vy;
        this.zMotion += vz;
        return this;
    }

    public WorldParticleBuilder<T> setMotion(Vector3f motion) {
        return setMotion(motion.x(), motion.y(), motion.z());
    }

    public WorldParticleBuilder<T> setMotion(Vec3 motion) {
        return setMotion(motion.x, motion.y, motion.z);
    }

    public WorldParticleBuilder<T> setMotion(double vx, double vy, double vz) {
        this.xMotion = vx;
        this.yMotion = vy;
        this.zMotion = vz;
        return this;
    }

    public WorldParticleBuilder<T> setRandomOffset(double maxDistance) {
        return setRandomOffset(maxDistance, maxDistance, maxDistance);
    }

    public WorldParticleBuilder<T> setRandomOffset(double maxHDist, double maxVDist) {
        return setRandomOffset(maxHDist, maxVDist, maxHDist);
    }

    public WorldParticleBuilder<T> setRandomOffset(double maxXDist, double maxYDist, double maxZDist) {
        this.maxXOffset = maxXDist;
        this.maxYOffset = maxYDist;
        this.maxZOffset = maxZDist;
        return this;
    }

    public WorldParticleBuilder<T> act(Consumer<WorldParticleBuilder<T>> particleBuilderConsumer) {
        particleBuilderConsumer.accept(this);
        return this;
    }

    public WorldParticleBuilder<T> addTickActor(Consumer<LodestoneWorldParticle> particleActor) {
        getParticleOptions().tickActors.add(particleActor);
        return this;
    }
    public WorldParticleBuilder<T> addSpawnActor(Consumer<LodestoneWorldParticle> particleActor) {
        getParticleOptions().spawnActors.add(particleActor);
        return this;
    }
    public WorldParticleBuilder<T> addRenderActor(Consumer<LodestoneWorldParticle> particleActor) {
        getParticleOptions().renderActors.add(particleActor);
        return this;
    }

    public WorldParticleBuilder<T> clearActors() {
        return clearTickActor().clearSpawnActors().clearRenderActors();
    }

    public WorldParticleBuilder<T> clearTickActor() {
        getParticleOptions().tickActors.clear();
        return this;
    }
    public WorldParticleBuilder<T> clearSpawnActors() {
        getParticleOptions().spawnActors.clear();
        return this;
    }
    public WorldParticleBuilder<T> clearRenderActors() {
        getParticleOptions().renderActors.clear();
        return this;
    }

    public WorldParticleBuilder<T> spawn(Level level, double x, double y, double z) {
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

    public WorldParticleBuilder<T> repeat(Level level, double x, double y, double z, int n) {
        for (int i = 0; i < n; i++) spawn(level, x, y, z);
        return this;
    }

    public WorldParticleBuilder<T> surroundBlock(Level level, BlockPos pos, Direction... directions) {
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

    public WorldParticleBuilder<T> repeatSurroundBlock(Level level, BlockPos pos, int n) {
        for (int i = 0; i < n; i++) surroundBlock(level, pos);
        return this;
    }

    public WorldParticleBuilder<T> repeatSurroundBlock(Level level, BlockPos pos, int n, Direction... directions) {
        for (int i = 0; i < n; i++) surroundBlock(level, pos, directions);
        return this;
    }

    public WorldParticleBuilder<T> surroundVoxelShape(Level level, BlockPos pos, VoxelShape voxelShape, int max) {
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
        Vec3 v = BlockHelper.fromBlockPos(pos);
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

    public WorldParticleBuilder<T> surroundVoxelShape(Level level, BlockPos pos, BlockState state, int max) {
        VoxelShape voxelShape = state.getShape(level, pos);
        if (voxelShape.isEmpty()) {
            voxelShape = Shapes.block();
        }
        return surroundVoxelShape(level, pos, voxelShape, max);
    }

    public WorldParticleBuilder<T> spawnAtRandomFace(Level level, BlockPos pos) {
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

    public WorldParticleBuilder<T> repeatRandomFace(Level level, BlockPos pos, int n) {
        for (int i = 0; i < n; i++) spawnAtRandomFace(level, pos);
        return this;
    }

    public WorldParticleBuilder<T> createCircle(Level level, double x, double y, double z, double distance, double currentCount, double totalCount) {
        double xSpeed = RANDOM.nextFloat() * maxXSpeed, ySpeed = RANDOM.nextFloat() * maxYSpeed, zSpeed = RANDOM.nextFloat() * maxZSpeed;
        double theta = (Math.PI * 2) / totalCount;
        double finalAngle = (currentCount / totalCount) + (theta * currentCount);
        double dx2 = (distance * Math.cos(finalAngle));
        double dz2 = (distance * Math.sin(finalAngle));

        Vector3d vector2f = new Vector3d(dx2, 0, dz2);
        this.xMotion = vector2f.x * xSpeed;
        this.zMotion = vector2f.z * zSpeed;

        double yaw2 = RANDOM.nextFloat() * Math.PI * 2, pitch2 = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xDist = RANDOM.nextFloat() * maxXOffset, yDist = RANDOM.nextFloat() * maxYOffset, zDist = RANDOM.nextFloat() * maxZOffset;
        double xPos = Math.sin(yaw2) * Math.cos(pitch2) * xDist;
        double yPos = Math.sin(pitch2) * yDist;
        double zPos = Math.cos(yaw2) * Math.cos(pitch2) * zDist;
        level.addParticle(getParticleOptions(), forceSpawn, x + xPos + dx2, y + yPos, z + zPos + dz2, xMotion, ySpeed, zMotion);
        return this;
    }

    public WorldParticleBuilder<T> repeatCircle(Level level, double x, double y, double z, double distance, int times) {
        for (int i = 0; i < times; i++) createCircle(level, x, y, z, distance, i, times);
        return this;
    }

    public WorldParticleBuilder<T> createBlockOutline(Level level, BlockPos pos, BlockState state) {
        VoxelShape voxelShape = state.getShape(level, pos);
        double d = 0.25;
        voxelShape.forAllBoxes(
                (x1, y1, z1, x2, y2, z2) -> {
                    Vec3 v = BlockHelper.fromBlockPos(pos);
                    Vec3 b = BlockHelper.fromBlockPos(pos).add(x1, y1, z1);
                    Vec3 e = BlockHelper.fromBlockPos(pos).add(x2, y2, z2);
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

    public WorldParticleBuilder<T> spawnLine(Level level, Vec3 one, Vec3 two) {
        double yaw = RANDOM.nextFloat() * Math.PI * 2, pitch = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xSpeed = RANDOM.nextFloat() * maxXSpeed, ySpeed = RANDOM.nextFloat() * maxYSpeed, zSpeed = RANDOM.nextFloat() * maxZSpeed;
        this.xMotion += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
        this.yMotion += Math.sin(pitch) * ySpeed;
        this.zMotion += Math.cos(yaw) * Math.cos(pitch) * zSpeed;
        Vec3 pos = one.lerp(two, RANDOM.nextDouble());
        level.addParticle(getParticleOptions(), forceSpawn, pos.x, pos.y, pos.z, xMotion, yMotion, zMotion);
        return this;
    }

    @Override
    public WorldParticleBuilder<T> modifyData(Supplier<GenericParticleData> dataType, Consumer<GenericParticleData> dataConsumer) {
        return (WorldParticleBuilder<T>) super.modifyData(dataType, dataConsumer);
    }

    @Override
    public WorldParticleBuilder<T> modifyData(Function<AbstractParticleBuilder<T>, GenericParticleData> dataType, Consumer<GenericParticleData> dataConsumer) {
        return (WorldParticleBuilder<T>) super.modifyData(dataType, dataConsumer);
    }

    @Override
    public WorldParticleBuilder<T> modifyColorData(Consumer<ColorParticleData> dataConsumer) {
        return (WorldParticleBuilder<T>) super.modifyColorData(dataConsumer);
    }

    @Override
    public WorldParticleBuilder<T> setColorData(ColorParticleData colorData) {
        return (WorldParticleBuilder<T>) super.setColorData(colorData);
    }

    @Override
    public WorldParticleBuilder<T> setScaleData(GenericParticleData scaleData) {
        return (WorldParticleBuilder<T>) super.setScaleData(scaleData);
    }

    @Override
    public WorldParticleBuilder<T> setTransparencyData(GenericParticleData transparencyData) {
        return (WorldParticleBuilder<T>) super.setTransparencyData(transparencyData);
    }

    @Override
    public WorldParticleBuilder<T> setSpinData(SpinParticleData spinData) {
        return (WorldParticleBuilder<T>) super.setSpinData(spinData);
    }

    @Override
    public WorldParticleBuilder<T> multiplyGravity(float gravityMultiplier) {
        return (WorldParticleBuilder<T>) super.multiplyGravity(gravityMultiplier);
    }

    @Override
    public WorldParticleBuilder<T> modifyGravity(Function<Float, Supplier<Float>> gravityReplacement) {
        return (WorldParticleBuilder<T>) super.modifyGravity(gravityReplacement);
    }

    @Override
    public WorldParticleBuilder<T> setGravityStrength(float gravity) {
        return (WorldParticleBuilder<T>) super.setGravityStrength(gravity);
    }

    @Override
    public WorldParticleBuilder<T> setGravityStrength(Supplier<Float> gravityStrengthSupplier) {
        return (WorldParticleBuilder<T>) super.setGravityStrength(gravityStrengthSupplier);
    }

    @Override
    public WorldParticleBuilder<T> multiplyLifetime(float lifetimeMultiplier) {
        return (WorldParticleBuilder<T>) super.multiplyLifetime(lifetimeMultiplier);
    }

    @Override
    public WorldParticleBuilder<T> modifyLifetime(Function<Integer, Supplier<Integer>> lifetimeReplacement) {
        return (WorldParticleBuilder<T>) super.modifyLifetime(lifetimeReplacement);
    }

    @Override
    public WorldParticleBuilder<T> setLifetime(int lifetime) {
        return (WorldParticleBuilder<T>) super.setLifetime(lifetime);
    }

    @Override
    public WorldParticleBuilder<T> setLifetime(Supplier<Integer> lifetimeSupplier) {
        return (WorldParticleBuilder<T>) super.setLifetime(lifetimeSupplier);
    }

    @Override
    public WorldParticleBuilder<T> multiplyLifeDelay(float lifeDelayMultiplier) {
        return (WorldParticleBuilder<T>) super.multiplyLifeDelay(lifeDelayMultiplier);
    }

    @Override
    public WorldParticleBuilder<T> modifyLifeDelay(Function<Integer, Supplier<Integer>> lifeDelayReplacement) {
        return (WorldParticleBuilder<T>) super.modifyLifeDelay(lifeDelayReplacement);
    }

    @Override
    public WorldParticleBuilder<T> setLifeDelay(int lifeDelay) {
        return (WorldParticleBuilder<T>) super.setLifeDelay(lifeDelay);
    }

    @Override
    public WorldParticleBuilder<T> setLifeDelay(Supplier<Integer> lifeDelaySupplier) {
        return (WorldParticleBuilder<T>) super.setLifeDelay(lifeDelaySupplier);
    }

    @Override
    public WorldParticleBuilder<T> setSpritePicker(SimpleParticleOptions.ParticleSpritePicker spritePicker) {
        return (WorldParticleBuilder<T>) super.setSpritePicker(spritePicker);
    }

    @Override
    public WorldParticleBuilder<T> setDiscardFunction(SimpleParticleOptions.ParticleDiscardFunctionType discardFunctionType) {
        return (WorldParticleBuilder<T>) super.setDiscardFunction(discardFunctionType);
    }
}