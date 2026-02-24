package team.lodestar.lodestone.systems.datagen.itemsmith;

import net.minecraft.world.item.Item;
import team.lodestar.lodestone.systems.datagen.providers.LodestoneItemModelProvider;

import java.util.function.Supplier;

public class EmptyItemModelSmith extends ItemModelSmith{
    public EmptyItemModelSmith() {
        super(null);
    }

    @Override
    public ItemModelSmithResult act(ItemModelSmithData data, Supplier<? extends Item> registryObject) {
        return null;
    }

    @Override
    public ItemModelSmithResult act(LodestoneItemModelProvider provider, Supplier<? extends Item> registryObject) {
        return null;
    }
}
