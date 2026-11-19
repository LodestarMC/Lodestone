package team.lodestar.lodestone.mixin.modules.toolkit.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.stream.Stream;

@Mixin(ClientHooks.class)
public class ClientHooksMixin {

    @WrapOperation(method = "gatherTooltipComponentsFromElements", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;toList()Ljava/util/List;"))
    private static List<ClientTooltipComponent> enchmod$addEnchantmentTooltip(Stream<?> instance, Operation<List<ClientTooltipComponent>> original,
                                                                              @Local(argsOnly = true) ItemStack stack,
                                                                              @Local(argsOnly = true) List<Either<FormattedText, TooltipComponent>> elements,
                                                                              @Local(name = "mouseX") int mouseX,
                                                                              @Local(name = "screenWidth") int screenWidth,
                                                                              @Local(name = "font") Font font,
                                                                              @Local RenderTooltipEvent.GatherComponents event) {
        var result = original.call(instance);
        return LodestoneTooltipInterfaceHandler.processInterfaces(stack, elements, result, mouseX, screenWidth, font, event);
    }
}