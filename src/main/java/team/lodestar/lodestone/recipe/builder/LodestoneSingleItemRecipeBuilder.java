package team.lodestar.lodestone.recipe.builder;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleItemRecipe;

import java.util.Objects;

public class LodestoneSingleItemRecipeBuilder extends SingleItemRecipeBuilder implements LodestoneRecipeBuilder<SingleItemRecipe> {
    public LodestoneSingleItemRecipeBuilder(SingleItemRecipeBuilder parent) {
        super(
                parent.category, parent.factory,
                parent.ingredient, parent.result,
                parent.count
        );
        this.criteria = parent.criteria;
        this.group(parent.group);
    }

    @Override
    public SingleItemRecipe build(ResourceLocation id) {
        return this.factory.create(
                Objects.requireNonNullElse(this.group, ""),
                this.ingredient, new ItemStack(this.result, this.count)
        );
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        this.ensureValid(id);
        defaultSaveFunc(recipeOutput, id);
    }
}
