package team.lodestar.lodestone.systems.worldgen;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import team.lodestar.lodestone.helpers.BlockHelper;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LodestoneBlockFiller extends ArrayList<LodestoneBlockFiller.LodestoneBlockFillerLayer> {

    public static final LodestoneLayerToken MAIN = new LodestoneLayerToken();

    protected final LodestoneBlockFillerLayer mainLayer;

    public LodestoneBlockFiller() {
        mainLayer = new LodestoneBlockFillerLayer(MAIN);
    }

    public LodestoneBlockFiller(LodestoneBlockFillerLayer... layers) {
        this(new ArrayList<>(List.of(layers)));
    }

    public LodestoneBlockFiller(Collection<LodestoneBlockFillerLayer> layers) {
        this();
        addAll(layers);
    }

    public LodestoneBlockFiller addLayers(LodestoneLayerToken... tokens) {
        for (LodestoneLayerToken token : tokens) {
            addLayers(new LodestoneBlockFillerLayer(token));
        }
        return this;
    }

    public LodestoneBlockFiller addLayers(LodestoneBlockFillerLayer... layers) {
        addAll(List.of(layers));
        return this;
    }

    public LodestoneBlockFillerLayer getLayer(@NotNull LodestoneLayerToken layerToken) {
        if (layerToken.equals(MAIN)) {
            return mainLayer;
        }
        return stream().filter(l -> l.layerToken.equals(layerToken)).findFirst().orElseThrow();
    }

    public LodestoneBlockFillerLayer getMainLayer() {
        return mainLayer;
    }

    public LodestoneBlockFillerLayer fill(LevelAccessor level) {
        var mainLayer = getMainLayer();
        mainLayer.clear();
        for (int i = 0; i < size(); i++) {
            mergeLayers(mainLayer, get(i));
        }
        var discarded = mainLayer.entrySet().stream().filter(entry -> entry.getValue().tryDiscard(level, entry.getKey())).map(Map.Entry::getValue).collect(Collectors.toCollection(ArrayList::new));

        mainLayer.forEach((pos, blockStateEntry) -> {
            if (!discarded.contains(blockStateEntry)) {
                if (blockStateEntry.canPlace(level, pos)) {
                    blockStateEntry.place(level, pos);
                }
            }
        });
        return mainLayer;
    }

    protected void mergeLayers(LodestoneBlockFillerLayer toLayer, LodestoneBlockFillerLayer fromLayer) {
        fromLayer.forEach(toLayer::putIfAbsent);
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

        public final LodestoneLayerToken layerToken;

        public LodestoneBlockFillerLayer(LodestoneLayerToken layerToken) {
            this.layerToken = layerToken;
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

        public BlockStateEntry put(BlockPos key, BlockStateEntryBuilder value) {
            return super.put(key, value.build());
        }

        public BlockStateEntry putIfAbsent(BlockPos key, BlockStateEntryBuilder value) {
            return super.putIfAbsent(key, value.build());
        }
    }

    public interface EntryDiscardPredicate {
        boolean shouldDiscard(LevelAccessor level, BlockPos pos, BlockState state);
    }

    public interface EntryPlacementPredicate {
        boolean canPlace(LevelAccessor level, BlockPos pos, BlockState state);
    }

    public static BlockStateEntryBuilder create(BlockState state) {
        return new BlockStateEntryBuilder(state);
    }

    public static class BlockStateEntryBuilder {

        private final BlockState state;
        private EntryDiscardPredicate discardPredicate;
        private EntryPlacementPredicate placementPredicate;
        private boolean forcePlace;

        public BlockStateEntryBuilder(BlockState state) {
            this.state = state;
        }

        public BlockStateEntryBuilder setDiscardPredicate(EntryDiscardPredicate discardPredicate) {
            this.discardPredicate = discardPredicate;
            return this;
        }

        public BlockStateEntryBuilder setPlacementPredicate(EntryPlacementPredicate placementPredicate) {
            this.placementPredicate = placementPredicate;
            return this;
        }

        public BlockStateEntryBuilder setForcePlace() {
            return setForcePlace(true);
        }

        public BlockStateEntryBuilder setForcePlace(boolean forcePlace) {
            this.forcePlace = forcePlace;
            return this;
        }

        public BlockStateEntry build() {
            return new BlockStateEntry(state, discardPredicate, placementPredicate, forcePlace);
        }
    }

    public static class BlockStateEntry {
        private final BlockState state;
        private final EntryDiscardPredicate discardPredicate;
        private final EntryPlacementPredicate placementPredicate;
        private final boolean forcePlace;

        private BlockStateEntry(BlockState state, EntryDiscardPredicate discardPredicate, EntryPlacementPredicate placementPredicate, boolean forcePlace) {
            this.state = state;
            this.discardPredicate = discardPredicate;
            this.placementPredicate = placementPredicate;
            this.forcePlace = forcePlace;
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
            if (placementPredicate != null && !placementPredicate.canPlace(level, pos, state)) {
                return false;
            }
            if (forcePlace) {
                return true;
            }
            return level.isEmptyBlock(pos) || state.canBeReplaced();
        }

        public void place(LevelAccessor level, BlockPos pos) {
            level.setBlock(pos, getState(), 19);
            if (level instanceof Level realLevel) {
                BlockHelper.updateState(realLevel, pos);
            }
        }
    }
}