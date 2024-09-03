package team.lodestar.lodestone.systems.datagen.itemsmith;

import net.minecraft.world.item.*;
import team.lodestar.lodestone.systems.datagen.providers.*;

import java.util.function.*;

public class ItemModelSmithData {
    public final LodestoneItemModelProvider provider;
    public final Consumer<Supplier<? extends Item>> consumer;

    public ItemModelSmithData(LodestoneItemModelProvider provider, Consumer<Supplier<? extends Item>> consumer) {
        this.provider = provider;
        this.consumer = consumer;
    }
}
