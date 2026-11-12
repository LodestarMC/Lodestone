package team.lodestar.lodestone.handlers.item_events;

import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.function.BiConsumer;

public record EventResponderCollection(EventResponderSource source, Collection<Entry> items) {

    public void run(BiConsumer<IEventResponder, ItemStack> consumer) {
        run(IEventResponder.class, consumer);
    }

    public <T extends IEventResponder> void run(Class<T> type, BiConsumer<T, ItemStack> consumer) {
        for (Entry entry : items) {
            var item = entry.item();
            if (!type.isInstance(item)) {
                continue;
            }
            consumer.accept(type.cast(item), entry.stack());
        }
    }

    public record Entry(IEventResponder item, ItemStack stack) {

    }
}
