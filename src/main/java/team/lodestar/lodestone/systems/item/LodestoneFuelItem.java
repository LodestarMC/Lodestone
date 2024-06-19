package team.lodestar.lodestone.systems.item;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.Nullable;

public class LodestoneFuelItem extends Item {
    public final int fuel;

    public LodestoneFuelItem(Properties properties, int fuel) {
        super(properties);
        this.fuel = fuel;
        FuelRegistry.INSTANCE.add(this, fuel);
    }
}
