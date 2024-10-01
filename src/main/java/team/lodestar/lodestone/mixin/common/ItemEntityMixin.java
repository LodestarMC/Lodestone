package team.lodestar.lodestone.mixin.common;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import team.lodestar.lodestone.events.LodestoneItemEvent;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;discard()V", ordinal = 1))
    private boolean lodestone$onTick(ItemEntity instance) {
        return LodestoneItemEvent.EXPIRE.invoker().expire(instance, instance.getItem()) < 0;
    }
}
