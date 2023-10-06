package team.lodestar.lodestone.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;


@Mixin(Screen.class)
public class ScreenMixin {

    @Inject(at = @At("HEAD"), method = "renderBackground")
    private void lodestone$BeforeUiParticleMixin(GuiGraphics pGuiGraphics, CallbackInfo ci) {
        ScreenParticleHandler.renderEarliestParticles();
    }
}