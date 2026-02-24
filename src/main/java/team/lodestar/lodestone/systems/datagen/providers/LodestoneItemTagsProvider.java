package team.lodestar.lodestone.systems.datagen.providers;

import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.data.*;
import net.minecraft.data.tags.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.common.data.*;
import net.neoforged.neoforge.registries.*;
import org.jetbrains.annotations.*;
import team.lodestar.lodestone.systems.block.*;
import team.lodestar.lodestone.systems.datagen.*;
import team.lodestar.lodestone.systems.block.*;
import team.lodestar.lodestone.systems.datagen.*;

import java.util.*;
import java.util.concurrent.*;

public abstract class LodestoneItemTagsProvider extends ItemTagsProvider {

    public LodestoneItemTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, modId, existingFileHelper);
    }

    @Override
    public IntrinsicTagAppender<Item> tag(TagKey<Item> pTag) {
        return super.tag(pTag);
    }

    public void safeCopy(DeferredRegister<Block> blocks, TagKey<Item> itemTag) {
        safeCopy(blocks, TagKey.create(Registries.BLOCK, itemTag.location()), itemTag);
    }

    public void safeCopy(DeferredRegister<Block> blocks, TagKey<Block> blockTag, TagKey<Item> itemTag) {
        for (DeferredHolder<Block, ? extends Block> holder : blocks.getEntries()) {
            final Block block = holder.get();
            if (block.properties instanceof LodestoneBlockProperties lodestoneBlockProperties) {
                final LodestoneDatagenBlockData datagenData = lodestoneBlockProperties.getDatagenData();
                if (datagenData.getTags().contains(blockTag)) {
                    final Item item = block.asItem();
                    if (!item.equals(Items.AIR)) {
                        tag(itemTag).add(item);
                    }
                }
            }
        }
    }
    public void copyTagsFromBlockProperties(Set<DeferredHolder<Block, ? extends Block>> blocks) {
        var blockList = blocks.stream().map(DeferredHolder::get).sorted(Comparator.comparingInt((Block b) -> BuiltInRegistries.BLOCK.getId(b))).toList();
        for (Block block : blockList) {
            var item = block.asItem();
            if (item.equals(Items.AIR)) {
                continue;
            }
            var properties = (LodestoneBlockProperties) block.properties();
            var data = properties.getDatagenData();
            for (TagKey<Block> tag : data.getTags()) {
                var itemTag = TagKey.create(Registries.ITEM, tag.location());
                tag(itemTag).add(item);
            }
        }
    }
}