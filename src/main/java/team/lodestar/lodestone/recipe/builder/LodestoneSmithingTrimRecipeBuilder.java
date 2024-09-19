package team.lodestar.lodestone.recipe.builder;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SmithingTrimRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;

public class LodestoneSmithingTrimRecipeBuilder extends SmithingTrimRecipeBuilder implements LodestoneRecipeBuilder<SmithingTrimRecipe> {
    public LodestoneSmithingTrimRecipeBuilder(SmithingTrimRecipeBuilder parent) {
        super(
                parent.category, parent.template,
                parent.base, parent.addition
        );
        this.criteria = parent.criteria;
    }

    @Override
    public SmithingTrimRecipe build(ResourceLocation id) {
        return new SmithingTrimRecipe(this.template, this.base, this.addition);
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation recipeId) {
        this.ensureValid(recipeId);
        defaultSaveFunc(recipeOutput, recipeId);
    }
}
