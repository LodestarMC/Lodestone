package team.lodestar.lodestone.systems.worldgen;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.lodestone.helpers.BlockHelper;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class LodestoneBlockFiller extends ArrayList<LodestoneBlockFiller.LodestoneBlockFillerLayer> {

    public LodestoneBlockFiller addLayer(LodestoneBlockFillerLayer layer) {
        add(layer);
        return this;
    }

    public LodestoneBlockFillerLayer getLayer(LodestoneLayerToken layerToken) {
        return stream().filter(l -> l.layerToken.equals(layerToken)).findFirst().orElseThrow();
    }

    public LodestoneBlockFillerLayer fill(LevelAccessor level) {
        while (size() > 1) {
            mergeLayers(get(size() - 2), get(size() - 1));
        }
        var mainLayer = get(0);
        var discarded = mainLayer.entrySet().stream().filter(entry -> entry.getValue().tryDiscard(level, entry.getKey())).map(Map.Entry::getValue).collect(Collectors.toCollection(ArrayList::new));

        mainLayer.forEach((pos, blockStateEntry) -> {
            if (!discarded.contains(blockStateEntry)) {
                blockStateEntry.place(level, pos);
            }
        });
        return mainLayer;
    }

    protected void mergeLayers(LodestoneBlockFillerLayer toLayer, LodestoneBlockFillerLayer fromLayer) {
        remove(fromLayer);
        fromLayer.mergingStrategy.mergingFunction.accept(toLayer, fromLayer);
    }

    public enum MergingStrategy {
        REPLACE(HashMap::putAll),
        ADD((to, from) -> from.forEach(to::putIfAbsent));

        public final BiConsumer<LodestoneBlockFiller.LodestoneBlockFillerLayer, LodestoneBlockFiller.LodestoneBlockFillerLayer> mergingFunction;

        MergingStrategy(BiConsumer<LodestoneBlockFiller.LodestoneBlockFillerLayer, LodestoneBlockFiller.LodestoneBlockFillerLayer> mergingFunction) {
            this.mergingFunction = mergingFunction;
        }
    }

    public static class LodestoneLayerToken {
        public final UUID index;

        public LodestoneLayerToken(UUID index) {
            this.index = index;
        }

        public LodestoneLayerToken() {
            this(UUID.randomUUID());
        }
    }

    public static class LodestoneBlockFillerLayer extends HashMap<BlockPos, LodestoneBlockFiller.BlockStateEntry> {

        public final MergingStrategy mergingStrategy;
        public final LodestoneLayerToken layerToken;

        public LodestoneBlockFillerLayer(LodestoneLayerToken layerToken, MergingStrategy mergingStrategy) {
            this.layerToken = layerToken;
            this.mergingStrategy = mergingStrategy;
        }

        public void fill(LevelAccessor level) {
            forEach((pos, entry) -> {
                if (entry.canPlace(level, pos)) {
                    entry.place(level, pos);
                }
            });
        }

        public void replace(BlockPos pos, Function<BlockStateEntry, BlockStateEntry> entryFunction) {
            replace(pos, entryFunction.apply(get(pos)));
        }
    }

    public static class BlockStateEntry {

        private final BlockState state;
        private final EntryDiscardPredicate discardPredicate;

        public BlockStateEntry(BlockState state, EntryDiscardPredicate discardPredicate) {
            this.state = state;
            this.discardPredicate = discardPredicate;
        }

        public BlockStateEntry(BlockState state) {
            this(state, null);
        }

        public BlockState getState() {
            return state;
        }

        public boolean tryDiscard(LevelAccessor level, BlockPos pos) {
            return discardPredicate != null && discardPredicate.shouldDiscard(level, pos, state);
        }

        public boolean canPlace(LevelAccessor level, BlockPos pos) {
            if (level.isOutsideBuildHeight(pos)) {
                return false;
            }
            BlockState state = level.getBlockState(pos);
            return level.isEmptyBlock(pos) || state.canBeReplaced();
        }

        public void place(LevelAccessor level, BlockPos pos) {
            level.setBlock(pos, getState(), 19);
            if (level instanceof Level realLevel) {
                BlockHelper.updateState(realLevel, pos);
            }
        }
    }

    public interface EntryDiscardPredicate {
        boolean shouldDiscard(LevelAccessor level, BlockPos pos, BlockState state);
    }
}