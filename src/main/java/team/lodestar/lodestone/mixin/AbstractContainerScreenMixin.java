package team.lodestar.lodestone.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.TheWorstInterface;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;


@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

    @Shadow public int leftPos;

    @Shadow public int topPos;

    @Inject(at = @At("RETURN"), method = "render")
    private void lodestoneBeforeTooltipParticleMixin(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick, CallbackInfo ci) {
        ScreenParticleHandler.renderEarlyParticles();
    }

    @Inject(method = "renderSlot", at = @At(value = "INVOKE",shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;III)V"))
    private void lodestone$d(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        ScreenParticleHandler.renderItemStackEarly(slot.getItem(), slot.x, slot.y, true);
        ((TheWorstInterface)guiGraphics).setB(true);
    }

    @Inject(method = "renderFloatingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;II)V"))
    private void lodestone$f(GuiGraphics guiGraphics, ItemStack stack, int x, int y, String p_282568_, CallbackInfo ci){
        ScreenParticleHandler.renderItemStackEarly(stack, x, y, true);
        ((TheWorstInterface)guiGraphics).setB(true);
    }

}