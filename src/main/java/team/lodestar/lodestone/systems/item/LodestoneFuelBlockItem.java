package team.lodestar.lodestone.systems.item;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;


public class LodestoneFuelBlockItem extends BlockItem {
    public final int fuel;

    public LodestoneFuelBlockItem(Block block, Properties properties, int fuel) {
        super(block, properties);
        this.fuel = fuel;
        FuelRegistry.INSTANCE.add(this, fuel);
    }
}
