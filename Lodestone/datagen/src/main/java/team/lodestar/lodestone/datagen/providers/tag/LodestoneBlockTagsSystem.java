package team.lodestar.lodestone.datagen.providers.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import team.lodestar.lodestone.core.block.LodestoneBlockProperties;
import team.lodestar.lodestone.core.datagen.LodestoneDatagenBlockData;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public abstract class LodestoneBlockTagsSystem extends BlockTagsProvider {

    public LodestoneBlockTagsSystem(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    public void addTagsFromBlockProperties(Set<DeferredHolder<Block, ? extends Block>> blocks) {
        var blockList = sorted(blocks);
        for (Block block : blockList) {
            LodestoneBlockProperties properties = (LodestoneBlockProperties) block.properties();
            LodestoneDatagenBlockData data = properties.getDatagenData();
            for (TagKey<Block> tag : data.getTags()) {
                tag(tag).add(block);
            }
        }
    }

    public static List<? extends Block> sorted(Set<DeferredHolder<Block, ? extends Block>> blocks) {
        return blocks.stream().map(DeferredHolder::get).sorted(Comparator.comparingInt(BuiltInRegistries.BLOCK::getId)).toList();
    }
}
