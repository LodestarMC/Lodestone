package team.lodestar.lodestone.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.lodestar.lodestone.events.LodestoneInteractionEvent;
import team.lodestar.lodestone.systems.sound.ExtendedSoundType;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Unique
    private SoundType lodestone$type;

    @ModifyVariable(method = "continueDestroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundManager;play(Lnet/minecraft/client/resources/sounds/SoundInstance;)V"))
    private SoundType lodestone$GetStepSound(SoundType type) {
        return this.lodestone$type = type;
    }

    @Inject(method = "continueDestroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundManager;play(Lnet/minecraft/client/resources/sounds/SoundInstance;)V"))
    private void lodestone$CallExtendedStepSound(BlockPos pPosBlock, Direction pDirectionFacing, CallbackInfoReturnable<Boolean> cir) {
        if (lodestone$type instanceof ExtendedSoundType extendedSoundType) {
            extendedSoundType.onPlayHitSound(pPosBlock);
        }
    }

    @Inject(method = "performUseItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"), cancellable = true)
    private void lodestone$injectEvent(LocalPlayer player, InteractionHand hand, BlockHitResult result, CallbackInfoReturnable<InteractionResult> cir, @Local BlockPos blockPos){
        boolean shouldCancel = LodestoneInteractionEvent.RIGHT_CLICK_BLOCK.invoker().onRightClickBlock(player, hand, blockPos, result);
        if (shouldCancel) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }
}
