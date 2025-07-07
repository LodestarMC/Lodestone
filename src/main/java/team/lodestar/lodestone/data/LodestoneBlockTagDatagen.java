package team.lodestar.lodestone.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import team.lodestar.lodestone.LodestoneLib;

import java.util.concurrent.CompletableFuture;

public class LodestoneBlockTagDatagen extends BlockTagsProvider {
    public LodestoneBlockTagDatagen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, LodestoneLib.LODESTONE);
    }

    @Override
    public String getName() {
        return "Lodestone Block Tags";
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

    }
}