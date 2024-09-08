package team.lodestar.lodestone.mixin.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.events.LodestoneRenderEvents;
import team.lodestar.lodestone.events.Stage;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;
import team.lodestar.lodestone.systems.sound.ExtendedSoundType;


@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow
    @Nullable
    private ClientLevel level;
    @Shadow
    private @Nullable RenderTarget particlesTarget;
    @Unique
    private SoundType lodestone$blockSoundType;


    @ModifyVariable(method = "levelEvent",
            slice = @Slice(from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;stateById(I)Lnet/minecraft/world/level/block/state/BlockState;")),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V",
                    ordinal = 0))
    private SoundType lodestone$GetBreakSound(SoundType type) {
        return this.lodestone$blockSoundType = type;
    }

    @Inject(method = "levelEvent", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;stateById(I)Lnet/minecraft/world/level/block/state/BlockState;")), at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V", ordinal = 0))
    private void lodestone$CallExtendedBreakSound(int pType, BlockPos pPos, int pData, CallbackInfo ci) {
        if (lodestone$blockSoundType instanceof ExtendedSoundType extendedSoundType) {
            extendedSoundType.onPlayBreakSound(this.level, pPos);
        }
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "net.minecraft.client.renderer.PostChain.process(F)V", ordinal = 1))
    public void lodestone$injectionBeforeTransparencyChainProcess(CallbackInfo ci) {
        LodestoneLib.debug("lodestone$injectionBeforeTransparencyChainProcess");
        PostProcessHandler.copyDepthBuffer();
    }

    @Inject(method = "renderLevel", at = @At(value = "HEAD"))
    private void lodestoneLevelRendererPoseStackGrabber(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo ci) {
        RenderHandler.MAIN_POSE_STACK = pPoseStack;
    }

    @ModifyVariable(method = "initTransparency", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PostChain;getTempTarget(Ljava/lang/String;)Lcom/mojang/blaze3d/pipeline/RenderTarget;", ordinal = 4), index = 2)
    private PostChain lodestone$initTransparency(PostChain value) {
        //RenderHandler.setupLodestoneRenderTargets();
        return value;
    }

    @Inject(method = "deinitTransparency", at = @At(value = "HEAD"))
    private void lodestone$deinitTransparency(CallbackInfo ci) {
        //RenderHandler.closeLodestoneRenderTargets();
    }

    //EVENTS 2456 / 2456

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 16))
    private void lodestone$injectEvent(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
        LodestoneRenderEvents.AFTER_PARTICLES.invoker().render(poseStack, partialTick, Stage.AFTER_PARTICLES);
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 19))
    private void lodestone$injectEvent2(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
        LodestoneRenderEvents.AFTER_PARTICLES.invoker().render(poseStack, partialTick, Stage.AFTER_PARTICLES);
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderSnowAndRain(Lnet/minecraft/client/renderer/LightTexture;FDDD)V", ordinal = 0))
    private void lodestone$injectEvent3(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
        LodestoneRenderEvents.AFTER_WEATHER.invoker().render(poseStack, partialTick, Stage.AFTER_WEATHER);
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderSnowAndRain(Lnet/minecraft/client/renderer/LightTexture;FDDD)V", ordinal = 1))
    private void lodestone$injectEvent4(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
        LodestoneRenderEvents.AFTER_WEATHER.invoker().render(poseStack, partialTick, Stage.AFTER_WEATHER);
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderSky(Lcom/mojang/blaze3d/vertex/PoseStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/Camera;ZLjava/lang/Runnable;)V"))
    private void lodestone$injectEvent5(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
        LodestoneRenderEvents.AFTER_SKY.invoker().render(poseStack, partialTick, Stage.AFTER_SKY);
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 12))
    private void lodestone$injectEvent6(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
        LodestoneRenderEvents.AFTER_BLOCK_ENTITIES.invoker().render(poseStack, partialTick, Stage.AFTER_BLOCK_ENTITIES);
    }
}