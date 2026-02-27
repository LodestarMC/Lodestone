package team.lodestar.lodestone.core.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.lodestar.lodestone.core.sound.ExtendedSoundType;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Inject(method = "continueDestroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundManager;play(Lnet/minecraft/client/resources/sounds/SoundInstance;)V"))
    private void lodestone$CallExtendedStepSound(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir, @Local SoundType soundType) {
        if (soundType instanceof ExtendedSoundType extendedSoundType) {
            var minecraft = Minecraft.getInstance();
            extendedSoundType.onPlayHitSound(minecraft.level, minecraft.player, pos,
                    ((event, volume, pitch) -> minecraft.getSoundManager().play(
                            new SimpleSoundInstance(
                                    event,
                                    SoundSource.BLOCKS,
                                    (volume + 1.0F) / 8.0F,
                                    pitch * 0.5F,
                                    SoundInstance.createUnseededRandom(),
                                    pos
                            ))
                    ));
        }
    }
}
