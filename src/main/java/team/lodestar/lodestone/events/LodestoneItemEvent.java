package team.lodestar.lodestone.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public interface LodestoneItemEvent {
    Event<ItemExpireEvent> EXPIRE = EventFactory.createArrayBacked(ItemExpireEvent.class, callbacks -> (itemEntity, stack) -> {
        for (final ItemExpireEvent callback : callbacks) {
            return callback.expire(itemEntity, stack);
        }
        return -1;
    });

    Event<ToolTip> ON_ITEM_TOOLTIP = EventFactory.createArrayBacked(ToolTip.class, callbacks -> (stack, player, tooltip, flags) -> {
        for (final ToolTip callback : callbacks) {
            callback.on(stack, player, tooltip, flags);
        }
    });


    @FunctionalInterface
    interface ToolTip {
        void on(ItemStack stack, Player player, List<Component> tooltip, TooltipFlag flags);
    }

    @FunctionalInterface
    interface ItemExpireEvent {
        int expire(ItemEntity itemEntity, ItemStack stack);
    }
}