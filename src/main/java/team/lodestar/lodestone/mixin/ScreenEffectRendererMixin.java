package team.lodestar.lodestone.mixin;

import team.lodestar.lodestone.config.ClientConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import team.lodestar.lodestone.handlers.FireEffectHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {
    @Inject(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isSpectator()Z"))
    private static void lodestoneFireEffectRendering(Minecraft overlay, PoseStack pMinecraft, CallbackInfo ci) {
        FireEffectHandler.ClientOnly.renderUIMeteorFire(overlay, pMinecraft);
    }

    @Inject(method = "renderFire", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V"))
    private static void lodestoneFireEffectOffset(Minecraft minecraft, PoseStack poseStack, CallbackInfo ci) {
        poseStack.translate(0, -(ClientConfig.FIRE_OVERLAY_OFFSET.getConfigValue()) * 0.3f, 0);
    }
}