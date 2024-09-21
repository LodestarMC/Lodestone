package team.lodestar.lodestone.recipe.builder;

import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.Objects;

public class LodestoneShapedRecipeBuilder extends ShapedRecipeBuilder implements LodestoneRecipeBuilder<ShapedRecipe> {
    public LodestoneShapedRecipeBuilder(ShapedRecipeBuilder parent) {
        super(parent.category, parent.resultStack);
        this.rows = parent.rows;
        this.key = parent.key;
        this.criteria = parent.criteria;
        this.group(parent.group);
        this.showNotification(parent.showNotification);
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
