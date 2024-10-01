package team.lodestar.lodestone.forge_stuff;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IItemHandlerModifiable extends IItemHandler {
    void setStackInSlot(int slot, @NotNull ItemStack stack);
}