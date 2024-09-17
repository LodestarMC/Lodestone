package team.lodestar.lodestone.data.builder;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

import java.util.Objects;

public class ExtendedShapedRecipeBuilder extends ShapedRecipeBuilder {
    public ExtendedShapedRecipeBuilder(ShapedRecipeBuilder parent) {
        super(parent.category, parent.resultStack);
        this.rows = parent.rows;
        this.key = parent.key;
        this.criteria = parent.criteria;
        this.group = parent.group;
        this.showNotification(parent.showNotification);
    }

    public ShapedRecipe buildRecipe(ResourceLocation id) {
        return new ShapedRecipe(
                Objects.requireNonNullElse(this.group, ""),
                RecipeBuilder.determineBookCategory(this.category),
                this.ensureValid(id),
                this.resultStack,
                this.showNotification
        );
    }

    public void save(RecipeOutput consumer, ResourceLocation id, ShapedRecipe recipe, Advancement.Builder builder) {
        consumer.accept(id, recipe, builder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    public void tweakAdvancement(Advancement.Builder builder) {}

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        Advancement.Builder advancement$builder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.tweakAdvancement(advancement$builder);
        this.criteria.forEach(advancement$builder::addCriterion);
        ShapedRecipe shapedRecipe = buildRecipe(id);
        this.save(recipeOutput, id, shapedRecipe, advancement$builder);
    }
}
