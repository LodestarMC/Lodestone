package com.sammy.ortus.handlers;

import com.sammy.ortus.helpers.ItemHelper;
import com.sammy.ortus.systems.item.IEventResponderItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

/**
 * A handler for firing {@link IEventResponderItem} events
 */
public class ItemEventHandler {

    public static void respondToDeath(LivingDeathEvent event) {
        if (event.isCanceled()) {
            return;
        }
        LivingEntity target = event.getEntityLiving();
        LivingEntity attacker = null;
        if (event.getSource().getEntity() instanceof LivingEntity directAttacker) {
            attacker = directAttacker;
        }
        if (attacker == null) {
            attacker = target.getLastHurtByMob();
        }
        if (attacker != null) {
            LivingEntity finalAttacker = attacker;
            ItemHelper.getEventResponders(attacker).forEach(s -> ((IEventResponderItem) s.getItem()).killEvent(event, finalAttacker, target, s));
        }
    }

    public static void respondToHurt(LivingHurtEvent event) {
        if (event.isCanceled() || event.getAmount() <= 0) {
            return;
        }
        LivingEntity target = event.getEntityLiving();
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ItemHelper.getEventResponders(attacker).forEach(s -> ((IEventResponderItem) s.getItem()).hurtEvent(event, attacker, target, s));
            ItemHelper.getEventResponders(target).forEach(s -> ((IEventResponderItem) s.getItem()).takeDamageEvent(event, attacker, target, s));
        }
    }
}