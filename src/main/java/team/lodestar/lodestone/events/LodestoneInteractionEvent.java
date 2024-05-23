package team.lodestar.lodestone.events;

import io.github.fabricators_of_create.porting_lib.entity.events.PlayerInteractionEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface LodestoneInteractionEvent {
    Event<PlayerRightClickEmpty> RIGHT_CLICK_EMPTY = EventFactory.createArrayBacked(PlayerRightClickEmpty.class, callbacks -> (event -> {
        for(PlayerRightClickEmpty e : callbacks)
            e.onRightClickEmpty(event);
    }));

    @FunctionalInterface
    interface PlayerRightClickEmpty {
        void onRightClickEmpty(PlayerInteractionEvents.LeftClickEmpty event);
    }
}
