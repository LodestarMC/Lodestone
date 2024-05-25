package team.lodestar.lodestone.systems.item;

import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingHurtEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * An interface containing various methods which are triggered on various forge events.
 * Implement on your item for the methods to be called.
 */
public interface IEventResponderItem {

    default void takeDamageEvent(LivingHurtEvent event, LivingEntity attacker, LivingEntity attacked, ItemStack stack) {

    }

    default void hurtEvent(LivingHurtEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack) {

    }

    default void killEvent(LivingEntity finalAttacker, LivingEntity livingEntity, ItemStack s, DamageSource damageSource, float damageAmount) {

    }
}