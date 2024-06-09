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

public class WorldParticleBuilder extends AbstractParticleBuilder<WorldParticleOptions> {

    private static final Random RANDOM = new Random();

    final WorldParticleOptions options;

    boolean forceSpawn = false;
    double zMotion = 0;
    double maxZSpeed = 0;
    double maxZOffset = 0;

    public static WorldParticleBuilder create(ParticleType<WorldParticleOptions> particle) {
        return create(particle, null);
    }
    public static WorldParticleBuilder create(ParticleType<WorldParticleOptions> particle, LodestoneParticleBehavior behavior) {
        return create(new WorldParticleOptions(particle, behavior));
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

    public WorldParticleBuilder setRenderTarget(RenderHandler.LodestoneRenderLayer renderLayer) {
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

    public WorldParticleBuilder enableCull() {
        return setShouldCull(true);
    }

    public WorldParticleBuilder disableCull() {
        return setShouldCull(false);
    }

    public WorldParticleBuilder setShouldCull(boolean shouldCull) {
        getParticleOptions().shouldCull = shouldCull;
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
        return clearTickActor().clearSpawnActors().clearRenderActors();
    }

    public WorldParticleBuilder clearTickActor() {
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

    public WorldParticleBuilder createCircle(Level level, double x, double y, double z, double distance, double currentCount, double totalCount) {
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

    public WorldParticleBuilder repeatCircle(Level level, double x, double y, double z, double distance, int times) {
        for (int i = 0; i < times; i++) createCircle(level, x, y, z, distance, i, times);
        return this;
    }

    public WorldParticleBuilder createBlockOutline(Level level, BlockPos pos, BlockState state) {
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
    public WorldParticleBuilder modifyData(Supplier<GenericParticleData> dataType, Consumer<GenericParticleData> dataConsumer) {
        return (WorldParticleBuilder) super.modifyData(dataType, dataConsumer);
    }

    @Override
    public WorldParticleBuilder modifyColorData(Consumer<ColorParticleData> dataConsumer) {
        return (WorldParticleBuilder) super.modifyColorData(dataConsumer);
    }

    @Override
    public WorldParticleBuilder setColorData(ColorParticleData colorData) {
        return (WorldParticleBuilder) super.setColorData(colorData);
    }

    @Override
    public WorldParticleBuilder setScaleData(GenericParticleData scaleData) {
        return (WorldParticleBuilder) super.setScaleData(scaleData);
    }

    @Override
    public WorldParticleBuilder setTransparencyData(GenericParticleData transparencyData) {
        return (WorldParticleBuilder) super.setTransparencyData(transparencyData);
    }

    @Override
    public WorldParticleBuilder setSpinData(SpinParticleData spinData) {
        return (WorldParticleBuilder) super.setSpinData(spinData);
    }

    @Override
    public WorldParticleBuilder multiplyGravity(float gravityMultiplier) {
        return (WorldParticleBuilder) super.multiplyGravity(gravityMultiplier);
    }

    @Override
    public WorldParticleBuilder modifyGravity(Function<Float, Supplier<Float>> gravityReplacement) {
        return (WorldParticleBuilder) super.modifyGravity(gravityReplacement);
    }

    @Override
    public WorldParticleBuilder setGravityStrength(float gravity) {
        return (WorldParticleBuilder) super.setGravityStrength(gravity);
    }

    @Override
    public WorldParticleBuilder setGravityStrength(Supplier<Float> gravityStrengthSupplier) {
        return (WorldParticleBuilder) super.setGravityStrength(gravityStrengthSupplier);
    }

    @Override
    public WorldParticleBuilder multiplyLifetime(float lifetimeMultiplier) {
        return (WorldParticleBuilder) super.multiplyLifetime(lifetimeMultiplier);
    }

    @Override
    public WorldParticleBuilder modifyLifetime(Function<Integer, Supplier<Integer>> lifetimeReplacement) {
        return (WorldParticleBuilder) super.modifyLifetime(lifetimeReplacement);
    }

    @Override
    public WorldParticleBuilder setLifetime(int lifetime) {
        return (WorldParticleBuilder) super.setLifetime(lifetime);
    }

    @Override
    public WorldParticleBuilder setLifetime(Supplier<Integer> lifetimeSupplier) {
        return (WorldParticleBuilder) super.setLifetime(lifetimeSupplier);
    }

    @Override
    public WorldParticleBuilder multiplyLifeDelay(float lifeDelayMultiplier) {
        return (WorldParticleBuilder) super.multiplyLifeDelay(lifeDelayMultiplier);
    }

    @Override
    public WorldParticleBuilder modifyLifeDelay(Function<Integer, Supplier<Integer>> lifeDelayReplacement) {
        return (WorldParticleBuilder) super.modifyLifeDelay(lifeDelayReplacement);
    }

    @Override
    public WorldParticleBuilder setLifeDelay(int lifeDelay) {
        return (WorldParticleBuilder) super.setLifeDelay(lifeDelay);
    }

    @Override
    public WorldParticleBuilder setLifeDelay(Supplier<Integer> lifeDelaySupplier) {
        return (WorldParticleBuilder) super.setLifeDelay(lifeDelaySupplier);
    }

    @Override
    public WorldParticleBuilder setSpritePicker(SimpleParticleOptions.ParticleSpritePicker spritePicker) {
        return (WorldParticleBuilder) super.setSpritePicker(spritePicker);
    }

    @Override
    public WorldParticleBuilder setDiscardFunction(SimpleParticleOptions.ParticleDiscardFunctionType discardFunctionType) {
        return (WorldParticleBuilder) super.setDiscardFunction(discardFunctionType);
    }
}