package team.lodestar.lodestone.mixin;


import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(BlockBehaviour.Properties.class)
public class BlockBehaviourPropertiesMixin implements BlockBehaviourLootFrom {




    @Override
    public BlockBehaviour.Properties lootFrom(Supplier<? extends Block> blockIn) {
        this.lootTableSupplier = () -> blockIn.get().getLootTable();
        return this;
    }
}
