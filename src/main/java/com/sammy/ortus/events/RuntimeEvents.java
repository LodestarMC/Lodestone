package com.sammy.ortus.events;

import com.sammy.ortus.capability.EntityDataCapability;
import com.sammy.ortus.capability.PlayerDataCapability;
import com.sammy.ortus.capability.WorldDataCapability;
import com.sammy.ortus.handlers.OrtusAttributeEventHandler;
import com.sammy.ortus.handlers.ItemEventHandler;
import com.sammy.ortus.handlers.PlacementAssistantHandler;
import com.sammy.ortus.handlers.WorldEventHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class RuntimeEvents {

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        ItemEventHandler.respondToHurt(event);
        OrtusAttributeEventHandler.processAttributes(event);
    }

    @SubscribeEvent
    public static void entityJoin(EntityJoinWorldEvent event) {
        WorldEventHandler.playerJoin(event);
        PlayerDataCapability.playerJoin(event);
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        PlayerDataCapability.playerClone(event);
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        PlayerDataCapability.playerTick(event);
    }

    @SubscribeEvent
    public static void placeBlock(PlayerInteractEvent.RightClickBlock event) {
        PlacementAssistantHandler.placeBlock(event);
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        ItemEventHandler.respondToDeath(event);
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        WorldEventHandler.worldTick(event);
    }

    @SubscribeEvent
    public static void attachWorldCapability(AttachCapabilitiesEvent<Level> event) {
        WorldDataCapability.attachWorldCapability(event);
    }

    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        PlayerDataCapability.attachPlayerCapability(event);
        EntityDataCapability.attachEntityCapability(event);
    }

    @SubscribeEvent
    public static void startTracking(PlayerEvent.StartTracking event) {
        PlayerDataCapability.syncPlayerCapability(event);
        EntityDataCapability.syncEntityCapability(event);
    }
}