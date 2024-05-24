package team.lodestar.lodestone.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;

public interface LodestoneInteractionEvent {
    Event<PlayerRightClickEmpty> RIGHT_CLICK_EMPTY = EventFactory.createArrayBacked(PlayerRightClickEmpty.class, callbacks -> (event -> {
        for(PlayerRightClickEmpty e : callbacks)
            e.onRightClickEmpty(event);
    }));

    Event<PlayerRightClickBlock> RIGHT_CLICK_BLOCK = EventFactory.createArrayBacked(PlayerRightClickBlock.class, callbacks -> ((event, hand, pos, hitResult) -> {
        for(PlayerRightClickBlock e : callbacks)
            return e.onRightClickBlock(event, hand, pos, hitResult);
        return false;
    }));

    Event<PlayerRightClickItem> RIGHT_CLICK_ITEM = EventFactory.createArrayBacked(PlayerRightClickItem.class, callbacks -> ((player, hand, itemStack) -> {
        for(PlayerRightClickItem e : callbacks)
            return e.onRightClickItem(player, hand, itemStack);
        return false;
    }));

    Event<OnItemUseStart> ON_ITEM_USE_START = EventFactory.createArrayBacked(OnItemUseStart.class, callbacks -> ((livingEntity, itemStack, duration) -> {
        for(OnItemUseStart e : callbacks)
            return e.onItemUseStart(livingEntity, itemStack, duration);
        return duration;
    }));


    @FunctionalInterface
    interface PlayerRightClickEmpty {
        void onRightClickEmpty(ServerPlayer player);
    }

    @FunctionalInterface
    interface PlayerRightClickBlock {
        boolean onRightClickBlock(Player player, InteractionHand hand, BlockPos blockPos, BlockHitResult hitResult);
    }

    @FunctionalInterface
    interface PlayerRightClickItem {
        boolean onRightClickItem(ServerPlayer player, InteractionHand hand, ItemStack itemStack);
    }

    @FunctionalInterface
    interface OnItemUseStart {
        int onItemUseStart(LivingEntity livingEntity, ItemStack itemStack, int duration);
    }
}
