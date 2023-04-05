package team.lodestar.lodestone.systems.datagen.providers;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class LodestoneItemModelProvider extends ItemModelProvider {

    private String texturePath = "";

    public LodestoneItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public String getItemName(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getPath();
    }

    public ResourceLocation getItemTexture(String path) {
        return modLoc("item/"+getTexturePath()+path);
    }

    public ResourceLocation getBlockTexture(String path) {
        return modLoc("block/"+getTexturePath()+path);
    }

    public void createGenericModel(Item item, ResourceLocation modelType, ResourceLocation textureLocation) {
        withExistingParent(getItemName(item), modelType).texture("layer0", textureLocation);
    }

    public ResourceLocation getBlockTextureFromCache(String key) {
        return LodestoneBlockModelProvider.BLOCK_TEXTURE_CACHE.get(key);
    }
}
