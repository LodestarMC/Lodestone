package team.lodestar.lodestone.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.config.ClientConfig;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {
    @Inject(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isSpectator()Z"))
    private static void lodestone$FireEffectRendering(Minecraft overlay, PoseStack pMinecraft, CallbackInfo ci) {
        FireEffectHandler.ClientOnly.renderUIFireEffect(overlay, pMinecraft);
    }

    @Inject(method = "renderFire", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    private static void lodestone$FireEffectOffset(Minecraft minecraft, PoseStack poseStack, CallbackInfo ci) {
        poseStack.translate(0, -(ClientConfig.FIRE_OVERLAY_OFFSET.getConfigValue()) * 0.3f, 0);
    }
}