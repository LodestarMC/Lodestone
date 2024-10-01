package team.lodestar.lodestone.events;

import team.lodestar.lodestone.handlers.ItemEventHandler;
import team.lodestar.lodestone.handlers.LodestoneAttributeEventHandler;
import team.lodestar.lodestone.handlers.WorldEventHandler;

public class RuntimeEvents {

    public static void onHurt(LivingDamageEvent.Post event) {
        ItemEventHandler.respondToHurt(event);
    }

    public static void onHurt(LivingDamageEvent.Pre event) {
        LodestoneAttributeEventHandler.processAttributes(event);
    }

    public static void entityJoin(EntityJoinLevelEvent event) {
        WorldEventHandler.playerJoin(event);
    }

    public static void onDeath(LivingDeathEvent event) {
        ItemEventHandler.respondToDeath(event);
    }

    public static void worldTick(LevelTickEvent.Post event) {
        WorldEventHandler.worldTick(event);
    }
}