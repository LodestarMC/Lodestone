package team.lodestar.lodestone.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.datagen.providers.*;

import java.util.concurrent.CompletableFuture;

import static team.lodestar.lodestone.registry.common.tag.LodestoneItemTags.*;


public class LodestoneItemTagDatagen extends LodestoneItemTagsProvider {
    public LodestoneItemTagDatagen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> blockProvider) {
        super(output, provider, blockProvider, LodestoneLib.LODESTONE);
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
        tag(NUGGETS_COBALT);
        tag(INGOTS_COBALT);
    }
}