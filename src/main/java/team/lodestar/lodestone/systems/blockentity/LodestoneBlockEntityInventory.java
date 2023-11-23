package team.lodestar.lodestone.systems.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import team.lodestar.lodestone.helpers.BlockHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A powerful ItemStackHandler designed to work with block entities
 */
public class LodestoneBlockEntityInventory extends ItemStackHandler {
    public final int slotCount;
    public final int allowedItemSize;
    public Predicate<ItemStack> inputPredicate;
    public Predicate<ItemStack> outputPredicate;
    public final LazyOptional<IItemHandler> inventoryOptional = LazyOptional.of(() -> this);

    public ArrayList<ItemStack> nonEmptyItemStacks = new ArrayList<>();

    public int emptyItemAmount;
    public int nonEmptyItemAmount;
    public int firstEmptyItemIndex;

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
    public void onContentsChanged(int slot) {
        updateData();
    }

    @Override
    public int getSlots() {
        return slotCount;
    }

    @Override
    public int getSlotLimit(int slot) {
        return allowedItemSize;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (inputPredicate != null) {
            if (!inputPredicate.test(stack)) {
                return false;
            }
        }
        return super.isItemValid(slot, stack);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (outputPredicate != null) {
            if (!outputPredicate.test(super.extractItem(slot, amount, true))) {
                return ItemStack.EMPTY;
            }
        }
        return super.extractItem(slot, amount, simulate);
    }

    public void updateData() {
        NonNullList<ItemStack> stacks = getStacks();
        nonEmptyItemStacks = stacks.stream().filter(s -> !s.isEmpty()).collect(Collectors.toCollection(ArrayList::new));
        nonEmptyItemAmount = nonEmptyItemStacks.size();
        emptyItemAmount = (int) stacks.stream().filter(ItemStack::isEmpty).count();
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = stacks.get(i);
            if (stack.isEmpty()) {
                firstEmptyItemIndex = i;
                return;
            }
        }
        firstEmptyItemIndex = -1;
    }

    public void load(CompoundTag compound) {
        load(compound, "inventory");
    }

    public void load(CompoundTag compound, String name) {
        deserializeNBT(compound.getCompound(name));
        if (stacks.size() != slotCount) {
            int missing = slotCount - stacks.size();
            for (int i = 0; i < missing; i++) {
                stacks.add(ItemStack.EMPTY);
            }
        }
        updateData();
    }

    public void save(CompoundTag compound) {
        save(compound, "inventory");
    }

    public void save(CompoundTag compound, String name) {
        compound.put(name, serializeNBT());
    }

    public NonNullList<ItemStack> getStacks() {
        return stacks;
    }

    public boolean isEmpty() {
        return nonEmptyItemAmount == 0;
    }

    public void clear() {
        for (int i = 0; i < slotCount; i++) {
            setStackInSlot(i, ItemStack.EMPTY);
        }
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

    public ItemStack interact(Level level, Player player, InteractionHand handIn) {
        if (!level.isClientSide) {
            ItemStack held = player.getItemInHand(handIn);
            player.swing(handIn, true);
            int size = nonEmptyItemStacks.size() - 1;
            if ((held.isEmpty() || firstEmptyItemIndex == -1) && size != -1) {
                ItemStack takeOutStack = nonEmptyItemStacks.get(size);
                if (takeOutStack.getItem().equals(held.getItem())) {
                    return insertItem(player, held);
                }
                ItemStack extractedStack = extractItem(level, held, player);
                boolean success = !extractedStack.isEmpty();
                if (success) {
                    insertItem(player, held);
                }
                return extractedStack;
            } else {
                return insertItem(player, held);
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack extractItem(Level level, ItemStack heldStack, Player player) {
        if (!level.isClientSide) {
            List<ItemStack> nonEmptyStacks = this.nonEmptyItemStacks;
            if (nonEmptyStacks.isEmpty()) {
                return heldStack;
            }
            ItemStack takeOutStack = nonEmptyStacks.get(nonEmptyStacks.size() - 1);
            int slot = stacks.indexOf(takeOutStack);
            if (extractItem(slot, takeOutStack.getCount(), true).equals(ItemStack.EMPTY)) {
                return heldStack;
            }
            extractItem(player, takeOutStack, slot);
            return takeOutStack;
        }
        return ItemStack.EMPTY;
    }

    public void extractItem(Player playerEntity, ItemStack stack, int slot) {
        ItemHandlerHelper.giveItemToPlayer(playerEntity, stack, playerEntity.getInventory().selected);
        setStackInSlot(slot, ItemStack.EMPTY);
    }

    public ItemStack insertItem(Player playerEntity, ItemStack stack) {
        return insertItem(stack);
    }
    public ItemStack insertItem(ItemStack stack) {
        if (!stack.isEmpty()) {
            ItemStack simulate = insertItem(stack, true);
            if (simulate.equals(stack)) {
                return ItemStack.EMPTY;
            }
            int count = stack.getCount() - simulate.getCount();
            if (count > allowedItemSize) {
                count = allowedItemSize;
            }
            ItemStack input = stack.split(count);
            insertItem(input, false);
            return input;
        }
        return ItemStack.EMPTY;
    }

    public ItemStack insertItem(ItemStack stack, boolean simulate) {
        return ItemHandlerHelper.insertItem(this, stack, simulate);
    }
}