package team.lodestar.lodestone.handlers.item_events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class EventResponderSource {

    public final ResourceLocation id;
    public final ItemStackPool stackFunction;

    public EventResponderSource(ResourceLocation id, ItemStackPool stackPool) {
        this.id = id;
        this.stackFunction = stackPool;
    }

    public EventResponderCollection getEventResponders(LivingEntity entity) {
        var stacks = stackFunction.gatherItems(entity);
        var result = new ArrayList<EventResponderCollection.Entry>();
        for (ItemStack stack : stacks) {
            var effect = mapEffectBody(stack);
            if (effect.isEmpty()) {
                continue;
            }
            result.add(new EventResponderCollection.Entry(effect.get(), stack));
        }
        return new EventResponderCollection(this, result);
    }

    public Optional<IEventResponderItem> mapEffectBody(ItemStack stack) {
        if (stack.getItem() instanceof IEventResponderItem responder) {
            return Optional.of(responder);
        }
        return Optional.empty();
    }

    public interface ItemStackPool {
        Collection<ItemStack> gatherItems(LivingEntity entity);
    }
}
