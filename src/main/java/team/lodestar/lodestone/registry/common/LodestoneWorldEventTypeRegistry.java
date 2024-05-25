package team.lodestar.lodestone.registry.common;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

import java.util.HashMap;

public class LodestoneWorldEventTypeRegistry {

    public static HashMap<ResourceLocation, WorldEventType> EVENT_TYPES = new HashMap<>();

    public static WorldEventType registerEventType(WorldEventType eventType) {
        EVENT_TYPES.put(eventType.id, eventType);
        return eventType;
    }
}
