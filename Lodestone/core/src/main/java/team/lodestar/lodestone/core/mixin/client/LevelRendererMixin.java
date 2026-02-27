package team.lodestar.lodestone.core.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.core.sound.ExtendedSoundType;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow
    @Nullable
    private ClientLevel level;

    @Shadow
    @Final
    private Minecraft minecraft;

    @SuppressWarnings("DataFlowIssue")
    @Inject(method = "levelEvent", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;stateById(I)Lnet/minecraft/world/level/block/state/BlockState;")), at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V", ordinal = 0))
    private void lodestone$CallExtendedBreakSound(int pType, BlockPos pPos, int pData, CallbackInfo ci, @Local(name = "blockstate1") BlockState blockState, @Local SoundType soundType) {
        if (soundType instanceof ExtendedSoundType extendedSoundType) {
            extendedSoundType.onPlayBreakSound(level, minecraft.player, pPos, blockState,
                    ((event, volume, pitch) -> level.playLocalSound(
                            pPos,
                            event,
                            SoundSource.BLOCKS,
                            (volume + 1.0F) / 2.0F,
                            pitch * 0.8F,
                            false
                    )));
        }
    }
}