package team.lodestar.lodestone.datagen.providers.block;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import team.lodestar.lodestone.datagen.IDatagenPathfinder;
import team.lodestar.lodestone.datagen.providers.item.LodestoneItemModelSystem;
import team.lodestar.lodestone.datagen.smith.blockstate.ModularBlockStateSmith;

import java.util.function.BiFunction;

@SuppressWarnings({"unused", "NullableProblems"})
public abstract class LodestoneBlockStateSystem extends BlockStateProvider implements IDatagenPathfinder {

    private final String modId;
    private final LodestoneBlockModelProvider blockModels;
    public final LodestoneItemModelSystem itemModelProvider;

    public LodestoneBlockStateSystem(PackOutput output, String modId, ExistingFileHelper exFileHelper, LodestoneItemModelSystem itemModelProvider) {
        super(output, modId, exFileHelper);
        this.modId = modId;
        this.itemModelProvider = itemModelProvider;
        this.blockModels = new LodestoneBlockModelProvider(output, modId, exFileHelper);
    }

    @Override
    public String getModId() {
        return modId;
    }

    @Override
    public String getFolder() {
        return "";
    }

    @Override
    public LodestoneBlockModelProvider models() {
        return blockModels;
    }

    @Override
    public LodestoneItemModelSystem itemModels() {
        return itemModelProvider;
    }

    public ModularBlockStateSmith.ModelFileSupplier fromFunction(BiFunction<String, ResourceLocation, ModelFile> modelFileFunction) {
        return b -> {
            var name = getBlockName(b);
            return modelFileFunction.apply(name, getBlockTexture(name));
        };
    }

    public void varyingRotationBlock(Block block, ModelFile model) {
        ConfiguredModel.Builder<VariantBlockStateBuilder> builder = getVariantBuilder(block).partialState().modelForState()
                .modelFile(model)
                .nextModel().modelFile(model).rotationY(90)
                .nextModel().modelFile(model).rotationY(180)
                .nextModel().modelFile(model).rotationY(270);
        simpleBlock(block, builder.build());
    }
}