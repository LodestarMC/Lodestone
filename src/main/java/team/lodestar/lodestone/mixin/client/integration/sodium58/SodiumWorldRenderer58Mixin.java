package team.lodestar.lodestone.mixin.client.integration.sodium58;


import com.mojang.blaze3d.vertex.PoseStack;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.events.LodestoneRenderEvents;
import team.lodestar.lodestone.events.Stage;

@Pseudo
@Mixin(SodiumWorldRenderer.class)
public class SodiumWorldRenderer58Mixin {

    @Inject(method = "drawChunkLayer", at = @At(value = "TAIL"), remap = false)
    private void lodestone$injectEvent58(RenderType renderLayer, PoseStack matrixStack, double x, double y, double z, CallbackInfo ci){
        LodestoneRenderEvents.BEFORE_CLEAR.invoker().render(renderLayer, matrixStack, Stage.AFTER_SOLID_BLOCKS);
    }
}
