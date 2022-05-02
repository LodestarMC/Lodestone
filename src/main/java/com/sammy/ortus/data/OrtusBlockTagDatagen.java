package com.sammy.ortus.data;

import com.sammy.ortus.OrtusLib;
import com.sammy.ortus.setup.OrtusBlockTags;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

public class OrtusBlockTagDatagen extends BlockTagsProvider {
    public OrtusBlockTagDatagen(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, OrtusLib.ORTUS, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Space Mod Block Tags";
    }

    @Override
    protected void addTags() {
        tag(OrtusBlockTags.TERRACOTTA).add(Registry.BLOCK.stream().filter(b -> b.getRegistryName().getPath().endsWith("terracotta")).toArray(Block[]::new));
    }
}