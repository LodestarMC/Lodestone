package team.lodestar.lodestone.systems.blockentity;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemHandlerHelper;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandlerContainer;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
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
import org.jetbrains.annotations.NotNull;
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
    }

    @Override
    public int getSlotLimit(int slot) {
        return allowedItemSize;
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

    private ItemStack extractItem(Level level, ItemStack held, Player player) {
        return (held); //TODO
    }

    private ItemStack insertItem(Player player, ItemStack held) {
        return held; //TODO
    }
}