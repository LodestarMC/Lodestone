package team.lodestar.lodestone.toolkit.block;


import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.registries.*;

import java.util.function.*;

public record BlockBlockItemHolder<T extends Block, K extends BlockItem>(DeferredHolder<Block, T> block, DeferredHolder<Item, K> item) implements Supplier<T> {

    @Override
    public T get() {
        return block.get();
    }
}