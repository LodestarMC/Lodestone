package team.lodestar.lodestone.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

//    @Inject(method = "renderSlot", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;III)V"))
//    private void lodestone$renderScreenParticleAtSlot(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
//        ScreenParticleHandler.renderItemStackEarly(guiGraphics.pose(), slot.getItem(), slot.x, slot.y, true);
//        ((TheWorstInterface) guiGraphics).lodestone$setB(true);
//    }
//
//    @Inject(method = "renderFloatingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;II)V"))
//    private void lodestone$renderScreenParticleAtMouse(GuiGraphics guiGraphics, ItemStack stack, int x, int y, String p_282568_, CallbackInfo ci) {
//        ScreenParticleHandler.renderItemStackEarly(guiGraphics.pose(), stack, x, y, true);
//        ((TheWorstInterface) guiGraphics).lodestone$setB(true);
//    }

}