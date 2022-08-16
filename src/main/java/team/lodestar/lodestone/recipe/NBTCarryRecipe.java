package team.lodestar.lodestone.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.setup.LodestoneRecipeSerializerRegistry;

import javax.annotation.Nonnull;

public class NBTCarryRecipe extends ShapedRecipe {
    public static final String NAME = "nbt_carry";

    public static class Type implements RecipeType<NBTCarryRecipe> {
        @Override
        public String toString() {
            return LodestoneLib.LODESTONE + ":" + NAME;
        }

        public static final NBTCarryRecipe.Type INSTANCE = new NBTCarryRecipe.Type();
    }

    public final Ingredient nbtCarry;

    public NBTCarryRecipe(ShapedRecipe compose, Ingredient nbtCarry) {
        super(compose.getId(), compose.getGroup(), compose.getWidth(), compose.getHeight(), compose.getIngredients(), compose.getResultItem());
        this.nbtCarry = nbtCarry;
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull CraftingContainer inv) {
        ItemStack out = super.assemble(inv);
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && nbtCarry.test(stack) && stack.hasTag()) {
                out.setTag(stack.getTag());
                break;
            }
        }
        return out;
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return LodestoneRecipeSerializerRegistry.NBT_CARRY_RECIPE_SERIALIZER.get();
    }
    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<NBTCarryRecipe> {
        @Override
        public NBTCarryRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            return new NBTCarryRecipe(SHAPED_RECIPE.fromJson(recipeId, json), Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "nbt_carry")));
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public NBTCarryRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
            return new NBTCarryRecipe(SHAPED_RECIPE.fromNetwork(recipeId, buffer), Ingredient.fromNetwork(buffer));
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull NBTCarryRecipe recipe) {
            SHAPED_RECIPE.toNetwork(buffer, recipe);
            recipe.nbtCarry.toNetwork(buffer);
        }
    }
}