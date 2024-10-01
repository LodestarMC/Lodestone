package team.lodestar.lodestone.helpers;

import dev.emi.trinkets.api.SlotReference;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemHandlerHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.compability.TrinketCompat;
import team.lodestar.lodestone.systems.item.IEventResponderItem;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ItemHelper {
    public static ArrayList<ItemStack> copyWithNewCount(List<ItemStack> stacks, int newCount) {
        ArrayList<ItemStack> newStacks = new ArrayList<>();
        for (ItemStack stack : stacks) {
            ItemStack copy = stack.copy();
            copy.setCount(newCount);
            newStacks.add(copy);
        }
        return newStacks;
    }

    public static ItemStack copyWithNewCount(ItemStack stack, int newCount) {
        ItemStack newStack = stack.copy();
        newStack.setCount(newCount);
        return newStack;
    }

    public static <T extends Entity> Entity getClosestEntity(List<T> entities, Vec3 pos) {
        double cachedDistance = -1.0D;
        Entity resultEntity = null;

        for (T entity : entities) {
            double newDistance = entity.distanceToSqr(pos.x, pos.y, pos.z);
            if (cachedDistance == -1.0D || newDistance < cachedDistance) {
                cachedDistance = newDistance;
                resultEntity = entity;
            }

        }
        return resultEntity;
    }

    public static ArrayList<ItemStack> nonEmptyStackList(ArrayList<ItemStack> stacks) {
        ArrayList<ItemStack> nonEmptyStacks = new ArrayList<>();
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty()) {
                nonEmptyStacks.add(stack);
            }
        }
        return nonEmptyStacks;
    }

    public static ArrayList<ItemStack> getEventResponders(LivingEntity attacker) {
        ArrayList<Tuple<SlotReference, ItemStack>> equippedCurios
                = TrinketCompat.LOADED ? TrinketsHelper.getEquippedTrinkets(attacker, p -> p.getItem() instanceof IEventResponderItem) : new ArrayList<>();

        ArrayList<ItemStack> itemStacks = TrinketCompat.LOADED ? new ArrayList<>(equippedCurios.stream().map(Tuple::getB).toList()) : new ArrayList<>();
        ItemStack stack = attacker.getMainHandItem();
        if (stack.getItem() instanceof IEventResponderItem) {
            itemStacks.add(stack);
        }
        attacker.getArmorSlots().forEach(s -> {
            if (s.getItem() instanceof IEventResponderItem) {
                itemStacks.add(s);
            }
        });
        return itemStacks;
    }

    public static void applyEnchantments(LivingEntity user, Entity target, DamageSource source, ItemStack stack) {
        if (user.level() instanceof ServerLevel serverLevel) {
            EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, target, source, stack);
        }
    }

    public static void giveItemToEntity(LivingEntity entity, ItemStack stack) {
        if (entity instanceof Player player) {
            ItemHandlerHelper.giveItemToPlayer(player, stack);
        } else {
            spawnItemOnEntity(entity, stack);
        }
    }

    public static void quietlyGiveItemToPlayer(Player player, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        Level level = player.level();
        ItemStack remainder = stack;
        if (!remainder.isEmpty()) {
            ItemHandlerHelper.giveItemToPlayer(player, remainder);
        }
    }

    public static void spawnItemOnEntity(LivingEntity entity, ItemStack stack) {
        Level level = entity.level();
        ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY() + 0.5, entity.getZ(), stack);
        itemEntity.setPickUpDelay(40);
        itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().multiply(0, 1, 0));
        level.addFreshEntity(itemEntity);
    }
}