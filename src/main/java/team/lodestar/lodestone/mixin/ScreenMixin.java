package team.lodestar.lodestone.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Screen.class)
public class ScreenMixin {

    @Inject(at = @At("HEAD"), method = "renderBackground")
    private void lodestoneBeforeUiParticleMixin(GuiGraphics pGuiGraphics, CallbackInfo ci) {
        ScreenParticleHandler.renderEarliestParticles();
    }
}