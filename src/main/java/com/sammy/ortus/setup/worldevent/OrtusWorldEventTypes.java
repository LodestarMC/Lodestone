package com.sammy.ortus.setup.worldevent;

import com.sammy.ortus.systems.worldevent.WorldEventType;

import java.util.HashMap;

public class OrtusWorldEventTypes {

    public static HashMap<String, WorldEventType> EVENT_TYPES = new HashMap<>();

    public static WorldEventType registerEventType(WorldEventType eventType) {
        EVENT_TYPES.put(eventType.id, eventType);
        return eventType;
    }
}
