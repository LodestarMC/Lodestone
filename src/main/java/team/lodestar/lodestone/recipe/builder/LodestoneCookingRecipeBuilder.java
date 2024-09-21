package team.lodestar.lodestone.recipe.builder;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;

import java.util.Objects;

public class LodestoneCookingRecipeBuilder extends SimpleCookingRecipeBuilder implements LodestoneRecipeBuilder<AbstractCookingRecipe> {
    public LodestoneCookingRecipeBuilder(SimpleCookingRecipeBuilder parent) {
        super(
                parent.category, parent.bookCategory,
                parent.stackResult, parent.ingredient,
                parent.experience, parent.cookingTime,
                parent.factory
        );
        this.group(parent.group);
    }


    @Override
    public AbstractCookingRecipe build(ResourceLocation id) {
        return this.factory.create(
                Objects.requireNonNullElse(this.group, ""),
                this.bookCategory, this.ingredient, this.stackResult,
                this.experience, this.cookingTime
        );
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        defaultSaveFunc(recipeOutput, id);
    }
}
