package team.lodestar.lodestone.systems.datagen.itemsmith;

import net.minecraft.world.item.Item;
import team.lodestar.lodestone.systems.datagen.providers.LodestoneItemModelProvider;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A configured instance of an ItemModelSmith
 */
public class ConfiguredItemModelSmith extends ItemModelSmith {

    private Consumer<ItemModelSmithResult> modifier;
    private Function<String, String> modelNameModifier;
    private Function<String, String> textureNameModifier;

    public ConfiguredItemModelSmith(ItemModelSupplier modelSupplier) {
        super(modelSupplier);
    }

    public ConfiguredItemModelSmith modifyResult(Consumer<ItemModelSmithResult> modifier) {
        this.modifier = modifier;
        return this;
    }

    public ConfiguredItemModelSmith addModelNameAffix(String affix) {
        return modifyModelName(s -> s + affix);
    }

    public ConfiguredItemModelSmith modifyModelName(Function<String, String> modelNameModifier) {
        this.modelNameModifier = modelNameModifier;
        return this;
    }

    public ConfiguredItemModelSmith addTextureNameAffix(String affix) {
        return modifyTextureName(s -> s + affix);
    }

    public ConfiguredItemModelSmith modifyTextureName(Function<String, String> textureNameModifier) {
        this.textureNameModifier = textureNameModifier;
        return this;
    }

    @Override
    protected void preDatagen(LodestoneItemModelProvider provider, Item item) {
        provider.setModelNameModifier(modelNameModifier);
        provider.setTextureNameModifier(textureNameModifier);
    }

    @Override
    protected void postDatagen(ItemModelSmithResult result) {
        if (modifier != null) {
            result.applyModifier(modifier);
        }
        var provider = result.provider();
        provider.setTextureNameModifier(null);
        provider.setModelNameModifier(null);
    }
}