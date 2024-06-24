package team.lodestar.lodestone.mixin.client.integration.sodium510;


import com.mojang.blaze3d.vertex.PoseStack;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.events.LodestoneRenderEvents;
import team.lodestar.lodestone.events.Stage;

@Pseudo
@Mixin(targets = "me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer")
public class SodiumWorldRendererMixin {
    @Inject(method = "drawChunkLayer", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/render/chunk/RenderSectionManager;renderLayer(Lme/jellysquid/mods/sodium/client/render/chunk/ChunkRenderMatrices;Lme/jellysquid/mods/sodium/client/render/chunk/terrain/TerrainRenderPass;DDD)V"))
    private void lodestone$injectEvent6(RenderType renderLayer, ChunkRenderMatrices matrices, double x, double y, double z, CallbackInfo ci){
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
