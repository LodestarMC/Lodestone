package team.lodestar.lodestone.systems.datagen.providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import team.lodestar.lodestone.systems.block.LodestoneBlockProperties;
import team.lodestar.lodestone.systems.datagen.LodestoneDatagenBlockData;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public abstract class LodestoneBlockTagsProvider extends FabricTagProvider.BlockTagProvider {


    public LodestoneBlockTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public void addTagsFromBlockProperties(Collection<Block> blocks) {
        for (Block block : blocks) {
            LodestoneBlockProperties properties = (LodestoneBlockProperties) block.properties;
            LodestoneDatagenBlockData data = properties.getDatagenData();
            for (TagKey<Block> tag : data.getTags()) {
                getOrCreateTagBuilder(tag).add(block);
            }
        }
    }
}
