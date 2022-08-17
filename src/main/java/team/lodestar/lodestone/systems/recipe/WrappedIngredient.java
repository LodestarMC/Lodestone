package team.lodestar.lodestone.systems.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import team.lodestar.lodestone.helpers.ItemHelper;

import java.util.ArrayList;
import java.util.List;

public class WrappedIngredient implements IRecipeComponent {
    public final Ingredient ingredient;

    public WrappedIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(getItem(), getCount(), ingredient.getItems()[0].getTag());
    }

    @Override
    public List<ItemStack> getStacks() {
        return ItemHelper.copyWithNewCount(List.of(ingredient.getItems()), getCount());
    }

    @Override
    public Item getItem() {
        return ingredient.getItems()[0].getItem();
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return ingredient.test(stack) && stack.getCount() >= getCount();
    }
}