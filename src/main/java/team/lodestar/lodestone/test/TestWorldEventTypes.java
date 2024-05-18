package team.lodestar.lodestone.test;

import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypeRegistry;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

public class TestWorldEventTypes {
    public static final WorldEventType TEST = LodestoneWorldEventTypeRegistry.registerEventType(new WorldEventType("test", TestWorldEvent::new));
}
