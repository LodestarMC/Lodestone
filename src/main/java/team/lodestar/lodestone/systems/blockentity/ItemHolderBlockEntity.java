package team.lodestar.lodestone.systems.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;


/**
 * A simple block entity which holds a single ItemStack
 */
public abstract class ItemHolderBlockEntity extends LodestoneBlockEntity {
    public LodestoneBlockEntityInventory inventory;

    public ItemHolderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public InteractionResult onUse(Player player, InteractionHand hand) {
        inventory.interact(this, player.level(), player, hand, s -> true);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onBreak(@Nullable Player player) {
        inventory.dumpItems(level, worldPosition);
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        inventory.serializeNBT();
        super.saveAdditional(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        inventory.deserializeNBT(compound);
        super.load(compound);
    }
}