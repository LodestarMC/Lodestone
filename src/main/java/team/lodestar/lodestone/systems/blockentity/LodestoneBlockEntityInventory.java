package team.lodestar.lodestone.systems.blockentity;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandlerContainer;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.helpers.BlockHelper;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A powerful ItemStackHandler designed to work with block entities
 */
public class LodestoneBlockEntityInventory extends ItemStackHandlerContainer {
    public final int slotCount;
    public final int allowedItemSize;
    public Predicate<ItemStack> inputPredicate;
    public Predicate<ItemStack> outputPredicate;
    public final LazyOptional<?> inventoryOptional = LazyOptional.of(() -> this);

    public ArrayList<SingleSlotStorage<ItemVariant>> nonEmptyItemStacks = new ArrayList<>();

    public int emptyItemAmount;
    public int nonEmptyItemAmount;
    public int firstEmptyItemIndex;

    private LodestoneBlockEntity blockEntity;

    public LodestoneBlockEntityInventory(int slotCount, int allowedItemSize, Predicate<ItemStack> inputPredicate, Predicate<ItemStack> outputPredicate) {
        this(slotCount, allowedItemSize, inputPredicate);
        this.outputPredicate = outputPredicate;
    }

    public LodestoneBlockEntityInventory(int slotCount, int allowedItemSize, Predicate<ItemStack> inputPredicate) {
        this(slotCount, allowedItemSize);
        this.inputPredicate = inputPredicate;
    }

    public LodestoneBlockEntityInventory(int slotCount, int allowedItemSize) {
        super(slotCount);
        this.slotCount = slotCount;
        this.allowedItemSize = allowedItemSize;
        updateData();
    }

    @Override
    public int getSlotLimit(int slot) {
        return allowedItemSize;
    }

    public boolean isEmpty() {
        return nonEmptyItemAmount == 0;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        updateData();
    }

    @Override
    public CompoundTag serializeNBT() {
        return super.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        updateData();
    }

    public void clear() {
        for (int i = 0; i < slotCount; i++) {
            setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public void updateData() {
        var stacks = getSlots();
        nonEmptyItemStacks = stacks.stream().filter(s -> !s.getSlots().isEmpty()).collect(Collectors.toCollection(ArrayList::new));
        nonEmptyItemAmount = nonEmptyItemStacks.size();
        emptyItemAmount = (int) stacks.stream().filter(storageViews -> storageViews.getSlots().isEmpty()).count();
        for (int i = 0; i < stacks.size(); i++) {
            var stack = stacks.get(i);
            if (stack.isResourceBlank()) {
                firstEmptyItemIndex = i;
                return;
            }
        }
        firstEmptyItemIndex = -1;
    }

    public void dumpItems(Level level, BlockPos pos) {
        dumpItems(level, BlockHelper.fromBlockPos(pos).add(0.5, 0.5, 0.5));
    }

    public void dumpItems(Level level, Vec3 pos) {
        for (int i = 0; i < slotCount; i++) {
            if (!getStackInSlot(i).isEmpty()) {
                level.addFreshEntity(new ItemEntity(level, pos.x(), pos.y(), pos.z(), getStackInSlot(i)));
            }
            setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public void interact(Level level, Player player, InteractionHand handIn) {
        if (!level.isClientSide) {
            ItemStack held = player.getItemInHand(handIn);
            player.swing(handIn, true);
            int size = nonEmptyItemStacks.size() - 1;
            if ((held.isEmpty() || firstEmptyItemIndex == -1) && size != -1) {
                ItemStack takeOutStack = nonEmptyItemStacks.get(size).getResource().toStack();
                if (takeOutStack.getItem().equals(held.getItem())) {
                    TransferUtil.insertItem(this, held);
                    return;
                }
                long extractedStack = TransferUtil.extractItem(this, held);
                if (extractedStack > 0) {
                    TransferUtil.insertItem(this, held);
                }
            } else {
                TransferUtil.insertItem(this, held);
            }
        }
    }
}