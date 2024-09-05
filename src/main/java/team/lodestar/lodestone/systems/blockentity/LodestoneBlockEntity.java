package team.lodestar.lodestone.systems.blockentity;

import io.github.fabricators_of_create.porting_lib.block.CustomDataPacketHandlingBlockEntity;
import io.github.fabricators_of_create.porting_lib.block.CustomUpdateTagHandlingBlockEntity;
import io.github.fabricators_of_create.porting_lib.extensions.extensions.BlockEntityExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.systems.block.LodestoneEntityBlock;

/**
 * A simple block entity with various frequently used methods called from {@link LodestoneEntityBlock}
 */
public class LodestoneBlockEntity extends BlockEntity implements BlockEntityExtensions, CustomUpdateTagHandlingBlockEntity, CustomDataPacketHandlingBlockEntity {

    public boolean needsSync;

    public LodestoneBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void onBreak(@Nullable Player player) {
        invalidateCaps();
    }

    public void onPlace(LivingEntity placer, ItemStack stack) {
    }

    public void onNeighborUpdate(BlockState state, BlockPos pos, BlockPos neighbor) {
    }

    public ItemStack onClone(BlockState state, BlockGetter level, BlockPos pos) {
        return ItemStack.EMPTY;
    }

    public InteractionResult onUse(Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    public void onEntityInside(BlockState state, Level level, BlockPos pos, Entity entity) {

    }

    @Override
    public void load(CompoundTag pTag) {
        needsSync = true;
        super.load(pTag);
    }

    public void tick() {
        if (needsSync) {
            init();
            needsSync = false;
        }
    }

    public void init() {

    }

    //Sync
    @Override
    public CompoundTag getUpdateTag() {
        return writeClient(new CompoundTag());
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        readClient(tag);
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
        CompoundTag tag = packet.getTag();
        readClient(tag == null ? new CompoundTag() : tag);
    }

    // Special handling for client update packets
    public void readClient(CompoundTag tag) {
        load(tag);
    }

    // Special handling for client update packets
    public CompoundTag writeClient(CompoundTag tag) {
        saveAdditional(tag);
        return tag;
    }

    public void sendData() {
        if (level instanceof ServerLevel serverLevel)
            serverLevel.getChunkSource().blockChanged(getBlockPos());
    }

    public void notifyUpdate() {
        setChanged();
        sendData();
    }

    public LevelChunk containedChunk() {
        return level.getChunkAt(worldPosition);
    }

    @Override
    public void deserializeNBT(BlockState state, CompoundTag nbt) {
        this.load(nbt);
    }
}