package team.lodestar.lodestone.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
public interface IDatagenPathfinder {

    String getModId();

    String getFolder();

    default String getBlockName(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    default ResourceLocation getBlockTexture(Block block) {
        return getBlockTexture(getBlockName(block));
    }

    default ResourceLocation getBlockTexture(String path) {
        return path("block/" + path);
    }

    default ResourceLocation getAbsoluteBlockTexture(String path) {
        var texture = getBlockTexture(path);
        return DatagenSystemCommons.escapeTextureFolderHierarchy(texture);
    }

    default String getItemName(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    default ResourceLocation getItemTexture(Item item) {
        return getItemTexture(getItemName(item));
    }

    default ResourceLocation getItemTexture(String path) {
        return path("item/" + path);
    }

    default ResourceLocation getAbsoluteItemTexture(String path) {
        var texture = getItemTexture(path);
        return DatagenSystemCommons.escapeTextureFolderHierarchy(texture);
    }

    default ResourceLocation extendWithFolder(ResourceLocation rl) {
        if (rl.getPath().contains("/")) {
            return rl;
        }
        return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), getFolder() + "/" + rl.getPath());
    }

    default ResourceLocation path(String name) {
        return ResourceLocation.fromNamespaceAndPath(getModId(), name);
    }
}
