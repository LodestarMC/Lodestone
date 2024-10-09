package team.lodestar.lodestone.recipe.builder;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import team.lodestar.lodestone.helpers.ReflectionHelper;

import java.util.Map;
import java.util.Optional;

public interface LodestoneRecipeBuilder<R extends Recipe<?>> {

    void save(RecipeOutput recipeOutput, ResourceLocation id);

    default void tweakAdvancement(Advancement.Builder advancement) {
    }

    R build(ResourceLocation id);

    default void write(RecipeOutput consumer, ResourceLocation id, R recipe, Advancement.Builder advancement) {
        String subfolder = this.getRecipeSubfolder().isEmpty() ? "" : this.getRecipeSubfolder() + "/";
        consumer.accept(id, recipe, advancement.build(id.withPrefix("recipes/" + subfolder)));
    }

    default void defaultSaveFunc(RecipeOutput recipeOutput, ResourceLocation id) {
        Advancement.Builder advancement$builder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.tweakAdvancement(advancement$builder);
        this.getCriteria().forEach(advancement$builder::addCriterion);
        this.write(recipeOutput, id, build(id), advancement$builder);
    }

    default Map<String, Criterion<?>> getCriteria() {
        Optional<Map<String, Criterion<?>>> criteria = ReflectionHelper.getField("criteria", this);
        return criteria.orElse(Map.of());
    }

    default String getRecipeSubfolder() {
        Optional<RecipeCategory> category = ReflectionHelper.getField("category", this);
        return category.map(RecipeCategory::getFolderName).orElse("");
    }
}
