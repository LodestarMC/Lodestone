package team.lodestar.lodestone.registry.common;

import team.lodestar.lodestone.systems.worldevent.WorldEventType;

import java.util.HashMap;

public class LodestoneWorldEventTypeRegistry {

    public static HashMap<String, WorldEventType> EVENT_TYPES = new HashMap<>();

    public static WorldEventType registerEventType(WorldEventType eventType) {
        EVENT_TYPES.put(eventType.id, eventType);
        return eventType;
    }
}
