package team.lodestar.lodestone.events.types.worldevent;

import net.minecraft.world.level.Level;
import net.neoforged.bus.api.ICancellableEvent;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;

public class WorldEventCreationEvent extends WorldEventInstanceEvent implements ICancellableEvent {
    public WorldEventCreationEvent(WorldEventInstance worldEvent, Level level) {
        super(worldEvent, level);
    }
}
