package team.lodestar.lodestone.events.types.worldevent;

import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;

public class WorldEventInstanceEvent extends Event {
    private WorldEventInstance worldEvent;
    private Level level;

    public WorldEventInstanceEvent(WorldEventInstance worldEvent, Level level) {
        this.worldEvent = worldEvent;
        this.level = level;
    }

    public WorldEventInstance getWorldEvent() {
        return worldEvent;
    }

    public Level getLevel() {
        return level;
    }

}
