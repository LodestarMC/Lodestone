package team.lodestar.lodestone.handlers;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import team.lodestar.lodestone.helpers.ItemHelper;
import team.lodestar.lodestone.systems.item.IEventResponderItem;

/**
 * A handler for firing {@link IEventResponderItem} events
 */
public class ItemEventHandler {

    public static void respondToDeath(LivingDeathEvent event) {
        if (event.isCanceled()) {
            return;
        }
        LivingEntity target = event.getEntity();
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

    public static void respondToHurt(LivingDamageEvent.Post event) {
        if (event.getNewDamage() <= 0) return;

        LivingEntity target = event.getEntity();
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ItemHelper.getEventResponders(attacker).forEach(s -> ((IEventResponderItem) s.getItem()).hurtEvent(event, attacker, target, s));
            ItemHelper.getEventResponders(target).forEach(s -> ((IEventResponderItem) s.getItem()).takeDamageEvent(event, attacker, target, s));
        }
    }
}