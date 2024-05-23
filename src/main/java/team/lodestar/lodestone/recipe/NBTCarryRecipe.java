package team.lodestar.lodestone.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import team.lodestar.lodestone.registry.common.LodestoneRecipeSerializerRegistry;


public class NBTCarryRecipe extends ShapedRecipe {
    public static final String NAME = "nbt_carry";

    public final Ingredient nbtCarry;

    public NBTCarryRecipe(ShapedRecipe compose, Ingredient nbtCarry, ItemStack output) {
        super(compose.getId(), compose.getGroup(), compose.category(), compose.getWidth(), compose.getHeight(), compose.getIngredients(), output);
        this.nbtCarry = nbtCarry;
    }

    @NotNull
    @Override
    public ItemStack assemble(@NotNull CraftingContainer inv, @NotNull RegistryAccess registryAccess) {
        ItemStack out = super.assemble(inv, registryAccess);
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && nbtCarry.test(stack) && stack.hasTag()) {
                out.setTag(stack.getTag());
                break;
            }
        }
        return out;
    }

    @NotNull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return LodestoneRecipeSerializerRegistry.NBT_CARRY_RECIPE_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<NBTCarryRecipe> {
        @Override
        public NBTCarryRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            return new NBTCarryRecipe(SHAPED_RECIPE.fromJson(recipeId, json), Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "nbtCarry")), ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result")));
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public NBTCarryRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            ShapedRecipe recipe = SHAPED_RECIPE.fromNetwork(recipeId, buffer);
            Ingredient nbtCarry = Ingredient.fromNetwork(buffer);
            ItemStack stack = buffer.readItem();
            return new NBTCarryRecipe(recipe, nbtCarry, stack);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull NBTCarryRecipe recipe) {
            SHAPED_RECIPE.toNetwork(buffer, recipe);
            recipe.nbtCarry.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}