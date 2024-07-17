package team.lodestar.lodestone.systems.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

/**
 * An interface containing various methods which are triggered on various forge events.
 * Implement on your item for the methods to be called.
 */
public interface IEventResponderItem {
    default void takeDamageEvent(LivingDamageEvent.Post event, LivingEntity attacker, LivingEntity attacked, ItemStack stack) {
        takeDamageEvent(attacker, attacked, stack);
    }

    default void takeDamageEvent(LivingEntity attacker, LivingEntity attacked, ItemStack stack) {

    }

    default void hurtEvent(LivingDamageEvent.Post event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
        hurtEvent(attacker, target, stack);
    }

    default void hurtEvent(LivingEntity attacker, LivingEntity target, ItemStack stack) {

    }

    default void killEvent(LivingDeathEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
        killEvent(attacker, target, stack);
    }

    default void killEvent(LivingEntity attacker, LivingEntity target, ItemStack stack) {

    }
}