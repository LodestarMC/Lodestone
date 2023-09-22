package team.lodestar.lodestone.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.systems.particle.screen.TheWorstInterface;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {

    @Inject(method = "renderTabButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;II)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void lodestone$renderScreenParticleAtTabIcon(GuiGraphics guiGraphics, CreativeModeTab tab, CallbackInfo ci, boolean bl, boolean bl2, int a, int b, int c, int d, int e, int f, ItemStack itemStack) {
        ScreenParticleHandler.renderItemStackEarly(tab.getIconItem(), d, e, false);
        ((TheWorstInterface) guiGraphics).lodestone$setB(true);
    }
}
