package team.lodestar.lodestone.data.builder;

import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import team.lodestar.lodestone.recipe.NBTCarryRecipe;

public class NBTCarryRecipeBuilder extends ExtendedShapedRecipeBuilder {
    Ingredient copyFrom;

    public NBTCarryRecipeBuilder(ShapedRecipeBuilder parent, Ingredient copyFrom) {
        super(parent);
        this.copyFrom = copyFrom;
    }

    @Override
    public ShapedRecipe buildRecipe(ResourceLocation id) {
        return new NBTCarryRecipe(super.buildRecipe(id), this.copyFrom);
    }
}
