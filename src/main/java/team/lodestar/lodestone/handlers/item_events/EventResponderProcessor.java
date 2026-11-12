package team.lodestar.lodestone.handlers.item_events;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

public class EventResponderProcessor {

    private final List<EventResponderCollection> result;

    public EventResponderProcessor(List<EventResponderCollection> result) {
        this.result = result;
    }

    public void run(BiConsumer<IEventResponder, ItemStack> consumer) {
        run(IEventResponder.class, consumer);
    }

    public <T extends IEventResponder> void run(Class<T> type, BiConsumer<T, ItemStack> consumer) {
        for (EventResponderCollection collection : result) {
            collection.run(type, consumer);
        }
    }
}
