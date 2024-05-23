package team.lodestar.lodestone.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public interface EntityAttributeModificationEvent {

    Event<Add> ADD = EventFactory.createArrayBacked(Add.class, callbacks -> (builder)  -> {
        for (final Add callback : callbacks) {
            return callback.add(builder);
        }
        return builder;
    });

    @FunctionalInterface
    interface Add {
        AttributeSupplier.Builder add(AttributeSupplier.Builder builder);
    }
}
