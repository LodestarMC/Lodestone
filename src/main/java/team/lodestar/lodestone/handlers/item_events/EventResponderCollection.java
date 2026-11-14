package team.lodestar.lodestone.handlers.item_events;

import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.function.BiConsumer;

public record EventResponderCollection(EventResponderSource source, Collection<Entry> items) {

    public void run(BiConsumer<IEventResponderBody, ItemStack> consumer) {
        run(IEventResponderBody.class, consumer);
    }

    public <T extends IEventResponderBody> void run(Class<T> type, BiConsumer<T, ItemStack> consumer) {
        for (Entry entry : items) {
            var body = entry.body();
            if (!type.isInstance(body)) {
                continue;
            }
            consumer.accept(type.cast(body), entry.stack());
        }
    }

    public record Entry(IEventResponderBody body, ItemStack stack) {

        public Entry(IEventResponderBody body) {
            this(body, ItemStack.EMPTY);
        }

    }
}
