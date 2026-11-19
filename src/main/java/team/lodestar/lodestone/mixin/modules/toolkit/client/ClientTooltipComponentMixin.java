package team.lodestar.lodestone.mixin.modules.toolkit.client;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.lodestar.lodestone.modules.toolkit.tooltip.ClientTooltipComponentWrapper;

@Mixin(ClientTooltipComponent.class)
public class ClientTooltipComponentMixin {

    @Inject(method = "create(Lnet/minecraft/world/inventory/tooltip/TooltipComponent;)Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipComponent;", at = @At(value = "HEAD"), cancellable = true)
    private static void lodestone$unwrapClientComponent(TooltipComponent visualTooltipComponent, CallbackInfoReturnable<ClientTooltipComponent> cir) {
        var optional = ClientTooltipComponentWrapper.unwrap(visualTooltipComponent);
        optional.ifPresent(cir::setReturnValue);
    }
}
