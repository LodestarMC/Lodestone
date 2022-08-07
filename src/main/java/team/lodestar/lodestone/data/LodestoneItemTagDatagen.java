package team.lodestar.lodestone.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import team.lodestar.lodestone.LodestoneLib;

import static team.lodestar.lodestone.setup.LodestoneItemTags.*;

public class LodestoneItemTagDatagen extends ItemTagsProvider {
    public LodestoneItemTagDatagen(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, LodestoneLib.LODESTONE, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Malum Item Tags";
    }

    @Override
    protected void addTags() {
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