package team.lodestar.lodestone.systems.postprocess;


import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.renderer.RenderType;
import team.lodestar.lodestone.events.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles world-space post-processing.
 * Based on vanilla {@link net.minecraft.client.renderer.PostChain} system, but allows the shader to access the world depth buffer.
 */

public class PostProcessHandler {
    private static final List<PostProcessor> instances = new ArrayList<>();

    private static boolean didCopyDepth = false;

    /**
     * Add an {@link PostProcessor} for it to be handled automatically.
     * IMPORTANT: processors has to be added in the right order!!!
     * There's no way of getting an instance, so you need to keep the instance yourself.
     */
    public static void addInstance(PostProcessor instance) {
        instances.add(instance);
    }

    public static void copyDepthBuffer() {
        if (didCopyDepth) return;
        instances.forEach(PostProcessor::copyDepthBuffer);
        didCopyDepth = true;
    }

    public static void resize(int width, int height) {
        instances.forEach(i -> i.resize(width, height));
    }


    public static void onAfterSolidBlocks(WorldRenderContext ctx) {
        PostProcessor.viewModelStack = ctx.matrixStack(); // Copy PoseStack from LevelRenderer#renderLevel
    }

    public static void onWorldRenderLast(WorldRenderContext ctx) {
        copyDepthBuffer(); // copy the depth buffer if the mixin didn't trigger

        instances.forEach(PostProcessor::applyPostProcess);

        didCopyDepth = false; // reset for next frame
        //TODO runs
    }

    public static void onAfterSolidBlocks(RenderType renderType, PoseStack poseStack, Stage stage) {
        PostProcessor.viewModelStack = poseStack;
        //TODO runs
    }
}
