package team.lodestar.lodestone.systems.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;


public class LodestoneFuelBlockItem extends CombustibleBlockItem {
    public final int fuel;

    public LodestoneFuelBlockItem(Block block, Properties properties, int fuel) {
        super(block, properties);
        this.fuel = fuel;
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return fuel;
    }
}
