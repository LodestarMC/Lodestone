package team.lodestar.lodestone.datagen.providers.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.lodestar.lodestone.core.block.LodestoneBlockProperties;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public abstract class LodestoneItemTagsSystem extends ItemTagsProvider {

    public LodestoneItemTagsSystem(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, String modId, ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, modId, existingFileHelper);
    }

    @Override
    public IntrinsicTagAppender<Item> tag(TagKey<Item> pTag) {
        return super.tag(pTag);
    }

    public void addTagsFromBlockProperties(Set<DeferredHolder<Block, ? extends Block>> blocks) {
        var blockList = LodestoneBlockTagsSystem.sorted(blocks);
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