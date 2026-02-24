package team.lodestar.lodestone.systems.datagen;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.data.loading.*;
import team.lodestar.lodestone.handlers.ThrowawayBlockDataHandler;
import team.lodestar.lodestone.systems.block.*;

import java.util.*;

/**
 * Various throwaway data stored in {@link ThrowawayBlockDataHandler#THROWAWAY_DATA_CACHE}, which is only ever instantiated during the data-generation process.
 */
@SuppressWarnings("UnusedReturnValue")
public class LodestoneDatagenBlockData {

    private static HashMap<LodestoneBlockProperties, LodestoneDatagenBlockData> DATAGEN_DATA_CACHE;

    public static final LodestoneDatagenBlockData EMPTY = new LodestoneDatagenBlockData();

    private final List<TagKey<Block>> tags = new ArrayList<>();
    public boolean noLootDatagen = false;

    public static LodestoneDatagenBlockData getDatagenData(LodestoneBlockProperties properties) {
        if (!DatagenModLoader.isRunningDataGen()) {
            throw new UnsupportedOperationException("Cannot access datagen data outside of datagen");
        }
        if (DATAGEN_DATA_CACHE == null) {
            DATAGEN_DATA_CACHE = new HashMap<>();
        }
        return LodestoneDatagenBlockData.DATAGEN_DATA_CACHE.computeIfAbsent(properties, p -> new LodestoneDatagenBlockData());
    }

    public LodestoneDatagenBlockData addTag(TagKey<Block> blockTagKey) {
        tags.add(blockTagKey);
        return this;
    }

    @SafeVarargs
    public final LodestoneDatagenBlockData addTags(TagKey<Block>... blockTagKeys) {
        tags.addAll(Arrays.asList(blockTagKeys));
        return this;
    }

    public List<TagKey<Block>> getTags() {
        return tags;
    }

    public LodestoneDatagenBlockData noLootDatagen() {
        noLootDatagen = true;
        return this;
    }

    public LodestoneDatagenBlockData needsPickaxe() {
        return addTag(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    public LodestoneDatagenBlockData needsAxe() {
        return addTag(BlockTags.MINEABLE_WITH_AXE);
    }

    public LodestoneDatagenBlockData needsShovel() {
        return addTag(BlockTags.MINEABLE_WITH_SHOVEL);
    }

    public LodestoneDatagenBlockData needsHoe() {
        return addTag(BlockTags.MINEABLE_WITH_HOE);
    }

    public LodestoneDatagenBlockData needsStone() {
        return addTag(BlockTags.NEEDS_STONE_TOOL);
    }

    public LodestoneDatagenBlockData needsIron() {
        return addTag(BlockTags.NEEDS_IRON_TOOL);
    }

    public LodestoneDatagenBlockData needsDiamond() {
        return addTag(BlockTags.NEEDS_DIAMOND_TOOL);
    }
}