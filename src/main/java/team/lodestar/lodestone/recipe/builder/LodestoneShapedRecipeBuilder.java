package team.lodestar.lodestone.recipe.builder;

import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.ShapedRecipe;
import team.lodestar.lodestone.helpers.ReflectionHelper;

import java.util.Objects;

public class LodestoneShapedRecipeBuilder extends ShapedRecipeBuilder implements LodestoneRecipeBuilder<ShapedRecipe> {
    public LodestoneShapedRecipeBuilder(ShapedRecipeBuilder parent) {
        super(parent.category, parent.resultStack);
        ReflectionHelper.copyFields(parent, this);
    }

    public ShapedRecipe build(ResourceLocation id) {
        return new ShapedRecipe(
                Objects.requireNonNullElse(this.group, ""),
                RecipeBuilder.determineBookCategory(this.category),
                this.ensureValid(id),
                this.resultStack,
                this.showNotification
        );
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        defaultSaveFunc(recipeOutput, id);
    }
}
