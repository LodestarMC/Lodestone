package team.lodestar.lodestone.systems.blockentity;

import io.github.fabricators_of_create.porting_lib.block.CustomDataPacketHandlingBlockEntity;
import io.github.fabricators_of_create.porting_lib.block.CustomUpdateTagHandlingBlockEntity;
import io.github.fabricators_of_create.porting_lib.extensions.extensions.BlockEntityExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import team.lodestar.lodestone.systems.block.LodestoneEntityBlock;

/**
 * A simple block entity with various frequently used methods called from {@link LodestoneEntityBlock}
 */
public class LodestoneBlockEntity extends BlockEntity implements CustomUpdateTagHandlingBlockEntity, CustomDataPacketHandlingBlockEntity {

    public boolean needsSync;

    public LodestoneBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void onBreak(@Nullable Player player) {
    }

    public void onPlace(LivingEntity placer, ItemStack stack) {
    }

    public void onNeighborUpdate(BlockState state, BlockPos pos, BlockPos neighbor) {
    }

    public ItemStack onClone(BlockState state, BlockGetter level, BlockPos pos) {
        return ItemStack.EMPTY;
    }

    public ItemInteractionResult onUse(Player pPlayer, InteractionHand pHand) {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public InteractionResult onUseWithoutItem(Player pPlayer) {
        return InteractionResult.PASS;
    }

    public ItemInteractionResult onUseWithItem(Player pPlayer, ItemStack pStack, InteractionHand pHand) {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public void onEntityInside(BlockState state, Level level, BlockPos pos, Entity entity) {

    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return this.saveWithoutMetadata(pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        needsSync = true;
        super.loadAdditional(pTag, pRegistries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        handleUpdateTag(getUpdatePacket().getTag(), lookupProvider);
    }

    public void tick() {
        if (needsSync) {
            init();
            needsSync = false;
        }
    }

    public void init() {

    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {

    }
}