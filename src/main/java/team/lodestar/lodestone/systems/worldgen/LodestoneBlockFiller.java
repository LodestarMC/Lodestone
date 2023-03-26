package team.lodestar.lodestone.systems.worldgen;

import team.lodestar.lodestone.helpers.block.BlockStateHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.function.Function;

public class LodestoneBlockFiller {
    protected final HashMap<BlockPos, BlockStateEntry> entries = new HashMap<>();
    protected final boolean careful;

    public LodestoneBlockFiller(boolean careful) {
        this.careful = careful;
    }

    public void fill(LevelAccessor level) {
        getEntries().forEach((pos, entry) -> {
            if (!isCareful() || entry.canPlace(level, pos)) {
                entry.place(level, pos);
            }
        });
    }

    public void replace(BlockPos pos, Function<BlockStateEntry, BlockStateEntry> entryFunction) {
        getEntries().replace(pos, entryFunction.apply(getEntries().get(pos)));
    }

    public HashMap<BlockPos, BlockStateEntry> getEntries() {
        return entries;
    }

    public boolean isCareful() {
        return careful;
    }

    @SuppressWarnings("ClassCanBeRecord")
    public static class BlockStateEntry {
        protected final BlockState state;

        public BlockStateEntry(BlockState state) {
            this.state = state;
        }

        public BlockState getState() {
            return state;
        }

        public boolean canPlace(LevelAccessor level, BlockPos pos) {
            if (level.isOutsideBuildHeight(pos)) {
                return false;
            }
            BlockState state = level.getBlockState(pos);
            return level.isEmptyBlock(pos) || state.getMaterial().isReplaceable();
        }

        public void place(LevelAccessor level, BlockPos pos) {
            level.setBlock(pos, state, 19);
            if (level instanceof Level) {
                BlockStateHelper.updateState((Level) level, pos);
            }
        }
    }
}