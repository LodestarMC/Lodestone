package team.lodestar.lodestone.toolkit.blockentity;

import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
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
import team.lodestar.lodestone.helpers.block.*;
import team.lodestar.lodestone.systems.block.LodestoneEntityBlock;

import javax.annotation.Nonnull;

/**
 * A simple block entity with various methods normally found inside of Block delegated here from {@link LodestoneEntityBlock}
 */
public class LodestoneBlockEntity extends BlockEntity {

    public LodestoneBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return this.saveWithoutMetadata(pRegistries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onBreak(@Nullable Player player) {
    }

    public void onPlace(LivingEntity placer, ItemStack stack) {
    }

    public void onNeighborUpdate(BlockState state, BlockPos pos, BlockPos neighbor) {
    }

    public ItemStack onClone(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
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

    /**
     * A method designed to run anytime substantial changes to the entity are made.
     * Called the tick after the entity is updated from the server to the client, or loaded from memory
     */
    public void update(@Nonnull Level level) {

    }

    public void serverTick(ServerLevel level) {

    }

    public void clientTick(Level level) {

    }

    @Deprecated
    public void tick() {
    }

    public void commonTick(Level level) {
    }


    public void playSound(SoundEvent soundEvent) {
        playSound(soundEvent, 1);
    }

    public void playSound(SoundEvent soundEvent, float volume) {
        playSound(soundEvent, volume, 1);
    }

    @SuppressWarnings("DataFlowIssue")
    public void playSound(SoundEvent soundEvent, float volume, float pitch) {
        level.playSound(null, worldPosition, soundEvent, SoundSource.BLOCKS, 1, 1f);
    }

    public void setDirty() {
        BlockStateHelper.updateState(level, worldPosition);
    }

    @SuppressWarnings("DataFlowIssue")
    public void notifyObservers() {
        getBlockState().updateNeighbourShapes(level, worldPosition, 2);
    }
}