package team.lodestar.lodestone.systems.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.function.Function;

public abstract class LodestoneBlockStateProvider extends BlockStateProvider {

    private final LodestoneBlockModelProvider blockModels;
    private String texturePath = "";

    public LodestoneBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
        this.blockModels = new LodestoneBlockModelProvider(this, gen, modid, exFileHelper);
    }

    @Override
    public BlockModelProvider models() {
        return blockModels;
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public ModelFile predefinedModel(Block block) {
        return models().getExistingFile(ForgeRegistries.BLOCKS.getKey(block));
    }

    public String getBlockName(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    public String getModIdFromBlock(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getNamespace();
    }

    public ResourceLocation getPath(Block block, String path) {
        return new ResourceLocation(getModIdFromBlock(block), "block/"+path);
    }

    public ResourceLocation extend(ResourceLocation resourceLocation, String suffix) {
        return new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath() + suffix);
    }
}
