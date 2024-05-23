package team.lodestar.lodestone.helpers;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class CurioHelper {

    public static Optional<Tuple<SlotReference, ItemStack>> getEquippedCurio(LivingEntity entity, Predicate<ItemStack> predicate) {
        return TrinketsApi.TRINKET_COMPONENT.maybeGet(entity).flatMap(trinketComponent -> trinketComponent.getEquipped(predicate).stream().findFirst());
    }

    public static Optional<Tuple<SlotReference, ItemStack>> getEquippedCurio(LivingEntity entity, Item curio) {
        var comp = TrinketsApi.TRINKET_COMPONENT.maybeGet(entity);
        return comp.flatMap(trinketComponent -> trinketComponent.getEquipped(curio).stream().findFirst());
    }

    public static boolean hasCurioEquipped(LivingEntity entity, Item curio) {
        return getEquippedCurio(entity, curio).isPresent();
    }

    public static List<Tuple<SlotReference, ItemStack>> getEquippedCurios(LivingEntity entity) {
        var v = TrinketsApi.TRINKET_COMPONENT.maybeGet(entity);
        if (v.isPresent()) {
            TrinketComponent component = v.get();
            return component.getAllEquipped();
        }
        return List.of();
    }

    public static ArrayList<ItemStack> getEquippedCurios(LivingEntity entity, Predicate<ItemStack> predicate) {
        Optional<IItemHandlerModifiable> optional = CuriosApi.getCuriosHelper().getEquippedCurios(entity).resolve();
        ArrayList<ItemStack> stacks = new ArrayList<>();
        if (optional.isPresent()) {
            IItemHandlerModifiable handler = optional.get();
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (predicate.test(stack)) {
                    stacks.add(stack);
                }
            }
        }
        return stacks;
    }

    public static Optional<ImmutableTriple<String, Integer, ItemStack>> findCosmeticCurio(Predicate<ItemStack> filter, LivingEntity livingEntity) {
        ImmutableTriple<String, Integer, ItemStack> result = CuriosApi.getCuriosHelper().getCuriosHandler(livingEntity).map(handler ->
        {
            Map<String, ICurioStacksHandler> curios = handler.getCurios();

            for (String id : curios.keySet()) {
                ICurioStacksHandler stacksHandler = curios.get(id);
                IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                IDynamicStackHandler cosmeticStackHelper = stacksHandler.getCosmeticStacks();

                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    ItemStack stack = stackHandler.getStackInSlot(i);

                    if (!stack.isEmpty() && filter.test(stack)) {
                        return new ImmutableTriple<>(id, i, stack);
                    }
                }
                for (int i = 0; i < cosmeticStackHelper.getSlots(); i++) {
                    ItemStack stack = cosmeticStackHelper.getStackInSlot(i);

                    if (!stack.isEmpty() && filter.test(stack)) {
                        return new ImmutableTriple<>(id, i, stack);
                    }
                }
            }
            return new ImmutableTriple<>("", 0, ItemStack.EMPTY);
        }).orElse(new ImmutableTriple<>("", 0, ItemStack.EMPTY));

        return result.getLeft().isEmpty() ? Optional.empty() : Optional.of(result);
    }
}