package team.lodestar.lodestone.systems.recipe;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import team.lodestar.lodestone.helpers.ItemHelper;

import java.util.List;

public class WrappedItem implements IRecipeComponent {
    public final ItemStack stack;

    public WrappedItem(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public ItemStack getStack() {
        return stack;
    }

    @Override
    public List<ItemStack> getStacks() {
        return ItemHelper.copyWithNewCount(List.of(stack), getCount());
    }

    @Override
    public Item getItem() {
        return stack.getItem();
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return stack.equals(this.stack, false); //TODO: not sure if this is right, need a check on this
    }
}