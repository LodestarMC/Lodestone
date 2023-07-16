package team.lodestar.lodestone.helpers.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

/**
 * A collection of various helper methods related to blockstates
 */
@SuppressWarnings("unused")
public class BlockStateHelper {

    /**
     * Copies all properties from oldState to newState, given that an individual property exists on the newState.
     *
     * @param oldState - State to copy properties from.
     * @param newState - State to apply copied property values to.
     * @return - NewState with adjusted properties.
     */
    public static BlockState getBlockStateWithExistingProperties(BlockState oldState, BlockState newState) {
        BlockState finalState = newState;
        for (Property<?> property : oldState.getProperties()) {
            if (newState.hasProperty(property)) {
                finalState = newStateWithOldProperty(oldState, finalState, property);
            }
        }
        return finalState;
    }

    /**
     * Copies BlockState properties from a BlockState already in the world, and replaces it with a newState with matching property values.
     */
    public static BlockState setBlockStateWithExistingProperties(Level level, BlockPos pos, BlockState newState, int flags) {
        BlockState oldState = level.getBlockState(pos);
        BlockState finalState = getBlockStateWithExistingProperties(oldState, newState);
        level.sendBlockUpdated(pos, oldState, finalState, flags);
        level.setBlock(pos, finalState, flags);
        return finalState;
    }

    /**
     * Copies BlockState properties from a BlockState already in the world, and returns a newState with matching property values.
     */
    public static <T extends Comparable<T>> BlockState newStateWithOldProperty(BlockState oldState, BlockState newState, Property<T> property) {
        return newState.setValue(property, oldState.getValue(property));
    }

    /**
     * Updates a blockstate at a given position with the client. Also syncs block entities.
     */
    public static void updateState(Level level, BlockPos pos) {
        updateState(level.getBlockState(pos), level, pos);
    }

    /**
     * Updates a blockstate at a given position with the client. Also syncs block entities.
     */
    public static void updateState(BlockState state, Level level, BlockPos pos) {
        level.sendBlockUpdated(pos, state, state, 2);
        level.blockEntityChanged(pos);
    }

    /**
     * Updates a blockstate at a given position with the client and notifies its neighbours. Also syncs block entities.
     */
    public static void updateAndNotifyState(Level level, BlockPos pos) {
        updateAndNotifyState(level.getBlockState(pos), level, pos);
    }

    /**
     * Updates a blockstate at a given position with the client and notifies its neighbours. Also syncs block entities.
     */
    public static void updateAndNotifyState(BlockState state, Level level, BlockPos pos) {
        updateState(state, level, pos);
        state.updateNeighbourShapes(level, pos, 2);
        level.updateNeighbourForOutputSignal(pos, state.getBlock());
    }
}