package team.lodestar.lodestone.helpers;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
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

    public static ArrayList<Tuple<SlotReference, ItemStack>> getEquippedCurios(LivingEntity entity, Predicate<ItemStack> predicate) {
        var v = TrinketsApi.TRINKET_COMPONENT.maybeGet(entity);
        if (v.isPresent()) {
            TrinketComponent component = v.get();
            return new ArrayList<>(component.getEquipped(predicate));
        }
        return new ArrayList<>();
    }
}