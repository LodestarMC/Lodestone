package team.lodestar.lodestone.handlers.item_events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public abstract class EventResponderSource {

    public final ResourceLocation id;

    protected EventResponderSource(ResourceLocation id) {
        this.id = id;
    }

    public abstract EventResponderCollection getEventResponders(LivingEntity entity);
}
