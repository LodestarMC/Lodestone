package team.lodestar.lodestone.systems.datagen;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.io.IOException;

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
}
