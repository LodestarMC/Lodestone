package team.lodestar.lodestone.handlers.item_events;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.AddAttributeTooltipsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import javax.annotation.Nullable;

/**
 * An interface containing various methods which are triggered alongside various forge events.
 * Implement on your item for the methods to be called.
 * Does not necessarily have to be bound to an itemstack.
 */
public interface IEventResponderItem {

    default void modifyAttributeTooltipEvent(AddAttributeTooltipsEvent event) {

    }

    //TODO: the naming here is confusing. We have the `Incoming` event which is ran before any damage is done
    // We also have Our Incoming and Outgoing wrappers for LivingDamageEvent Pre and Post
    // Need to figure out a nice naming consistency here
    default void incomingDamageEvent(LivingIncomingDamageEvent event, @Nullable LivingEntity attacker, LivingEntity target, ItemStack stack) {
    }

    default void outgoingDamageEvent(LivingIncomingDamageEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
    }

    default void incomingDamageEvent(LivingDamageEvent.Pre event, @Nullable LivingEntity attacker, LivingEntity target, ItemStack stack) {
    }

    default void outgoingDamageEvent(LivingDamageEvent.Pre event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
    }

    default void finalizedIncomingDamageEvent(LivingDamageEvent.Post event, @Nullable LivingEntity attacker, LivingEntity target, ItemStack stack) {
    }

    default void finalizedOutgoingDamageEvent(LivingDamageEvent.Post event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
    }

    default void incomingDeathEvent(LivingDeathEvent event, @Nullable LivingEntity attacker, LivingEntity target, ItemStack stack) {
    }

    default void outgoingDeathEvent(LivingDeathEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
    }
}
