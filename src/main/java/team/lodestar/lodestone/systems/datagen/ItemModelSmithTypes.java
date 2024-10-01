package team.lodestar.lodestone.systems.datagen;

import io.github.fabricators_of_create.porting_lib.models.generators.ModelFile;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import team.lodestar.lodestone.systems.datagen.itemsmith.ItemModelSmith;

import java.util.function.Function;

public class ItemModelSmithTypes {

    public static final ResourceLocation GENERATED = ResourceLocation.parse("item/generated");
    public static final ResourceLocation HANDHELD = ResourceLocation.parse("item/handheld");

    public static ItemModelSmith NO_MODEL = new ItemModelSmith(((item, provider) -> {
    }));

    public static ItemModelSmith HANDHELD_ITEM = new ItemModelSmith(((item, provider) -> {
        String name = provider.getItemName(item);
        provider.createGenericModel(item, HANDHELD, provider.getItemTexture(name));
    }));
    public static ItemModelSmith GENERATED_ITEM = new ItemModelSmith(((item, provider) -> {
        String name = provider.getItemName(item);
        provider.createGenericModel(item, GENERATED, provider.getItemTexture(name));
    }));
    public static ItemModelSmith UNIQUE_ITEM_MODEL = new ItemModelSmith(((item, provider) -> {
        String name = provider.getItemName(item);
        ResourceLocation path = provider.modLoc("item/" + name);
        if (provider.existingFileHelper.exists(path, PackType.CLIENT_RESOURCES)) {
            provider.getExistingFile(path);
        }
    }));

    public static ItemModelSmith BLOCK_TEXTURE_ITEM = new ItemModelSmith(((item, provider) -> {
        String name = provider.getItemName(item);
        provider.createGenericModel(item, GENERATED, provider.getBlockTexture(name));
    }));
    public static Function<String, ItemModelSmith> AFFIXED_BLOCK_TEXTURE_MODEL = Util.memoize((affix) -> new ItemModelSmith(((item, provider) -> {
        String name = provider.getItemName(item);
        provider.createGenericModel(item, GENERATED, provider.getBlockTexture(name + affix));
    })));

    public static ItemModelSmith BLOCK_MODEL_ITEM = new ItemModelSmith(((item, provider) -> {
        String name = provider.getItemName(item);
        provider.getBuilder(name).parent(new ModelFile.UncheckedModelFile(provider.modLoc("block/" + name)));
    }));
    public static Function<String, ItemModelSmith> AFFIXED_BLOCK_MODEL = Util.memoize((affix) -> new ItemModelSmith(((item, provider) -> {
        String name = provider.getItemName(item);
        provider.getBuilder(name).parent(new ModelFile.UncheckedModelFile(provider.modLoc("block/" + name + affix)));
    })));

    public static ItemModelSmith CROSS_MODEL_ITEM = new ItemModelSmith(((item, provider) -> {
        provider.createGenericModel(item, GENERATED, provider.getBlockTextureFromCache("cross"));
    }));
    public static ItemModelSmith WALL_ITEM = new ItemModelSmith(((item, provider) -> {
        String name = provider.getItemName(item);
        provider.wallInventory(name, provider.getBlockTextureFromCache("wall"));
    }));
    public static ItemModelSmith FENCE_ITEM = new ItemModelSmith(((item, provider) -> {
        String name = provider.getItemName(item);
        provider.fenceInventory(name, provider.getBlockTextureFromCache("texture"));
    }));

    public static ItemModelSmith BUTTON_ITEM = AFFIXED_BLOCK_MODEL.apply("_inventory");
    public static ItemModelSmith TRAPDOOR_ITEM = AFFIXED_BLOCK_MODEL.apply("_bottom");
}