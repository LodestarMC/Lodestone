package team.lodestar.lodestone.mixin;

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
    private SoundType type;

    @ModifyVariable(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BlockItem;getPlaceSound(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/sounds/SoundEvent;"))
    private SoundType lodestoneGetStepSound(SoundType type) {
        return this.type = type;
    }

    @Inject(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    private void lodestoneCallExtendedStepSound(BlockPlaceContext pContext, CallbackInfoReturnable<InteractionResult> cir) {
        if (type instanceof ExtendedSoundType extendedSoundType) {
            extendedSoundType.onPlayPlaceSound(pContext.getLevel(), pContext.getClickedPos(), pContext.getPlayer());
        }
    }
}