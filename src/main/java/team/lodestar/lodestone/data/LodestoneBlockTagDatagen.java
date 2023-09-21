package team.lodestar.lodestone.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.setup.LodestoneBlockTags;
import team.lodestar.lodestone.LodestoneLib;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class LodestoneBlockTagDatagen extends BlockTagsProvider {
    public LodestoneBlockTagDatagen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, LodestoneLib.LODESTONE, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Lodestone Block Tags";
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {


    }
}