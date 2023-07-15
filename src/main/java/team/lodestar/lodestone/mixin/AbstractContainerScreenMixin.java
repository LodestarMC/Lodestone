package team.lodestar.lodestone.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;


@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {
    @Inject(at = @At("RETURN"), method = "render")
    private void lodestoneBeforeTooltipParticleMixin(GuiGraphics p_283479_, int p_283661_, int p_281248_, float p_281886_, CallbackInfo ci) {
        ScreenParticleHandler.renderEarlyParticles();
    }
}