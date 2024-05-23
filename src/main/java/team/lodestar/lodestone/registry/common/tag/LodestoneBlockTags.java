package team.lodestar.lodestone.registry.common.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class LodestoneBlockTags {

    public static TagKey<Block> modTag(String path) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(path));
    }

    public static TagKey<Block> forgeTag(String name) {
        return create(new ResourceLocation("forge", name));
    }

    public static TagKey<Block> create(final ResourceLocation name) {
        return TagKey.create(Registries.BLOCK, name);
    }
}