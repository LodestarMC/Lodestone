package team.lodestar.lodestone.systems.particle.builder;

import net.minecraft.client.particle.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.joml.*;
import team.lodestar.lodestone.handlers.*;
import team.lodestar.lodestone.helpers.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;
import team.lodestar.lodestone.systems.particle.world.options.*;

import java.lang.*;
import java.lang.Math;
import java.util.*;
import java.util.Random;
import java.util.function.*;

public class WorldParticleBuilder<T extends LodestoneParticleBehavior<T>, K extends WorldParticleOptions<T>, O extends WorldParticleBuilder<T, K, O>> extends AbstractParticleBuilder<O, K> {

    private static final Random RANDOM = new Random();

    final K options;

    boolean forceSpawn = false;
    double zMotion = 0;
    double maxZSpeed = 0;
    double maxZOffset = 0;

    public static<T extends LodestoneParticleBehavior<T>, K extends WorldParticleOptions<T>, O extends WorldParticleBuilder<T, K, O>> WorldParticleBuilder<T, K, O> create(K options) {
        return new WorldParticleBuilder<>(options);
    }

    protected WorldParticleBuilder(K options) {
        this.options = options;
    }

    @Override
    public K getParticleOptions() {
        return options;
    }

    public O enableNoClip() {
        return setNoClip(true);
    }

    public O disableNoClip() {
        return setNoClip(false);
    }

    public O setNoClip(boolean noClip) {
        getParticleOptions().noClip = noClip;
        return wrapper();
    }

    public O setRenderType(ParticleRenderType renderType) {
        getParticleOptions().renderType = renderType;
        return wrapper();
    }

    public O setRenderTarget(RenderHandler.LodestoneRenderLayer renderLayer) {
        getParticleOptions().renderLayer = renderLayer;
        return wrapper();
    }

    public O enableForcedSpawn() {
        return setForceSpawn(true);
    }

    public O disableForcedSpawn() {
        return setForceSpawn(false);
    }

    public O setForceSpawn(boolean forceSpawn) {
        this.forceSpawn = forceSpawn;
        return wrapper();
    }

    public O enableCull() {
        return setShouldCull(true);
    }

    public O disableCull() {
        return setShouldCull(false);
    }

    public O setShouldCull(boolean shouldCull) {
        getParticleOptions().shouldCull = shouldCull;
        return wrapper();
    }

    public O setRandomMotion(double maxSpeed) {
        return setRandomMotion(maxSpeed, maxSpeed, maxSpeed);
    }

    public O setRandomMotion(double maxHSpeed, double maxVSpeed) {
        return setRandomMotion(maxHSpeed, maxVSpeed, maxHSpeed);
    }

    public O setRandomMotion(double maxXSpeed, double maxYSpeed, double maxZSpeed) {
        this.maxXSpeed = maxXSpeed;
        this.maxYSpeed = maxYSpeed;
        this.maxZSpeed = maxZSpeed;
        return wrapper();
    }

    public O addMotion(Vector3f motion) {
        return addMotion(motion.x(), motion.y(), motion.z());
    }

    public O addMotion(Vec3 motion) {
        return addMotion(motion.x, motion.y, motion.z);
    }

    public O addMotion(double vx, double vy, double vz) {
        this.xMotion += vx;
        this.yMotion += vy;
        this.zMotion += vz;
        return wrapper();
    }

    public O setMotion(Vector3f motion) {
        return setMotion(motion.x(), motion.y(), motion.z());
    }

    public O setMotion(Vec3 motion) {
        return setMotion(motion.x, motion.y, motion.z);
    }

    public O setMotion(double vx, double vy, double vz) {
        this.xMotion = vx;
        this.yMotion = vy;
        this.zMotion = vz;
        return wrapper();
    }

    public O setRandomOffset(double maxDistance) {
        return setRandomOffset(maxDistance, maxDistance, maxDistance);
    }

    public O setRandomOffset(double maxHDist, double maxVDist) {
        return setRandomOffset(maxHDist, maxVDist, maxHDist);
    }

    public O setRandomOffset(double maxXDist, double maxYDist, double maxZDist) {
        this.maxXOffset = maxXDist;
        this.maxYOffset = maxYDist;
        this.maxZOffset = maxZDist;
        return wrapper();
    }

    public O act(Consumer<O> particleBuilderConsumer) {
        particleBuilderConsumer.accept(wrapper());
        return wrapper();
    }

    public O addTickActor(Consumer<LodestoneWorldParticle<T>> particleActor) {
        getParticleOptions().tickActors.add(particleActor);
        return wrapper();
    }
    public O addSpawnActor(Consumer<LodestoneWorldParticle<T>> particleActor) {
        getParticleOptions().spawnActors.add(particleActor);
        return wrapper();
    }
    public O addRenderActor(Consumer<LodestoneWorldParticle<T>> particleActor) {
        getParticleOptions().renderActors.add(particleActor);
        return wrapper();
    }

    public O clearActors() {
        return clearTickActor().clearSpawnActors().clearRenderActors();
    }

    public O clearTickActor() {
        getParticleOptions().tickActors.clear();
        return wrapper();
    }
    public O clearSpawnActors() {
        getParticleOptions().spawnActors.clear();
        return wrapper();
    }
    public O clearRenderActors() {
        getParticleOptions().renderActors.clear();
        return wrapper();
    }

    public O spawn(Level level, double x, double y, double z) {
        double yaw = RANDOM.nextFloat() * Math.PI * 2, pitch = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xSpeed = RANDOM.nextFloat() * maxXSpeed, ySpeed = RANDOM.nextFloat() * maxYSpeed, zSpeed = RANDOM.nextFloat() * maxZSpeed;
        this.xMotion += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
        this.yMotion += Math.sin(pitch) * ySpeed;
        this.zMotion += Math.cos(yaw) * Math.cos(pitch) * zSpeed;
        double yaw2 = RANDOM.nextFloat() * Math.PI * 2, pitch2 = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xDist = RANDOM.nextFloat() * maxXOffset, yDist = RANDOM.nextFloat() * maxYOffset, zDist = RANDOM.nextFloat() * maxZOffset;
        double xPos = Math.sin(yaw2) * Math.cos(pitch2) * xDist;
        double yPos = Math.sin(pitch2) * yDist;
        double zPos = Math.cos(yaw2) * Math.cos(pitch2) * zDist;

        level.addParticle(getParticleOptions(), forceSpawn, x + xPos, y + yPos, z + zPos, xMotion, yMotion, zMotion);
        return wrapper();
    }

    public O repeat(Level level, double x, double y, double z, int n) {
        for (int i = 0; i < n; i++) spawn(level, x, y, z);
        return wrapper();
    }

    public O surroundBlock(Level level, BlockPos pos, Direction... directions) {
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
        return wrapper();
    }

    public O repeatSurroundBlock(Level level, BlockPos pos, int n) {
        for (int i = 0; i < n; i++) surroundBlock(level, pos);
        return wrapper();
    }

    public O repeatSurroundBlock(Level level, BlockPos pos, int n, Direction... directions) {
        for (int i = 0; i < n; i++) surroundBlock(level, pos, directions);
        return wrapper();
    }

    public O surroundVoxelShape(Level level, BlockPos pos, VoxelShape voxelShape, int max) {
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
        return wrapper();
    }

    public O surroundVoxelShape(Level level, BlockPos pos, BlockState state, int max) {
        VoxelShape voxelShape = state.getShape(level, pos);
        if (voxelShape.isEmpty()) {
            voxelShape = Shapes.block();
        }
        return surroundVoxelShape(level, pos, voxelShape, max);
    }

    public O spawnAtRandomFace(Level level, BlockPos pos) {
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
        return wrapper();
    }

    public O repeatRandomFace(Level level, BlockPos pos, int n) {
        for (int i = 0; i < n; i++) spawnAtRandomFace(level, pos);
        return wrapper();
    }

    public O createCircle(Level level, double x, double y, double z, double distance, double currentCount, double totalCount) {
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
        return wrapper();
    }

    public O repeatCircle(Level level, double x, double y, double z, double distance, int times) {
        for (int i = 0; i < times; i++) createCircle(level, x, y, z, distance, i, times);
        return wrapper();
    }

    public O createBlockOutline(Level level, BlockPos pos, BlockState state) {
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
        return wrapper();
    }

    public O spawnLine(Level level, Vec3 one, Vec3 two) {
        double yaw = RANDOM.nextFloat() * Math.PI * 2, pitch = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xSpeed = RANDOM.nextFloat() * maxXSpeed, ySpeed = RANDOM.nextFloat() * maxYSpeed, zSpeed = RANDOM.nextFloat() * maxZSpeed;
        this.xMotion += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
        this.yMotion += Math.sin(pitch) * ySpeed;
        this.zMotion += Math.cos(yaw) * Math.cos(pitch) * zSpeed;
        Vec3 pos = one.lerp(two, RANDOM.nextDouble());
        level.addParticle(getParticleOptions(), forceSpawn, pos.x, pos.y, pos.z, xMotion, yMotion, zMotion);
        return wrapper();
    }
}