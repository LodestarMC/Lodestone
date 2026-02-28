package team.lodestar.lodestone.toolkit.blockentity;

import net.minecraft.core.*;
import net.neoforged.neoforge.items.*;

public interface IItemHandlerSupplier {

    public IItemHandler getInventory(Direction direction);
}
