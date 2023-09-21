package team.lodestar.lodestone.systems.recipe;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public interface IRecipeComponent {
    ItemStack getStack();

    List<ItemStack> getStacks();

    Item getItem();

    int getCount();

    default boolean isValid() {
        return !getStack().is(Items.BARRIER);
    }

    boolean matches(ItemStack stack);
}