package team.lodestar.lodestone.datagen.providers.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.spongepowered.include.com.google.common.base.Preconditions;
import team.lodestar.lodestone.datagen.DatagenSystemCommons;
import team.lodestar.lodestone.datagen.IDatagenPathfinder;

import java.util.function.Function;

@SuppressWarnings({"NullableProblems", "unused"})
public abstract class LodestoneItemModelSystem extends ItemModelProvider implements IDatagenPathfinder {

    private final Function<ResourceLocation, LodestoneItemModelBuilder> factory;

    public LodestoneItemModelSystem(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
        this.factory = l -> new LodestoneItemModelBuilder(this, l, existingFileHelper);
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
    public ModelFile.ExistingModelFile getExistingFile(ResourceLocation path) {
        var modified = DatagenSystemCommons.modifyModelPath(path);
        return super.getExistingFile(modified);
    }

    @Override
    public LodestoneItemModelBuilder getBuilder(String path) {
        Preconditions.checkNotNull(path, "Path must not be null");
        ResourceLocation outputLoc = extendWithFolder(path.contains(":") ? ResourceLocation.parse(path) : ResourceLocation.fromNamespaceAndPath(modid, path));
        this.existingFileHelper.trackGenerated(outputLoc, MODEL);
        return (LodestoneItemModelBuilder) generatedModels.computeIfAbsent(outputLoc, factory);
    }

    public ItemModelBuilder createParentedModel(Item item, ResourceLocation modelParent) {
        return getBuilder(getItemName(item)).parent(new ModelFile.UncheckedModelFile(modelParent));
    }

    public ItemModelBuilder createGenericModel(Item item, ResourceLocation modelParent, ResourceLocation... textures) {
        var itemModelBuilder = createParentedModel(item, modelParent);
        for (int i = 0; i < textures.length; i++) {
            itemModelBuilder.texture("layer" + i, textures[i]);
        }
        return itemModelBuilder;
    }
}