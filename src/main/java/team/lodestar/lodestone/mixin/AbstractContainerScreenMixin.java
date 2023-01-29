package team.lodestar.lodestone.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {
    @Inject(at = @At("RETURN"), method = "render")
    private void lodestoneBeforeTooltipParticleMixin(PoseStack i1, int slot, int k, float l1, CallbackInfo ci) {
        ScreenParticleHandler.renderParticles();
    }
}