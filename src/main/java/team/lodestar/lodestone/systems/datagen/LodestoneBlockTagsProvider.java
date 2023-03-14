package team.lodestar.lodestone.systems.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.systems.block.LodestoneBlockProperties;
import team.lodestar.lodestone.systems.datagen.LodestoneDatagenBlockData;

public abstract class LodestoneBlockTagsProvider extends BlockTagsProvider {

    public LodestoneBlockTagsProvider(DataGenerator p_126511_, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_126511_, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
    }

    public void addTagsFromBlockProperties(Block[] blocks) {
        for (Block block : blocks) {
            LodestoneBlockProperties properties = (LodestoneBlockProperties) block.properties;
            LodestoneDatagenBlockData data = properties.getDatagenData();
            for (TagKey<Block> tag : data.getTags()) {
                tag(tag).add(block);
            }
        }
    }
}
