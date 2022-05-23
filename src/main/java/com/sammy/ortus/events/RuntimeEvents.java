package com.sammy.ortus.events;

import com.sammy.ortus.capability.OrtusEntityDataCapability;
import com.sammy.ortus.capability.OrtusPlayerDataCapability;
import com.sammy.ortus.capability.OrtusWorldDataCapability;
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
        OrtusPlayerDataCapability.playerJoin(event);
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        OrtusPlayerDataCapability.playerClone(event);
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        OrtusPlayerDataCapability.playerTick(event);
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
        OrtusWorldDataCapability.attachWorldCapability(event);
    }

    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        OrtusPlayerDataCapability.attachPlayerCapability(event);
        OrtusEntityDataCapability.attachEntityCapability(event);
    }

    @SubscribeEvent
    public static void startTracking(PlayerEvent.StartTracking event) {
        OrtusPlayerDataCapability.syncPlayerCapability(event);
        OrtusEntityDataCapability.syncEntityCapability(event);
    }
}