package team.lodestar.lodestone.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.lodestar.lodestone.component.LodestoneEntityDataCapability;
import team.lodestar.lodestone.component.LodestonePlayerDataCapability;
import team.lodestar.lodestone.component.LodestoneWorldDataCapability;
import team.lodestar.lodestone.handlers.ItemEventHandler;
import team.lodestar.lodestone.handlers.LodestoneAttributeEventHandler;
import team.lodestar.lodestone.handlers.PlacementAssistantHandler;
import team.lodestar.lodestone.handlers.WorldEventHandler;

@Mod.EventBusSubscriber
public class RuntimeEvents {

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        ItemEventHandler.respondToHurt(event);
        LodestoneAttributeEventHandler.processAttributes(event);
    }

    @SubscribeEvent
    public static void entityJoin(EntityJoinLevelEvent event) {
        WorldEventHandler.playerJoin(event);
        LodestonePlayerDataCapability.playerJoin(event);
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        LodestonePlayerDataCapability.playerClone(event);
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        LodestonePlayerDataCapability.playerTick(event);
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
    public static void worldTick(TickEvent.LevelTickEvent event) {
        WorldEventHandler.worldTick(event);
    }

    @SubscribeEvent
    public static void attachWorldCapability(AttachCapabilitiesEvent<Level> event) {
        LodestoneWorldDataCapability.attachWorldCapability(event);
    }

    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        LodestonePlayerDataCapability.attachPlayerCapability(event);
        LodestoneEntityDataCapability.attachEntityCapability(event);
    }

    @SubscribeEvent
    public static void startTracking(PlayerEvent.StartTracking event) {
        LodestonePlayerDataCapability.syncPlayerCapability(event);
        LodestoneEntityDataCapability.syncEntityCapability(event);
    }
}