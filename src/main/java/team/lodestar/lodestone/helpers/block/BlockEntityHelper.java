package team.lodestar.lodestone.helpers.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A collection of various helper methods related to block entities
 */
@SuppressWarnings("unused")
public class BlockEntityHelper {
    /**
     * Returns a list of block entities within a range, with a predicate for conditional checks.
     */
    public static <T> Collection<T> getBlockEntities(Class<T> type, Level level, BlockPos pos, int range, Predicate<T> predicate) {
        return getBlockEntitiesStream(type, level, pos, range, predicate).collect(Collectors.toSet());
    }

    /**
     * Returns a list of block entities within a range, with a predicate for conditional checks, as stream
     */
    public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, Level level, BlockPos pos, int range, Predicate<T> predicate) {
        return getBlockEntitiesStream(type, level, pos, range, range, range, predicate);
    }

    /**
     * Returns a list of block entities within an XZ range, with a predicate for conditional checks.
     */
    public static <T> Collection<T> getBlockEntities(Class<T> type, Level level, BlockPos pos, int x, int z, Predicate<T> predicate) {
        return getBlockEntitiesStream(type, level, pos, x, z, predicate).collect(Collectors.toSet());
    }

    /**
     * Returns a list of block entities within an XZ range, with a predicate for conditional checks, as stream
     */
    public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, Level level, BlockPos pos, int x, int z, Predicate<T> predicate) {
        return getBlockEntitiesStream(type, level, pos, x, z).filter(predicate);
    }

    /**
     * Returns a list of block entities within an XYZ range, with a predicate for conditional checks.
     */
    public static <T> Collection<T> getBlockEntities(Class<T> type, Level level, BlockPos pos, int x, int y, int z, Predicate<T> predicate) {
        return getBlockEntitiesStream(type, level, pos, x, y, z, predicate).collect(Collectors.toSet());
    }

    /**
     * Returns a list of block entities within an XYZ range, with a predicate for conditional checks, as streamq
     */
    public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, Level level, BlockPos pos, int x, int y, int z, Predicate<T> predicate) {
        return getBlockEntitiesStream(type, level, pos, x, y, z).filter(predicate);
    }

    /**
     * Returns a list of block entities within a radius around a position.
     */
    public static <T> Collection<T> getBlockEntities(Class<T> type, Level level, BlockPos pos, int range) {
        return getBlockEntities(type, level, pos, range, range, range);
    }

    /**
     * Returns a list of block entities within a radius around a position, as stream
     *
     * @param type  - Class of the block entity to search for
     * @param level - Level to search in
     * @param pos   - Position to search around
     * @param range - Radius to search in
     * @return - Stream of block entities
     */
    public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, Level level, BlockPos pos, int range) {
        return getBlockEntitiesStream(type, level, pos, range, range, range);
    }

    /**
     * Returns a list of block entities within an XZ radius around a position.
     */
    public static <T> Collection<T> getBlockEntities(Class<T> type, Level level, BlockPos pos, int x, int z) {
        return getBlockEntitiesStream(type, level, pos, x, z).collect(Collectors.toSet());
    }

    /**
     * Returns a list of block entities within an XZ radius around a position, as stream
     *
     * @param type  - Class of the block entity to search for
     * @param level - Level to search in
     * @param pos   - Position to search around
     * @param x     - Radius to search in X
     * @param z     - Radius to search in Z
     * @return - Stream of block entities
     */
    public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, Level level, BlockPos pos, int x, int z) {
        return getBlockEntitiesStream(type, level, new AABB((double) pos.getX() - x, pos.getY(), (double) pos.getZ() - z, (double) pos.getX() + x, (double) pos.getY() + 1, (double) pos.getZ() + z));
    }

    /**
     * Returns a list of block entities within an XYZ radius around a position.
     */
    public static <T> Collection<T> getBlockEntities(Class<T> type, Level level, BlockPos pos, int x, int y, int z) {
        return getBlockEntitiesStream(type, level, pos, x, y, z).collect(Collectors.toSet());
    }

    /**
     * Returns a list of block entities within an XYZ radius around a position, as stream
     *
     * @param type  - Class of the block entity to search for
     * @param level - Level to search in
     * @param pos   - Position to search around
     * @param x     - Radius to search in X
     * @param y     - Radius to search in Y
     * @param z     - Radius to search in Z
     * @return - Stream of block entities
     */
    public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, Level level, BlockPos pos, int x, int y, int z) {
        return getBlockEntitiesStream(type, level, pos, -x, -y, -z, x, y, z);
    }

    /**
     * Returns a list of block entities within set coordinates.
     */
    public static <T> Collection<T> getBlockEntities(Class<T> type, Level level, BlockPos pos, int x1, int y1, int z1, int x2, int y2, int z2) {
        return getBlockEntitiesStream(type, level, pos, x1, y1, z1, x2, y2, z2).collect(Collectors.toSet());
    }

    /**
     * Returns a list of block entities within set coordinates, as stream
     */
    public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, Level level, BlockPos pos, int x1, int y1, int z1, int x2, int y2, int z2) {
        return getBlockEntitiesStream(type, level, new AABB((double) pos.getX() + 1.5f + x1, (double) pos.getY() + 1.5f + y1, (double) pos.getZ() + 1.5f + z1, (double) pos.getX() + 0.5f + x2, (double) pos.getY() + 0.5f + y2, (double) pos.getZ() + 0.5f + z2));
    }

    /**
     * Returns a list of block entities within an AABB.
     */
    public static <T> Collection<T> getBlockEntities(Class<T> type, Level world, AABB bb) {
        return getBlockEntitiesStream(type, world, bb).collect(Collectors.toSet());
    }

    /**
     * Returns a list of block entities within an AABB, as stream
     */
    public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, Level world, AABB bb) {

        // Int stream from min to max + 16 (to get all chunks), step 16
        return IntStream.iterate((int) Math.floor(bb.minX), i -> i < Math.ceil(bb.maxX) + 16, i -> i + 16)
                .boxed()
                .flatMap(i ->
                        // Int stream from min to max + 16 (to get all chunks), step 16
                        IntStream.iterate((int) Math.floor(bb.minZ), j -> j < Math.ceil(bb.maxZ) + 16, j -> j + 16)
                                .boxed().flatMap(j -> {
                                    // get chunk access
                                    ChunkAccess c = world.getChunk(new BlockPos(i, 0, j));
                                    // get block entities in chunk
                                    return c.getBlockEntitiesPos().stream();
                                })
                )
                .filter(p -> bb.contains(p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5))
                .map(world::getBlockEntity)
                .filter(type::isInstance)
                .map(type::cast);
    }
}
