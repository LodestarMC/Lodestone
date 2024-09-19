package team.lodestar.lodestone.recipe.builder;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.ApiStatus;
import team.lodestar.lodestone.LodestoneLib;

import java.lang.reflect.Field;
import java.util.Map;

public interface LodestoneRecipeBuilder<R extends Recipe<?>> {

    void save(RecipeOutput recipeOutput, ResourceLocation id);

    default void tweakAdvancement(Advancement.Builder advancement) {}
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

    @ApiStatus.Internal
    default Map<String, Criterion<?>> getCriteria() {
        try {
            Field criteria = this.getClass().getField("criteria");
            criteria.setAccessible(true);
            return (Map<String, Criterion<?>>) criteria.get(this);
        } catch (Exception error) {
            if (error instanceof ClassCastException) {
                LodestoneLib.LOGGER.error("Field 'criteria' in class {} was not of type Map<String, Criterion<?>>", this.getClass().getSimpleName());
            } else if (error instanceof IllegalAccessException) {
                LodestoneLib.LOGGER.error("Field 'criteria' in class {} had wrong access flags", this.getClass().getSimpleName());
            }
            return Map.of(); // Allow builders which do not permit additional criteria
        }
    }

    @ApiStatus.Internal
    default String getRecipeSubfolder() {
        try {
            Field criteria = this.getClass().getField("category");
            criteria.setAccessible(true);
            return ((RecipeCategory) criteria.get(this)).getFolderName();
        } catch (Exception error) {
            if (error instanceof ClassCastException) {
                LodestoneLib.LOGGER.error("Field 'category' in class {} was not of type Map<String, Criterion<?>>", this.getClass().getSimpleName());
            } else if (error instanceof IllegalAccessException) {
                LodestoneLib.LOGGER.error("Field 'category' in class {} had wrong access flags", this.getClass().getSimpleName());
            }
            return ""; // Allow builders which do not have a specified category
        }
    }
}
