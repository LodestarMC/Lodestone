package team.lodestar.lodestone.systems.datagen;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.systems.datagen.itemsmith.ItemModelSmith;

public class ItemModelSmithTypes {

    public static final ResourceLocation GENERATED = new ResourceLocation("item/generated");
    public static final ResourceLocation HANDHELD = new ResourceLocation("item/handheld");

    public static ItemModelSmith HANDHELD_ITEM = new ItemModelSmith(((item, provider) -> {
        String name = provider.getItemName(item);
        provider.createGenericModel(item, HANDHELD, provider.getItemTexture(name));
    }));
    public static ItemModelSmith GENERATED_ITEM = new ItemModelSmith(((item, provider) -> {
        String name = provider.getItemName(item);
        provider.createGenericModel(item, GENERATED, provider.getItemTexture(name));
    }));
    public static ItemModelSmith BLOCK_TEXTURE_ITEM = new ItemModelSmith(((item, provider) -> {
        String name = provider.getItemName(item);
        provider.createGenericModel(item, GENERATED, provider.getBlockTexture(name));
    }));
    public static ItemModelSmith BLOCK_MODEL_ITEM = new ItemModelSmith(((item, provider) -> {
        String name = provider.getItemName(item);
        provider.getBuilder(name).parent(new ModelFile.UncheckedModelFile(provider.modLoc("block/"+name)));
    }));
}
