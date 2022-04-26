package com.sammy.ortus.systems.worldgen;

import com.sammy.ortus.helpers.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.function.Function;

public class OrtusBlockFiller {
    public ArrayList<BlockStateEntry> entries = new ArrayList<>();
    public final boolean careful;

    public OrtusBlockFiller(boolean careful) {
        this.careful = careful;
    }

    public void fill(WorldGenLevel level) {
        for (BlockStateEntry entry : entries) {
            if (careful && !entry.canPlace(level)) {
                continue;
            }
            entry.place(level);
        }
    }

    public void replace(int index, Function<BlockStateEntry, BlockStateEntry> function) {
        entries.set(index, function.apply(entries.get(index)));
    }

    public static class BlockStateEntry {
        public BlockState state;
        public final BlockPos pos;

        public BlockStateEntry(BlockState state, BlockPos pos) {
            this.state = state;
            this.pos = pos;
        }

        public BlockStateEntry replaceState(BlockState state) {
            this.state = state;
            return this;
        }
        
        public boolean canPlace(WorldGenLevel level) {
            return canPlace(level, pos);
        }

        public boolean canPlace(WorldGenLevel level, BlockPos pos) {
            if (level.isOutsideBuildHeight(pos)) {
                return false;
            }
            BlockState state = level.getBlockState(pos);
            return level.isEmptyBlock(pos) || state.getMaterial().isReplaceable();
        }

        public void place(WorldGenLevel level) {
            level.setBlock(pos, state, 19);
            if (level instanceof Level) {
                BlockHelper.updateState((Level) level, pos);
            }
        }
    }
}