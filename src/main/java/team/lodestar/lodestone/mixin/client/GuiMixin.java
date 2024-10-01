package team.lodestar.lodestone.mixin.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(at = @At("HEAD"), method = "renderHotbarAndDecorations")
    private void lodestone$RenderHotbarStart(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        ScreenParticleHandler.renderingHotbar = true;
    }

    @Inject(at = @At("RETURN"), method = "renderHotbarAndDecorations")
    private void lodestone$RenderHotbarEnd(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        ScreenParticleHandler.renderingHotbar = false;
    }
}
