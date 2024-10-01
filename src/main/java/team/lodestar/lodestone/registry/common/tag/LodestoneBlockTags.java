package team.lodestar.lodestone.registry.common.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class LodestoneBlockTags {

    public static TagKey<Block> modTag(String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.parse(path));
    }

    public static TagKey<Block> cTag(String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", name));
    }
}