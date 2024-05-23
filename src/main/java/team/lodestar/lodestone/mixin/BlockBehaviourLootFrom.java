package team.lodestar.lodestone.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public interface BlockBehaviourLootFrom {
    BlockBehaviour.Properties lootFrom(Supplier<? extends Block> blockIn);
}
