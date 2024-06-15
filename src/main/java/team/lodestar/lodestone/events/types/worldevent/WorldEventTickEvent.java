package team.lodestar.lodestone.events.types.worldevent;

import net.minecraft.world.level.Level;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;

public class WorldEventTickEvent extends WorldEventInstanceEvent {
    public WorldEventTickEvent(WorldEventInstance worldEvent, Level level) {
        super(worldEvent, level);
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
