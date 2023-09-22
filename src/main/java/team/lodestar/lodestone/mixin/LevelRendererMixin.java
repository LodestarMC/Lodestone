package team.lodestar.lodestone.mixin;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import org.spongepowered.asm.mixin.injection.*;
import team.lodestar.lodestone.handlers.*;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;
import team.lodestar.lodestone.systems.sound.ExtendedSoundType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Unique
    private SoundType lodestone$blockSoundType;

    @ModifyVariable(method = "levelEvent", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;stateById(I)Lnet/minecraft/world/level/block/state/BlockState;")), at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V", ordinal = 0), index = 17)
    private SoundType lodestoneGetBreakSound(SoundType type) {
        return this.lodestone$blockSoundType = type;
    }

    @Inject(method = "levelEvent", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;stateById(I)Lnet/minecraft/world/level/block/state/BlockState;")), at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V", ordinal = 0))
    private void lodestoneCallExtendedBreakSound(Player pPlayer, int pType, BlockPos pPos, int pData, CallbackInfo ci) {
        if (pPlayer != null) {
            if (lodestone$blockSoundType instanceof ExtendedSoundType extendedSoundType) {
                extendedSoundType.onPlayBreakSound(pPlayer.level, pPos);
            }
        }
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "net.minecraft.client.renderer.PostChain.process(F)V", ordinal = 1))
    public void injectionBeforeTransparencyChainProcess(CallbackInfo ci) {
        PostProcessHandler.copyDepthBuffer();
    }

    @Inject(method = "renderLevel", at = @At(value = "HEAD"))
    private void lodestoneLevelRendererPoseStackGrabber(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo ci) {
        RenderHandler.MAIN_POSE_STACK = pPoseStack;
    }
}