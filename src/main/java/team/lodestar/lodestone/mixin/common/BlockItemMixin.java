package team.lodestar.lodestone.mixin.common;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.lodestar.lodestone.systems.sound.ExtendedSoundType;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Unique
    private SoundType lodestone$type;

    @ModifyVariable(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BlockItem;getPlaceSound(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/sounds/SoundEvent;"))
    private SoundType lodestone$GetStepSound(SoundType type) {
        return this.lodestone$type = type;
    }

    @Inject(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    private void lodestone$CallExtendedStepSound(BlockPlaceContext pContext, CallbackInfoReturnable<InteractionResult> cir) {
        if (lodestone$type instanceof ExtendedSoundType extendedSoundType) {
            extendedSoundType.onPlayPlaceSound(pContext.getLevel(), pContext.getClickedPos(), pContext.getPlayer());
        }
    }
}