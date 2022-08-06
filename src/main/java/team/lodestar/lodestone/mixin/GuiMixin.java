package team.lodestar.lodestone.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import team.lodestar.lodestone.handlers.ScreenParticleHandler;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(at = @At("HEAD"), method = "renderHotbar")
    private void lodestoneRenderHotbarStart(float l1, PoseStack j1, CallbackInfo ci) {
        ScreenParticleHandler.renderingHotbar = true;
    }
    @Inject(at = @At("RETURN"), method = "renderHotbar")
    private void lodestoneRenderHotbarEnd(float l1, PoseStack j1, CallbackInfo ci) {
        ScreenParticleHandler.renderingHotbar = false;
    }
}
