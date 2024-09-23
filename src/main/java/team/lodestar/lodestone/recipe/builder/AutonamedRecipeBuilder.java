package team.lodestar.lodestone.recipe.builder;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

public interface AutonamedRecipeBuilder<R extends Recipe<?>> extends LodestoneRecipeBuilder<R> {
    Item getResult();

    default void save(RecipeOutput recipeOutput) {
        this.save(recipeOutput, getDefaultRecipeId(this.getResult()));
    }

    default void save(RecipeOutput recipeOutput, String id) {
        ResourceLocation defaultId = getDefaultRecipeId(this.getResult());
        ResourceLocation providedId = ResourceLocation.parse(id);
        if (providedId.equals(defaultId)) {
            throw new IllegalStateException("Recipe " + id + " should remove its 'save' argument as it is equal to default one");
        } else {
            this.save(recipeOutput, providedId);
        }
    }

    static ResourceLocation getDefaultRecipeId(ItemLike itemLike) {
        return BuiltInRegistries.ITEM.getKey(itemLike.asItem());
    }
}
