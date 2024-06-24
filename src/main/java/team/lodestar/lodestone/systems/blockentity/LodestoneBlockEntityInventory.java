package team.lodestar.lodestone.systems.blockentity;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemHandlerHelper;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandlerContainer;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
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
import java.util.List;
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

    public ArrayList<ItemStack> nonEmptyItemStacks = new ArrayList<>();

    public int emptyItemAmount;
    public int nonEmptyItemAmount;
    public int firstEmptyItemIndex;

    private LodestoneBlockEntity blockEntity;

    public LodestoneBlockEntityInventory(LodestoneBlockEntity blockEntity, int slotCount, int allowedItemSize, Predicate<ItemStack> inputPredicate, Predicate<ItemStack> outputPredicate) {
        this(blockEntity, slotCount, allowedItemSize, inputPredicate);
        this.outputPredicate = outputPredicate;
    }

    public LodestoneBlockEntityInventory(LodestoneBlockEntity blockEntity, int slotCount, int allowedItemSize, Predicate<ItemStack> inputPredicate) {
        this(blockEntity, slotCount, allowedItemSize);
        this.inputPredicate = inputPredicate;
    }

    public LodestoneBlockEntityInventory(LodestoneBlockEntity blockEntity, int slotCount, int allowedItemSize) {
        super(slotCount);
        this.slotCount = slotCount;
        this.allowedItemSize = allowedItemSize;
        updateData();
        this.blockEntity = blockEntity;
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
        NonNullList<ItemStack> stacks = convertToNonNullItemStacks(getSlots());
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

    public static ArrayList<ItemStack> convertToItemStacks(ArrayList<SingleSlotStorage<ItemVariant>> nonEmptyItemStacks) {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();

        for (SingleSlotStorage<ItemVariant> slot : nonEmptyItemStacks) {
            // Get the ItemVariant from the slot
            ItemVariant itemVariant = slot.getResource();

            // Convert ItemVariant to ItemStack
            ItemStack itemStack = itemVariant.toStack((int) slot.getAmount());

            // Add the ItemStack to the new list
            itemStacks.add(itemStack);
        }

        return itemStacks;
    }

    public static NonNullList<ItemStack> convertToNonNullItemStacks(List<SingleSlotStorage<ItemVariant>> nonEmptyItemStacks) {
        NonNullList<ItemStack> itemStacks = NonNullList.create();

        for (SingleSlotStorage<ItemVariant> slot : nonEmptyItemStacks) {
            // Get the ItemVariant from the slot
            ItemVariant itemVariant = slot.getResource();

            // Convert ItemVariant to ItemStack
            ItemStack itemStack = itemVariant.toStack((int) slot.getAmount());

            // Add the ItemStack to the new list
            itemStacks.add(itemStack);
        }

        return itemStacks;
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

    public boolean interact(LodestoneBlockEntity be, Level level, Player player, InteractionHand handIn, Predicate<ItemStack> predicate) {

        if (!level.isClientSide) {
            boolean res = false;
            ItemStack held = player.getItemInHand(handIn);
            if (predicate.test(held)) {
                player.swing(handIn, true);

                if (held.isEmpty()) {
                    res = !interactExtractInv(be, player).isEmpty();
                } else {
                    res = interactInsertInv(be, held);
                }
            }
            return res;
        }
        return false;
    }

    public boolean interactInsertInv(LodestoneBlockEntity be, ItemStack stack){
        try (Transaction t = TransferUtil.getTransaction()){

            long inserted = insert(ItemVariant.of(stack), stack.getCount(), t);
            stack.shrink((int)inserted);
            t.commit();

            if (inserted > 0) {
                setChanged();
                be.notifyUpdate();
                return true;
            }
        }
        return false;
    }

    public ItemStack interactExtractInv(LodestoneBlockEntity be, Player player){
        if (!nonEmptyItemStacks.isEmpty()) {
            try (Transaction t = TransferUtil.getTransaction()) {
                ItemStack takeOutStack = nonEmptyItemStacks.get(nonEmptyItemStacks.size() - 1);
                long extracted = extract(ItemVariant.of(takeOutStack), takeOutStack.getCount(), t);
                t.commit();
                if (extracted > 0) {
                    player.getInventory().placeItemBackInInventory(takeOutStack);
                    setChanged();
                    be.notifyUpdate();
                    return takeOutStack;
                }
            }
        }
        return ItemStack.EMPTY;
    }
}