package team.lodestar.lodestone.data;

import team.lodestar.lodestone.setup.LodestoneBlockTags;
import team.lodestar.lodestone.LodestoneLib;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

public class LodestoneBlockTagDatagen extends BlockTagsProvider {
    public LodestoneBlockTagDatagen(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, LodestoneLib.LODESTONE, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Lodestone Block Tags";
    }

    @Override
    protected void addTags() {
        tag(LodestoneBlockTags.TERRACOTTA).add(Registry.BLOCK.stream().filter(b -> b.getRegistryName().getPath().endsWith("terracotta")).toArray(Block[]::new));

    }
}