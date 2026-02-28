package team.lodestar.lodestone.toolkit.mixin.client;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import team.lodestar.lodestone.toolkit.creative_tab.CategorizedCreativeTabHandler;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

    @Inject(method = "renderSlot", at = @At("HEAD"), cancellable = true)
    private void lodestone$modifySlotRendering(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        if (CategorizedCreativeTabHandler.renderSlot(guiGraphics, slot)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderSlotHighlight(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/inventory/Slot;IIF)V", at = @At("HEAD"), cancellable = true)
    private void lodestone$modifySlotHighlightRendering(GuiGraphics guiGraphics, Slot slot, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (CategorizedCreativeTabHandler.disableSlotHighlight(slot)) {
            ci.cancel();
        }
    }

}