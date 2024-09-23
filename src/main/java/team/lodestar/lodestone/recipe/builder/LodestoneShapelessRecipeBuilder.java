package team.lodestar.lodestone.recipe.builder;

import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import java.util.Objects;

public class LodestoneShapelessRecipeBuilder extends ShapelessRecipeBuilder implements LodestoneRecipeBuilder<ShapelessRecipe> {
    public LodestoneShapelessRecipeBuilder(ShapelessRecipeBuilder parent) {
        super(parent.category, parent.resultStack);
        this.ingredients = parent.ingredients;
        this.criteria = parent.criteria;
        this.group(parent.group);
    }

    @Override
    public ShapelessRecipe build(ResourceLocation id) {
        return new ShapelessRecipe(
                Objects.requireNonNullElse(this.group, ""),
                RecipeBuilder.determineBookCategory(this.category),
                this.resultStack, this.ingredients
        );
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        this.ensureValid(id);
        defaultSaveFunc(recipeOutput, id);
    }
}
