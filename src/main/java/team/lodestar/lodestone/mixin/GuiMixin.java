package team.lodestar.lodestone.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(at = @At("HEAD"), method = "renderHotbar")
    private void lodestoneRenderHotbarStart(float p_283031_, GuiGraphics p_282108_, CallbackInfo ci) {
        ScreenParticleHandler.renderingHotbar = true;
    }
    @Inject(at = @At("RETURN"), method = "renderHotbar")
    private void lodestoneRenderHotbarEnd(float p_283031_, GuiGraphics p_282108_, CallbackInfo ci) {
        ScreenParticleHandler.renderingHotbar = false;
    }
}
