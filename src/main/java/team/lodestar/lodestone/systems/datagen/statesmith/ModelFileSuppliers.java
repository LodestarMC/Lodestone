package team.lodestar.lodestone.systems.datagen.statesmith;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.function.Function;

import static team.lodestar.lodestone.systems.datagen.statesmith.BlockStateSmithTypes.getBlockName;

public class ModelFileSuppliers {

    public ModelFile predefinedModel(Block block, BlockStateProvider provider, Function<String, ResourceLocation> textureGetter) {
        return provider.models().getExistingFile(textureGetter.apply(getBlockName(block)));
    }
}
