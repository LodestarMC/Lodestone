package team.lodestar.lodestone.mixin.client.integration.sodium;


import com.mojang.blaze3d.vertex.PoseStack;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.events.LodestoneRenderEvents;
import team.lodestar.lodestone.events.Stage;


@Mixin(SodiumWorldRenderer.class)
public class SodiumWorldRendererMixin {

    @Inject(method = "drawChunkLayer", at = @At(value = "TAIL"), remap = false)
    private void lodestone$injectEvent6(RenderType renderLayer, ChunkRenderMatrices matrices, double x, double y, double z, CallbackInfo ci) {
        LodestoneRenderEvents.BEFORE_CLEAR.invoker().render(renderLayer, toPoseStack(matrices), Stage.AFTER_SOLID_BLOCKS);
    }

    @Unique
    private PoseStack toPoseStack(ChunkRenderMatrices chunkRenderMatrices) {
        var poseStack = new PoseStack();
        var poseMatrix = chunkRenderMatrices.modelView();
        // Assuming poseMatrix is of type Matrix4f
        poseStack.last().pose().set(poseMatrix);
        return poseStack;
    }
}
