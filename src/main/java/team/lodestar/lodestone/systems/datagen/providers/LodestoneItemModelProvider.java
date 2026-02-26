package team.lodestar.lodestone.systems.datagen.providers;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Function;

@SuppressWarnings({"NullableProblems", "unused"})
public abstract class LodestoneItemModelProvider extends ItemModelProvider {

    private String texturePath = "";
    private Function<String, String> modelNameModifier;
    private Function<String, String> textureNameModifier;

    public LodestoneItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    public ItemModelBuilder getBuilder(String path) {
        if (modelNameModifier != null) {
            path = modelNameModifier.apply(path);
            modelNameModifier = null;
        }
        return super.getBuilder(path);
    }

    public void setModelNameModifier(Function<String, String> modelNameModifier) {
        this.modelNameModifier = modelNameModifier;
    }

    public void setTextureNameModifier(Function<String, String> textureNameModifier) {
        this.textureNameModifier = textureNameModifier;
    }

    public void setTexturePath(String texturePath) {
        if (!texturePath.isEmpty() && !texturePath.endsWith("/")) {
            texturePath += "/";
        }
        this.texturePath = texturePath;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public String getItemName(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    public ResourceLocation getItemTexture(String path) {
        String texture = path;
        if (textureNameModifier != null) {
            texture = textureNameModifier.apply(texture);
            textureNameModifier = null;
        }
        return modLoc("item/" + getTexturePath() + texture);
    }

    public ResourceLocation getBlockTexture(String path) {
        String texture = path;
        if (textureNameModifier != null) {
            texture = textureNameModifier.apply(texture);
            textureNameModifier = null;
        }
        return modLoc("block/" + LodestoneBlockStateProvider.getTexturePath() + texture);
    }

    @Override
    public ResourceLocation modLoc(String name) {
        String path = name;
        if (textureNameModifier != null) {
            path = textureNameModifier.apply(path);
            textureNameModifier = null;
        }
        return super.modLoc(path);
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

    public ResourceLocation getBlockTextureFromCache(String key) {
        return LodestoneBlockModelProvider.BLOCK_TEXTURE_CACHE.get(key);
    }
}