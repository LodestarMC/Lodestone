package team.lodestar.lodestone.systems.datagen.itemsmith;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.loaders.ItemLayerModelBuilder;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import team.lodestar.lodestone.systems.datagen.providers.LodestoneItemModelProvider;

import java.util.function.Consumer;

public record ItemModelSmithResult(LodestoneItemModelProvider provider, Item item, ItemModelBuilder builder) {

    public ItemModelBuilder parentedToThis(ExistingFileHelper existingFileHelper) {
        return new ItemModelBuilder(builder.getLocation(), existingFileHelper).parent(builder);
    }

    public ItemModelBuilder parentedToThis(ExistingFileHelper existingFileHelper, String childName) {
        return new ItemModelBuilder(builder.getLocation().withSuffix("_" + childName), existingFileHelper).parent(builder);
    }

    public ItemLayerModelBuilder<ItemModelBuilder> addModelLayerData() {
        return builder.customLoader(ItemLayerModelBuilder::begin);
    }

    public SeparateTransformsModelBuilder<ItemModelBuilder> addSeparateTransformData() {
        return builder.customLoader(SeparateTransformsModelBuilder::begin);
    }

    public ItemModelSmithResult applyModifier(Consumer<ItemModelSmithResult> modifier) {
        modifier.accept(this);
        return this;
    }
}