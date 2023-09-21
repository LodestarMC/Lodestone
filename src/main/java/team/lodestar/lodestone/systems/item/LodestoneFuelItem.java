package team.lodestar.lodestone.systems.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;

public class LodestoneFuelItem extends Item {
    public final int fuel;

    public LodestoneFuelItem(Properties properties, int fuel) {
        super(properties);
        this.fuel = fuel;
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return fuel;
    }
}
