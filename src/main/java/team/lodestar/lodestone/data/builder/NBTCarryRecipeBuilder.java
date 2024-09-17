package team.lodestar.lodestone.data.builder;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import team.lodestar.lodestone.recipe.NBTCarryRecipe;

import java.util.Objects;

public class NBTCarryRecipeBuilder extends ShapedRecipeBuilder {
    Ingredient copyFrom;

    public NBTCarryRecipeBuilder(ShapedRecipeBuilder parent, Ingredient copyFrom) {
        super(parent.category, parent.resultStack);
        this.rows = parent.rows;
        this.key = parent.key;
        this.criteria = parent.criteria;
        this.group = parent.group;
        this.showNotification(parent.showNotification);
        this.copyFrom = copyFrom;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        ShapedRecipePattern shapedrecipepattern = this.ensureValid(id);
        Advancement.Builder advancement$builder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        NBTCarryRecipe carryRecipe = new NBTCarryRecipe(new ShapedRecipe(
                Objects.requireNonNullElse(this.group, ""),
                RecipeBuilder.determineBookCategory(this.category),
                shapedrecipepattern,
                this.resultStack,
                this.showNotification
        ), this.copyFrom);
        recipeOutput.accept(id, carryRecipe, advancement$builder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }
}
