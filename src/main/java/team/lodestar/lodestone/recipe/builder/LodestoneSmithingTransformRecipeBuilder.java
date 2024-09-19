package team.lodestar.lodestone.recipe.builder;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;

public class LodestoneSmithingTransformRecipeBuilder extends SmithingTransformRecipeBuilder implements LodestoneRecipeBuilder<SmithingTransformRecipe> {
    public LodestoneSmithingTransformRecipeBuilder(SmithingTransformRecipeBuilder parent) {
        super(
                parent.template, parent.base,
                parent.addition, parent.category,
                parent.result
        );
        this.criteria = parent.criteria;
    }

    @Override
    public SmithingTransformRecipe build(ResourceLocation id) {
        return new SmithingTransformRecipe(
                this.template, this.base,
                this.addition, new ItemStack(this.result)
        );
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation recipeId) {
        this.ensureValid(recipeId);
        defaultSaveFunc(recipeOutput, recipeId);
    }
}
