package team.lodestar.lodestone.datagen.providers.block;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.spongepowered.include.com.google.common.base.Preconditions;
import team.lodestar.lodestone.datagen.DatagenSystemCommons;
import team.lodestar.lodestone.datagen.IDatagenPathfinder;

import java.util.function.Function;

@SuppressWarnings("NullableProblems")
public final class LodestoneBlockModelProvider extends BlockModelProvider implements IDatagenPathfinder {

    private final Function<ResourceLocation, LodestoneBlockModelBuilder> factory;

    public LodestoneBlockModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
        this.factory = l -> new LodestoneBlockModelBuilder(l, existingFileHelper);
    }

    public LodestoneBlockModelBuilder getBuilder(String path) {
        Preconditions.checkNotNull(path, "Path must not be null");
        ResourceLocation outputLoc = extendWithFolder(path.contains(":") ? ResourceLocation.parse(path) : ResourceLocation.fromNamespaceAndPath(modid, path));
        this.existingFileHelper.trackGenerated(outputLoc, MODEL);
        return (LodestoneBlockModelBuilder) generatedModels.computeIfAbsent(outputLoc, factory);
    }

    @Override
    public String getModId() {
        return modid;
    }

    @Override
    public String getFolder() {
        return folder;
    }

    @Override
    protected void registerModels() {

    }

    @Override
    public ModelFile.ExistingModelFile getExistingFile(ResourceLocation path) {
        var modified = DatagenSystemCommons.modifyModelPath(path);
        return super.getExistingFile(modified);
    }

    public ModelFile predefinedModel(Block block) {
        return predefinedModel(block, "");
    }

    public ModelFile predefinedModel(Block block, String affix) {
        var id = BuiltInRegistries.BLOCK.getKey(block);
        var path = id.withSuffix(affix);
        return getExistingFile(path);
    }

    public ModelFile grassBlockModel(Block block) {
        String name = getBlockName(block);
        ResourceLocation side = getBlockTexture(name);
        ResourceLocation dirt = ResourceLocation.withDefaultNamespace("block/dirt");
        ResourceLocation top = getBlockTexture(name + "_top");
        return cubeBottomTop(name, side, dirt, top);
    }

    public ModelFile leavesBlockModel(Block block) {
        String name = getBlockName(block);
        return withExistingParent(name, ResourceLocation.withDefaultNamespace("block/leaves")).texture("all", getBlockTexture(name));
    }

    public ModelFile crossModel(Block block) {
        String name = getBlockName(block);
        return cross(name, getBlockTexture(name));
    }

    public ModelFile cubeBottomTop(Block block) {
        String name = getBlockName(block);
        ResourceLocation side = getBlockTexture(name + "_side");
        ResourceLocation bottom = getBlockTexture(name + "_bottom");
        ResourceLocation top = getBlockTexture(name + "_top");
        return cubeBottomTop(name, side, bottom, top);
    }

    public ModelFile airModel(Block block) {
        String name = getBlockName(block);
        return withExistingParent(name, ResourceLocation.withDefaultNamespace("block/air"));
    }

    public ModelFile cubeModelAirTexture(Block block) {
        String name = getBlockName(block);
        return cubeAll(name, ResourceLocation.withDefaultNamespace("block/air"));
    }
}