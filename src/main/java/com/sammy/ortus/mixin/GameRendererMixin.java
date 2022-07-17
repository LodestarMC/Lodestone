package com.sammy.ortus.mixin;

import com.sammy.ortus.systems.postprocess.PostProcessHandler;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "resize", at = @At(value = "HEAD"))
    public void injectionResizeListener(int width, int height, CallbackInfo ci) {
        PostProcessHandler.resize(width, height);
    }
}
