package team.lodestar.lodestone.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.LodestoneLib;

import java.util.concurrent.CompletableFuture;

import static team.lodestar.lodestone.setup.LodestoneItemTags.*;

public class LodestoneItemTagDatagen extends ItemTagsProvider {
    public LodestoneItemTagDatagen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> blockProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, blockProvider, LodestoneLib.LODESTONE, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Malum Item Tags";
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        tag(NUGGETS_COPPER);
        tag(INGOTS_COPPER).add(Items.COPPER_INGOT);
        tag(NUGGETS_LEAD);
        tag(INGOTS_LEAD);
        tag(NUGGETS_SILVER);
        tag(INGOTS_SILVER);
        tag(NUGGETS_ALUMINUM);
        tag(INGOTS_ALUMINUM);
        tag(NUGGETS_NICKEL);
        tag(INGOTS_NICKEL);
        tag(NUGGETS_URANIUM);
        tag(INGOTS_URANIUM);
        tag(NUGGETS_OSMIUM);
        tag(INGOTS_OSMIUM);
        tag(NUGGETS_ZINC);
        tag(INGOTS_ZINC);
        tag(NUGGETS_TIN);
        tag(INGOTS_TIN);
    }
}