package team.lodestar.lodestone.systems.item;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;

public class CombustibleBlockItem extends BlockItem {
    private int burnTime = -1;

    public CombustibleBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    public void setBurnTime(int burnTime) {
        FuelRegistry.INSTANCE.add(this, burnTime);
        this.burnTime = burnTime;
    }

    //	@Override
    public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
        return this.burnTime;
    }

}