package team.lodestar.lodestone.helpers.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A collection of various helper methods related to block positions
 */
public class BlockPosHelper {

    /**
     * Converts a block position into a Vec3 entry.
     *
     * @param pos the block position
     * @return the vec3 representation.
     */
    public static Vec3 fromBlockPos(BlockPos pos) {
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Generates a randomly picked position within a block
     * @param rand an instance of random
     * @param pos the position the block is centered around
     * @return The randomized vector position
     */
    public static Vec3 withinBlock(RandomSource rand, BlockPos pos) {
        double x = pos.getX() + rand.nextDouble();
        double y = pos.getY() + rand.nextDouble();
        double z = pos.getZ() + rand.nextDouble();
        return new Vec3(x, y, z);
    }

    /**
     * Returns a list of block positions within a radius around a position, with a predicate for conditional checks.
     */
    public static Collection<BlockPos> getBlocks(BlockPos pos, int range, Predicate<BlockPos> predicate) {
        return getBlocksStream(pos, range, predicate).collect(Collectors.toSet());
    }

    /**
     * Returns a list of block positions within a radius around a position, with a predicate for conditional checks, as stream
     */
    public static Stream<BlockPos> getBlocksStream(BlockPos pos, int range, Predicate<BlockPos> predicate) {
        return getBlocksStream(pos, range, range, range, predicate);
    }

    /**
     * Returns a list of block positions within a XYZ radius around a position, with a predicate for conditional checks.
     */
    public static Collection<BlockPos> getBlocks(BlockPos pos, int x, int y, int z, Predicate<BlockPos> predicate) {
        return getBlocksStream(pos, x, y, z, predicate).collect(Collectors.toSet());
    }

    /**
     * Returns a list of block positions within a XYZ radius around a position, with a predicate for conditional checks, as stream
     */
    public static Stream<BlockPos> getBlocksStream(BlockPos pos, int x, int y, int z, Predicate<BlockPos> predicate) {
        return getBlocksStream(pos, x, y, z).filter(predicate);
    }

    /**
     * Returns a list of block positions within a XYZ radius around a position.
     */
    public static Collection<BlockPos> getBlocks(BlockPos pos, int x, int y, int z) {
        return getBlocksStream(pos, x, y, z).collect(Collectors.toSet());
    }

    /**
     * Returns a list of block positions within a XYZ radius around a position, as stream
     */
    public static Stream<BlockPos> getBlocksStream(BlockPos pos, int x, int y, int z) {
        return getBlocksStream(pos, -x, -y, -z, x, y, z);
    }

    /**
     * Returns a list of block positions within set coordinates.
     */
    public static Collection<BlockPos> getBlocks(BlockPos pos, int x1, int y1, int z1, int x2, int y2, int z2) {
        return getBlocksStream(pos, x1, y1, z1, x2, y2, z2).collect(Collectors.toSet());
    }

    /**
     * Returns a list of block positions within set coordinates, as stream
     */
    public static Stream<BlockPos> getBlocksStream(BlockPos pos, int x1, int y1, int z1, int x2, int y2, int z2) {
        return IntStream.rangeClosed(x1, x2)
                .boxed()
                .flatMap(i ->
                        IntStream.rangeClosed(y1, y2)
                                .boxed().flatMap(j ->
                                        IntStream.rangeClosed(z1, z2)
                                                .boxed().map(k ->
                                                        pos.offset(i, j, k)
                                                )
                                )
                );
    }

    /**
     * Returns a list of block positions within a radius around a position, with a predicate for conditional checks.
     */
    public static Collection<BlockPos> getPlaneOfBlocks(BlockPos pos, int range, Predicate<BlockPos> predicate) {
        return getPlaneOfBlocksStream(pos, range, predicate).collect(Collectors.toSet());
    }

    /**
     * Returns a list of block positions within a radius around a position, with a predicate for conditional checks, as stream
     */
    public static Stream<BlockPos> getPlaneOfBlocksStream(BlockPos pos, int range, Predicate<BlockPos> predicate) {
        return getPlaneOfBlocksStream(pos, range, range, predicate);
    }

    /**
     * Returns a list of block positions within a XYZ radius around a position, with a predicate for conditional checks.
     */
    public static Collection<BlockPos> getPlaneOfBlocks(BlockPos pos, int x, int z, Predicate<BlockPos> predicate) {
        return getPlaneOfBlocksStream(pos, x, z, predicate).collect(Collectors.toSet());
    }

    /**
     * Returns a list of block positions within a XZ radius around a position, with a predicate for conditional checks.
     */
    public static Stream<BlockPos> getPlaneOfBlocksStream(BlockPos pos, int x, int z, Predicate<BlockPos> predicate) {
        return getPlaneOfBlocksStream(pos, x, z).filter(predicate);
    }

    /**
     * Returns a list of block positions within a XZ radius around a position, with a predicate for conditional checks, as stream
     */
    public static Collection<BlockPos> getPlaneOfBlocks(BlockPos pos, int x, int z) {
        return getPlaneOfBlocksStream(pos, x, z).collect(Collectors.toSet());
    }

    /**
     * Returns a list of block positions within a XZ radius around a position, as stream
     */
    public static Stream<BlockPos> getPlaneOfBlocksStream(BlockPos pos, int x, int z) {
        return getPlaneOfBlocksStream(pos, -x, -z, x, z);
    }

    /**
     * Returns a list of block positions within set coordinates.
     */
    public static Collection<BlockPos> getPlaneOfBlocks(BlockPos pos, int x1, int z1, int x2, int z2) {
        return getPlaneOfBlocksStream(pos, x1, z1, x2, z2).collect(Collectors.toSet());
    }

    /**
     * Returns a list of block positions within set coordinates, as stream
     */
    public static Stream<BlockPos> getPlaneOfBlocksStream(BlockPos pos, int x1, int z1, int x2, int z2) {
        return IntStream.rangeClosed(x1, x2)
                .boxed()
                .flatMap(x ->
                        IntStream.rangeClosed(z1, z2)
                                .boxed().map(z ->
                                        pos.offset(x, 0, z)
                                )
                );
    }

    public static Collection<BlockPos> getSphereOfBlocks(BlockPos pos, float range, Predicate<BlockPos> predicate) {
        return getSphereOfBlocksStream(pos, range, predicate).collect(Collectors.toSet());
    }

    public static Stream<BlockPos> getSphereOfBlocksStream(BlockPos pos, float range, Predicate<BlockPos> predicate) {
        return getSphereOfBlocksStream(pos, range, range).filter(predicate);
    }

    public static Collection<BlockPos> getSphereOfBlocks(BlockPos pos, float width, float height, Predicate<BlockPos> predicate) {
        return getSphereOfBlocksStream(pos, width, height, predicate).collect(Collectors.toSet());
    }

    public static Stream<BlockPos> getSphereOfBlocksStream(BlockPos pos, float width, float height, Predicate<BlockPos> predicate) {
        return getSphereOfBlocksStream(pos, width, height).filter(predicate);
    }

    public static Collection<BlockPos> getSphereOfBlocks(BlockPos pos, float range) {
        return getSphereOfBlocksStream(pos, range).collect(Collectors.toSet());
    }

    public static Stream<BlockPos> getSphereOfBlocksStream(BlockPos pos, float range) {
        return getSphereOfBlocksStream(pos, range, range);
    }

    public static Collection<BlockPos> getSphereOfBlocks(BlockPos pos, float width, float height) {
        return getSphereOfBlocksStream(pos, width, height).collect(Collectors.toSet());
    }

    public static Stream<BlockPos> getSphereOfBlocksStream(BlockPos pos, float width, float height) {
        return IntStream.rangeClosed((int) -width, (int) width)
                .boxed()
                .flatMap(x ->
                        IntStream.rangeClosed((int) -height, (int) height)
                                .boxed().flatMap(y ->
                                        IntStream.rangeClosed((int) -width, (int) width)
                                                .boxed().filter(z -> {
                                                    double d = Math.sqrt(x * x + y * y + z * z);
                                                    return d <= width;
                                                }).map(z ->
                                                        pos.offset(x, y, z)
                                                )
                                )
                );
    }

    /**
     * Quick method to get all blocks neighboring a block.
     */
    public static Collection<BlockPos> getNeighboringBlocks(BlockPos current) {
        return getBlocks(current, -1, -1, -1, 1, 1, 1);
    }

    /**
     * Quick method to get all blocks neighboring a block, as stream.
     */
    public static Stream<BlockPos> getNeighboringBlocksStream(BlockPos current) {
        return getBlocksStream(current, -1, -1, -1, 1, 1, 1);
    }
}
