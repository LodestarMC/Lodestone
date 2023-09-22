package team.lodestar.lodestone.mixin;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
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
}
