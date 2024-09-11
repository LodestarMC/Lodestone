package team.lodestar.lodestone.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import team.lodestar.lodestone.handlers.ItemEventHandler;
import team.lodestar.lodestone.handlers.LodestoneAttributeEventHandler;
import team.lodestar.lodestone.handlers.WorldEventHandler;

@EventBusSubscriber
public class RuntimeEvents {

    @SubscribeEvent
    public static void onHurt(LivingDamageEvent event) {
        if (event instanceof LivingDamageEvent.Pre preEvent) {
            LodestoneAttributeEventHandler.processAttributes(preEvent);
        }
        else if (event instanceof LivingDamageEvent.Post postEvent) {
            ItemEventHandler.respondToHurt(postEvent);
        }
    }

    @SubscribeEvent
    public static void entityJoin(EntityJoinLevelEvent event) {
        WorldEventHandler.playerJoin(event);
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
    public static void worldTick(LevelTickEvent event) {
        WorldEventHandler.worldTick(event);
    }
}