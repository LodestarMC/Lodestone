package team.lodestar.lodestone.handlers.item_events;

import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;

public class EventResponderProcessor {

    private final List<EventResponderCollection> result;

    public EventResponderProcessor(List<EventResponderCollection> result) {
        this.result = result;
    }

    public void run(BiConsumer<IEventResponderBody, ItemStack> consumer) {
        run(IEventResponderBody.class, consumer);
    }

    public <T extends IEventResponderBody> void run(Class<T> type, BiConsumer<T, ItemStack> consumer) {
        for (EventResponderCollection collection : result) {
            collection.run(type, consumer);
        }
    }
}
