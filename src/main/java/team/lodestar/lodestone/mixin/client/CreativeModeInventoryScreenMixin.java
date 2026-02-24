package team.lodestar.lodestone.mixin.client;

import net.minecraft.client.gui.screens.inventory.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import team.lodestar.lodestone.systems.creative_tab.*;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {

    @Inject(method = "selectTab", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;addAll(Ljava/util/Collection;)Z", ordinal = 1, shift = At.Shift.AFTER))
    private void lodestone$selectTab(CallbackInfo ci) {
        var screen = ((CreativeModeInventoryScreen) (Object) this);
        var menu = screen.getMenu();
        CategorizedCreativeTabHandler.modifyTab(menu);
    }
}
