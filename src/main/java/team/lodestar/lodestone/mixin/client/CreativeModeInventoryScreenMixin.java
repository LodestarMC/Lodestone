package team.lodestar.lodestone.mixin.client;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {

//    @Inject(method = "renderTabButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;II)V"), locals = LocalCapture.CAPTURE_FAILHARD)
//    private void lodestone$renderScreenParticleAtTabIcon(GuiGraphics guiGraphics, CreativeModeTab tab, CallbackInfo ci, boolean bl, boolean bl2, int a, int b, int c, int d, int e, int f, ItemStack itemStack) {
//        ScreenParticleHandler.renderItemStackEarly(guiGraphics.pose(), tab.getIconItem(), d, e, false);
//        ((TheWorstInterface) guiGraphics).lodestone$setB(true);
//    }
}
