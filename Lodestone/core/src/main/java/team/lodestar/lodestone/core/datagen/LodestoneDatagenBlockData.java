package team.lodestar.lodestone.core.datagen;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import team.lodestar.lodestone.core.block.LodestoneBlockProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("UnusedReturnValue")
@DatagenOnly
public class LodestoneDatagenBlockData {

    private static HashMap<LodestoneBlockProperties, LodestoneDatagenBlockData> DATAGEN_DATA_CACHE;

    private final List<TagKey<Block>> tags = new ArrayList<>();
    public boolean noLootDatagen = false;

    public static LodestoneDatagenBlockData getDatagenData(LodestoneBlockProperties properties) {
        if (!DatagenModLoader.isRunningDataGen()) {
            throw new UnsupportedOperationException("Cannot access datagen data outside of datagen");
        }
        if (DATAGEN_DATA_CACHE == null) {
            DATAGEN_DATA_CACHE = new HashMap<>();
        }
        return DATAGEN_DATA_CACHE.computeIfAbsent(properties, p -> new LodestoneDatagenBlockData());
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