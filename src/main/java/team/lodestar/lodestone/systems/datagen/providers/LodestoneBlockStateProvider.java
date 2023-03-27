package team.lodestar.lodestone.systems.datagen.providers;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

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

    public ResourceLocation getBlockTexture(String path) {
        return modLoc("block/"+path);
    }

    //TODO: move this to some sorta ResourceLocationHelper if it ever becomes needed.
    public ResourceLocation extend(ResourceLocation resourceLocation, String suffix) {
        return new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath() + suffix);
    }
}
