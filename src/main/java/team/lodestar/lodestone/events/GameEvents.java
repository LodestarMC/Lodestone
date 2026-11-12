package team.lodestar.lodestone.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.*;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import team.lodestar.lodestone.handlers.item_events.ItemEventHandler;
import team.lodestar.lodestone.modules.toolkit.enchanting.*;
import team.lodestar.lodestone.modules.toolkit.worldevent.*;

@EventBusSubscriber
public class GameEvents {

    @SubscribeEvent
    public static void modifyAttributes(ItemAttributeModifierEvent event) {
        LodestoneSlotBasedEnchantmentAttributeEffect.modifyAttributes(event);
    }

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        ItemEventHandler.triggerHurtResponses(event);
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent.Pre event) {
        ItemEventHandler.triggerHurtResponses(event);
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent.Post event) {
        ItemEventHandler.triggerHurtResponses(event);
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        ItemEventHandler.triggerDeathResponses(event);
    }

    @SubscribeEvent
    public static void entityJoin(EntityJoinLevelEvent event) {
        WorldEventHandler.playerJoin(event);
    }

    @SubscribeEvent
    public static void worldTick(LevelTickEvent.Post event) {
        WorldEventHandler.worldTick(event);
    }
}