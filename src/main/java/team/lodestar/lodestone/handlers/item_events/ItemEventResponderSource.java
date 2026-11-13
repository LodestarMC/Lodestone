package team.lodestar.lodestone.handlers.item_events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class ItemEventResponderSource extends EventResponderSource {

    public final ItemStackPool stackPool;

    public ItemEventResponderSource(ResourceLocation id, ItemStackPool stackPool) {
        super(id);
        this.stackPool = stackPool;
    }

    @Override
    public EventResponderCollection getEventResponders(LivingEntity entity) {
        var stacks = stackPool.gatherItems(entity);
        var result = new ArrayList<EventResponderCollection.Entry>();
        for (ItemStack stack : stacks) {
            if (stack.getItem() instanceof IEventResponderItem responder) {
                result.add(new EventResponderCollection.Entry(responder, stack));
            }
        }
        return new EventResponderCollection(this, result);
    }


    public interface ItemStackPool {
        Collection<ItemStack> gatherItems(LivingEntity entity);
    }
}
