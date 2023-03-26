package team.lodestar.lodestone.data;

import team.lodestar.lodestone.LodestoneLib;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
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

    }
}