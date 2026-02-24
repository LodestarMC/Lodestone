package team.lodestar.lodestone.handlers.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.*;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import team.lodestar.lodestone.handlers.item.*;
import team.lodestar.lodestone.handlers.worldevent.*;
import team.lodestar.lodestone.systems.enchanting.*;
import team.lodestar.lodestone.handlers.attributes.*;

@EventBusSubscriber
public class GameEvents {

    @SubscribeEvent
    public static void modifyAttributes(ItemAttributeModifierEvent event) {
        LodestoneSlotBasedEnchantmentAttributeEffect.modifyAttributes(event);
    }

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        ItemizedEventHandler.triggerHurtResponses(event);
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent.Pre event) {
        MagicDamageHandler.processAttributes(event);
        ItemizedEventHandler.triggerHurtResponses(event);
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent.Post event) {
        ItemizedEventHandler.triggerHurtResponses(event);
        MagicDamageHandler.triggerMagicDamage(event);
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        ItemizedEventHandler.triggerDeathResponses(event);
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