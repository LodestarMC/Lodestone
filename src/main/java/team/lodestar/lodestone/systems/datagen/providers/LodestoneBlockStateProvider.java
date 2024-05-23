package team.lodestar.lodestone.systems.datagen.providers;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.models.generators.ConfiguredModel;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelFile;
import io.github.fabricators_of_create.porting_lib.models.generators.block.BlockStateProvider;
import io.github.fabricators_of_create.porting_lib.models.generators.block.VariantBlockStateBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import team.lodestar.lodestone.systems.datagen.statesmith.ModularBlockStateSmith;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

public abstract class LodestoneBlockStateProvider extends BlockStateProvider {

    public final Set<ResourceLocation> staticTextures = new HashSet<>();

    private final LodestoneBlockModelProvider blockModels;
    public final LodestoneItemModelProvider itemModelProvider;

    private static String texturePath = "";

    public LodestoneBlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper, LodestoneItemModelProvider itemModelProvider) {
        super(output, modid, exFileHelper);
        this.itemModelProvider = itemModelProvider;
        this.blockModels = new LodestoneBlockModelProvider(this, output, modid, exFileHelper);
    }

    @Override
    public LodestoneBlockModelProvider models() {
        return blockModels;
    }

    @Override
    public LodestoneItemModelProvider itemModels() {
        return itemModelProvider;
    }

    public void setTexturePath(String texturePath) {
        LodestoneBlockStateProvider.texturePath = texturePath;
    }

    public static String getTexturePath() {
        return texturePath;
    }

    public ModularBlockStateSmith.ModelFileSupplier fromFunction(BiFunction<String, ResourceLocation, ModelFile> modelFileFunction) {
        return b -> {
            String name = getBlockName(b);
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

    public ModelFile predefinedModel(Block block) {
        return models().getExistingFile(BuiltInRegistries.BLOCK.getKey(block));
    }

    public ModelFile predefinedModel(Block block, String extension) {
        return models().getExistingFile(extend(BuiltInRegistries.BLOCK.getKey(block), extension));
    }

    public ModelFile grassBlockModel(Block block) {
        String name = getBlockName(block);
        ResourceLocation side = getBlockTexture(name);
        ResourceLocation dirt = new ResourceLocation("block/dirt");
        ResourceLocation top = getBlockTexture(name + "_top");
        return models().cubeBottomTop(name, side, dirt, top);
    }

    public ModelFile leavesBlockModel(Block block) {
        String name = getBlockName(block);
        return models().withExistingParent(name, new ResourceLocation("block/leaves")).texture("all", getBlockTexture(name));
    }

    public ModelFile airModel(Block block) {
        String name = getBlockName(block);
        return models().withExistingParent(name, new ResourceLocation("block/air"));
    }

    public ModelFile cubeModelAirTexture(Block block) {
        String name = getBlockName(block);
        return models().cubeAll(name, new ResourceLocation("block/air"));
    }

    public String getBlockName(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    public ResourceLocation getBlockTexture(String path) {
        return modLoc("block/" + path);
    }

    public ResourceLocation getStaticBlockTexture(String path) {
        return markTextureAsStatic(getBlockTexture(path));
    }

    public ResourceLocation markTextureAsStatic(ResourceLocation texture) {
        staticTextures.add(texture);
        return texture;
    }

    //TODO: move this to some sorta ResourceLocationHelper if it ever becomes needed.
    public ResourceLocation extend(ResourceLocation resourceLocation, String suffix) {
        return new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath() + suffix);
    }
}