package com.sammy.ortus.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sammy.ortus.handlers.ScreenParticleHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.sammy.ortus.systems.rendering.particle.screen.base.ScreenParticle.RenderOrder.BEFORE_UI;

@Mixin(Screen.class)
public class ScreenMixin {

    @Inject(at = @At("HEAD"), method = "renderBackground(Lcom/mojang/blaze3d/vertex/PoseStack;I)V")
    private void ortusBeforeUiParticleMixin(PoseStack pPoseStack, int pVOffset, CallbackInfo ci) {
        ScreenParticleHandler.renderParticles(BEFORE_UI);
    }
}