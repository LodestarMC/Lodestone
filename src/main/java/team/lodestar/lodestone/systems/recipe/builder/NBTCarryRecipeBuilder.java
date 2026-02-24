package team.lodestar.lodestone.systems.recipe.builder;

import net.minecraft.advancements.*;
import net.minecraft.core.registries.*;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;
import team.lodestar.lodestone.systems.recipe.*;

import java.util.*;

public class NBTCarryRecipeBuilder extends ShapedRecipeBuilder implements LodestoneRecipeBuilder<NBTCarryRecipe> {

    public final Ingredient copyFrom;

    public NBTCarryRecipeBuilder(RecipeCategory category, ItemStack result, Ingredient copyFrom) {
        super(category, result);
        this.copyFrom = copyFrom;
    }

    @Override
    public NBTCarryRecipe buildRecipe(ResourceLocation id) {
        var recipe = new ShapedRecipe(
                Objects.requireNonNullElse(group, ""),
                RecipeBuilder.determineBookCategory(category),
                ensureValid(id),
                resultStack,
                showNotification
        );

        return new NBTCarryRecipe(recipe, this.copyFrom);
    }

    @Override
    public void save(RecipeOutput recipeOutput) {
        save(recipeOutput, BuiltInRegistries.ITEM.getKey(getResult()));
    }

    @Override
    public void save(RecipeOutput recipeOutput, String id) {
        save(recipeOutput, ResourceLocation.parse(id));
    }

    @Override
    public NBTCarryRecipeBuilder define(Character symbol, TagKey<Item> tag) {
        return (NBTCarryRecipeBuilder) super.define(symbol, tag);
    }

    @Override
    public NBTCarryRecipeBuilder define(Character symbol, ItemLike item) {
        return (NBTCarryRecipeBuilder) super.define(symbol, item);
    }

    @Override
    public NBTCarryRecipeBuilder define(Character symbol, Ingredient ingredient) {
        return (NBTCarryRecipeBuilder) super.define(symbol, ingredient);
    }

    @Override
    public NBTCarryRecipeBuilder pattern(String pattern) {
        return (NBTCarryRecipeBuilder) super.pattern(pattern);
    }

    @Override
    public NBTCarryRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        return (NBTCarryRecipeBuilder) super.unlockedBy(name, criterion);
    }

    @Override
    public NBTCarryRecipeBuilder group(@Nullable String groupName) {
        return (NBTCarryRecipeBuilder) super.group(groupName);
    }

    @Override
    public NBTCarryRecipeBuilder showNotification(boolean showNotification) {
        return (NBTCarryRecipeBuilder) super.showNotification(showNotification);
    }
}
