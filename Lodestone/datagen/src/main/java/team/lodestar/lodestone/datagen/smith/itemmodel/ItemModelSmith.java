package team.lodestar.lodestone.datagen.smith.itemmodel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import team.lodestar.lodestone.datagen.providers.item.LodestoneItemModelSystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * A class responsible for generating item models when used with an ItemModelProvider
 */
public class ItemModelSmith {

    private final ItemModelSupplier modelSupplier;

    public static ItemModelSmith parentedItem(ResourceLocation parent, boolean useTexture) {
        return new ItemModelSmith(((item, provider) -> {
            var name = provider.getItemName(item);
            if (useTexture) {
                return provider.createGenericModel(item, parent, provider.getItemTexture(name));
            }
            return provider.createParentedModel(item, parent);
        }));
    }

    public ItemModelSmith(ItemModelSupplier modelSupplier) {
        this.modelSupplier = modelSupplier;
    }

    public ConfiguredItemModelSmith modifyResult(Consumer<ItemModelSmithResult> modifier) {
        return configure().modifyResult(modifier);
    }

    public ConfiguredItemModelSmith addModelPathAffix(String affix) {
        return configure().addModelPathAffix(affix);
    }

    public ConfiguredItemModelSmith modifyModelPath(UnaryOperator<String> modelPathModifier) {
        return configure().modifyModelPath(modelPathModifier);
    }

    public ConfiguredItemModelSmith addTextureNameAffix(String affix) {
        return configure().addTextureNameAffix(affix);
    }

    public ConfiguredItemModelSmith modifyTexturePath(UnaryOperator<String> textureNameModifier) {
        return configure().modifyTexturePath(textureNameModifier);
    }

    protected ConfiguredItemModelSmith configure() {
        return new ConfiguredItemModelSmith(modelSupplier);
    }

    @SafeVarargs
    public final List<ItemModelSmithResult> act(ItemModelSmithProcessor data, Supplier<? extends Item>... items) {
        return act(data, List.of(items));
    }

    public final List<ItemModelSmithResult> act(ItemModelSmithProcessor data, Collection<Supplier<? extends Item>> items) {
        var copy = new ArrayList<>(items);
        var result = new ArrayList<ItemModelSmithResult>();
        for (Supplier<? extends Item> item : copy) {
            result.add(act(data, item));
        }
        return result;
    }

    public ItemModelSmithResult act(ItemModelSmithProcessor data, Supplier<? extends Item> itemSupplier) {
        data.consumer().accept(itemSupplier);
        return act(data.provider(), itemSupplier);
    }

    public ItemModelSmithResult act(LodestoneItemModelSystem provider, Supplier<? extends Item> itemSupplier) {
        var item = itemSupplier.get();
        preDatagen(provider, item);
        var model = modelSupplier.act(item, provider);
        var result = new ItemModelSmithResult(provider, item, model);
        postDatagen(result);
        return result;
    }

    protected void preDatagen(LodestoneItemModelSystem provider, Item item) {

    }

    protected void postDatagen(ItemModelSmithResult result) {

    }

    public interface ItemModelSupplier {
        ItemModelBuilder act(Item item, LodestoneItemModelSystem provider);
    }
}