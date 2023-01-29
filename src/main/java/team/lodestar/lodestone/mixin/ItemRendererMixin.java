package team.lodestar.lodestone.mixin;

import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V", ordinal = 0), method = "renderGuiItem(Lnet/minecraft/world/item/ItemStack;IILnet/minecraft/client/resources/model/BakedModel;)V")
    private void lodestone$renderGuiItem(ItemStack pStack, int pX, int pY, BakedModel pBakedmodel, CallbackInfo ci) {
        ScreenParticleHandler.renderItemStack(pStack, pX, pY);
    }
    @Inject(at = @At(value = "TAIL"), method = "renderGuiItem(Lnet/minecraft/world/item/ItemStack;IILnet/minecraft/client/resources/model/BakedModel;)V")
    private void lodestone$renderGuiItemLate(ItemStack pStack, int pX, int pY, BakedModel pBakedmodel, CallbackInfo ci) {
        ScreenParticleHandler.renderItemStackLate();
    }
}