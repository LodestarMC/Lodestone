package team.lodestar.lodestone.events.types.worldevent;

import net.minecraft.world.level.Level;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;

public class WorldEventDiscardEvent extends WorldEventInstanceEvent {
    public WorldEventDiscardEvent(WorldEventInstance worldEvent, Level level) {
        super(worldEvent, level);
    }
}
