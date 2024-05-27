package team.lodestar.lodestone.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.player.LocalPlayer;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.registry.common.particle.LodestoneScreenParticleRegistry;
import team.lodestar.lodestone.systems.client.ClientTickCounter;


@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    public abstract boolean isPaused();

    @Shadow
    private float pausePartialTick;

    @Shadow
    public abstract float getFrameTime();

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow public abstract RenderTarget getMainRenderTarget();

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;render(FJZ)V"))
    private void onFrameStart(boolean tick, CallbackInfo ci) {
        ClientTickCounter.renderTick(isPaused() ? pausePartialTick : getFrameTime());
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/ReloadableResourceManager;registerReloadListener(Lnet/minecraft/server/packs/resources/PreparableReloadListener;)V", ordinal = 17))
    private void lodestone$registerParticleFactories(GameConfig gameConfig, CallbackInfo ci) {
        LodestoneScreenParticleRegistry.registerParticleFactory();
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", ordinal = 4,  shift = At.Shift.AFTER))
    private void lodestone$renderTickThingamajig(boolean tick, CallbackInfo ci) {
        ScreenParticleHandler.renderTick();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void lodestone$onInit(GameConfig gameConfig, CallbackInfo ci) {
        RenderHandler.TEMP_RENDER_TARGET = new TextureTarget(getMainRenderTarget().width, getMainRenderTarget().height, true, Minecraft.ON_OSX);;

    }
}