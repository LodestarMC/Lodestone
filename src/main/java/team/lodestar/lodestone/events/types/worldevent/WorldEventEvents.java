package team.lodestar.lodestone.events.types.worldevent;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;

public class WorldEventEvents {

    public static final Event<Creation> CREATION = EventFactory.createArrayBacked(Creation.class, callbacks -> (worldEvent, level) -> {
        for (Creation e : callbacks)
            e.onCreation(worldEvent, level);
    });

    public static final Event<Discard> DISCARD = EventFactory.createArrayBacked(Discard.class, callbacks -> (worldEvent, level) -> {
        for (Discard e : callbacks)
            e.onDiscard(worldEvent, level);
    });

    public static final Event<Tick> TICK = EventFactory.createArrayBacked(Tick.class, callbacks -> (worldEvent, level) -> {
        for (Tick e : callbacks)
            e.onTick(worldEvent, level);
    });

    @FunctionalInterface
    public interface Creation {
        void onCreation(WorldEventInstance worldEvent, Level level);
    }

    @FunctionalInterface
    public interface Discard {
        void onDiscard(WorldEventInstance worldEvent, Level level);
    }

    @FunctionalInterface
    public interface Tick {
        void onTick(WorldEventInstance worldEvent, Level level);
    }

}
