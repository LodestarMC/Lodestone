package team.lodestar.lodestone.systems.particle.builder;

import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3d;
import org.joml.Vector3f;
import team.lodestar.lodestone.helpers.BlockHelper;
import team.lodestar.lodestone.systems.particle.LodestoneWorldParticleActor;
import team.lodestar.lodestone.systems.particle.options.AbstractWorldParticleOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings({"unused"})
public abstract class AbstractWorldParticleBuilder<T extends AbstractWorldParticleBuilder<T, Y>, Y extends AbstractWorldParticleOptions> extends AbstractParticleBuilder<T, Y> {

    private static final Random RANDOM = new Random();

    final ParticleType<Y> type;

    double zMotion = 0;
    double maxZSpeed = 0;
    double maxZOffset = 0;

    protected AbstractWorldParticleBuilder(ParticleType<Y> type) {
        this.type = type;
    }

    public T enableNoClip() {
        return setNoClip(true);
    }

    public T disableNoClip() {
        return setNoClip(false);
    }

    public T setNoClip(boolean noClip) {
        getParticleOptions().noClip = noClip;
        return wrapper();
    }

    public T setRenderType(ParticleRenderType renderType) {
        getParticleOptions().renderType = renderType;
        return wrapper();
    }

    public T setRandomMotion(double maxSpeed) {
        return setRandomMotion(maxSpeed, maxSpeed, maxSpeed);
    }

    public T setRandomMotion(double maxHSpeed, double maxVSpeed) {
        return setRandomMotion(maxHSpeed, maxVSpeed, maxHSpeed);
    }

    public T setRandomMotion(double maxXSpeed, double maxYSpeed, double maxZSpeed) {
        this.maxXSpeed = maxXSpeed;
        this.maxYSpeed = maxYSpeed;
        this.maxZSpeed = maxZSpeed;
        return wrapper();
    }

    public T addMotion(Vector3f motion) {
        return addMotion(motion.x(), motion.y(), motion.z());
    }

    public T addMotion(Vec3 motion) {
        return addMotion(motion.x, motion.y, motion.z);
    }

    public T addMotion(double vx, double vy, double vz) {
        this.xMotion += vx;
        this.yMotion += vy;
        this.zMotion += vz;
        return wrapper();
    }

    public T setMotion(Vector3f motion) {
        return setMotion(motion.x(), motion.y(), motion.z());
    }

    public T setMotion(Vec3 motion) {
        return setMotion(motion.x, motion.y, motion.z);
    }

    public T setMotion(double vx, double vy, double vz) {
        this.xMotion = vx;
        this.yMotion = vy;
        this.zMotion = vz;
        return wrapper();
    }

    public T setRandomOffset(double maxDistance) {
        return setRandomOffset(maxDistance, maxDistance, maxDistance);
    }

    public T setRandomOffset(double maxHDist, double maxVDist) {
        return setRandomOffset(maxHDist, maxVDist, maxHDist);
    }

    public T setRandomOffset(double maxXDist, double maxYDist, double maxZDist) {
        this.maxXOffset = maxXDist;
        this.maxYOffset = maxYDist;
        this.maxZOffset = maxZDist;
        return wrapper();
    }

    public T act(Consumer<T> particleBuilderConsumer) {
        particleBuilderConsumer.accept(wrapper());
        return wrapper();
    }

    public <K extends AbstractWorldParticleBuilder<K, Y>> T act(Class<K> type, Consumer<K> particleBuilderConsumer) {
        particleBuilderConsumer.accept(type.cast(wrapper()));
        return wrapper();
    }

    public T addActor(Consumer<LodestoneWorldParticleActor> particleActor) {
        getParticleOptions().actors.add(particleActor);
        return wrapper();
    }

    public T clearActors() {
        getParticleOptions().actors.clear();
        return wrapper();
    }

    public T spawn(Level level, double x, double y, double z) {
        double yaw = RANDOM.nextFloat() * Math.PI * 2, pitch = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xSpeed = RANDOM.nextFloat() * maxXSpeed, ySpeed = RANDOM.nextFloat() * maxYSpeed, zSpeed = RANDOM.nextFloat() * maxZSpeed;
        this.xMotion += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
        this.yMotion += Math.sin(pitch) * ySpeed;
        this.zMotion += Math.cos(yaw) * Math.cos(pitch) * zSpeed;
        double yaw2 = RANDOM.nextFloat() * Math.PI * 2, pitch2 = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xDist = RANDOM.nextFloat() * maxXOffset, yDist = RANDOM.nextFloat() * maxYOffset, zDist = RANDOM.nextFloat() * maxZOffset;
        double xPos = Math.sin(yaw2) * Math.cos(pitch2) * xDist;
        double yPos = Math.sin(pitch2) * yDist;
        double zPos = Math.cos(yaw2) * Math.cos(pitch2) * zDist;

        level.addParticle(getParticleOptions(), x + xPos, y + yPos, z + zPos, xMotion, yMotion, zMotion);
        return wrapper();
    }

    public T repeat(Level level, double x, double y, double z, int n) {
        for (int i = 0; i < n; i++) spawn(level, x, y, z);
        return wrapper();
    }

    public T surroundBlock(Level level, BlockPos pos, Direction... directions) {
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

            level.addParticle(getParticleOptions(), pos.getX() + xPos, pos.getY() + yPos, pos.getZ() + zPos, xMotion, yMotion, zMotion);

        }
        return wrapper();
    }

    public T repeatSurroundBlock(Level level, BlockPos pos, int n) {
        for (int i = 0; i < n; i++) surroundBlock(level, pos);
        return wrapper();
    }

    public T repeatSurroundBlock(Level level, BlockPos pos, int n, Direction... directions) {
        for (int i = 0; i < n; i++) surroundBlock(level, pos, directions);
        return wrapper();
    }

    public T surroundVoxelShape(Level level, BlockPos pos, VoxelShape voxelShape, int max) {
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

    public T surroundVoxelShape(Level level, BlockPos pos, BlockState state, int max) {
        VoxelShape voxelShape = state.getShape(level, pos);
        if (voxelShape.isEmpty()) {
            voxelShape = Shapes.block();
        }
        return surroundVoxelShape(level, pos, voxelShape, max);
    }

    public T spawnAtRandomFace(Level level, BlockPos pos) {
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

        level.addParticle(getParticleOptions(), pos.getX() + xPos, pos.getY() + yPos, pos.getZ() + zPos, xMotion, yMotion, zMotion);
        return wrapper();
    }

    public T repeatRandomFace(Level level, BlockPos pos, int n) {
        for (int i = 0; i < n; i++) spawnAtRandomFace(level, pos);
        return wrapper();
    }

    public T createCircle(Level level, double x, double y, double z, double distance, double currentCount, double totalCount) {
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
        level.addParticle(getParticleOptions(), x + xPos + dx2, y + yPos, z + zPos + dz2, xMotion, ySpeed, zMotion);
        return wrapper();
    }

    public T repeatCircle(Level level, double x, double y, double z, double distance, int times) {
        for (int i = 0; i < times; i++) createCircle(level, x, y, z, distance, i, times);
        return wrapper();
    }

    public T createBlockOutline(Level level, BlockPos pos, BlockState state) {
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

    public T spawnLine(Level level, Vec3 one, Vec3 two) {
        double yaw = RANDOM.nextFloat() * Math.PI * 2, pitch = RANDOM.nextFloat() * Math.PI - Math.PI / 2, xSpeed = RANDOM.nextFloat() * maxXSpeed, ySpeed = RANDOM.nextFloat() * maxYSpeed, zSpeed = RANDOM.nextFloat() * maxZSpeed;
        this.xMotion += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
        this.yMotion += Math.sin(pitch) * ySpeed;
        this.zMotion += Math.cos(yaw) * Math.cos(pitch) * zSpeed;
        Vec3 pos = one.lerp(two, RANDOM.nextDouble());
        level.addParticle(getParticleOptions(), pos.x, pos.y, pos.z, xMotion, yMotion, zMotion);
        return wrapper();
    }
}