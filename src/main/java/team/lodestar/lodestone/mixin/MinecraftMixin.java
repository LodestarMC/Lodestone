package team.lodestar.lodestone.mixin;

import io.github.fabricators_of_create.porting_lib.entity.events.PlayerInteractionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
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

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;render(FJZ)V"))
    private void onFrameStart(boolean tick, CallbackInfo ci) {
        ClientTickCounter.renderTick(isPaused() ? pausePartialTick : getFrameTime());
    }
}