package team.lodestar.lodestone.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.irisshaders.iris.Iris;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.fml.ModList;
import team.lodestar.lodestone.handlers.RenderHandler;

public class ShadersHelper {
    public static boolean LOADED;

    public static class LoadedOnly {
        public static boolean isShadersEnabled() {
            return Iris.getIrisConfig().areShadersEnabled();
        }
    }

    public static void init() {
        LOADED = ModList.get().isLoaded("oculus");
    }

    public static boolean isLoaded() {
        return LOADED;
    }

    public static boolean isShadersEnabled() {
        if (isLoaded()) {
            return LoadedOnly.isShadersEnabled();
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderStages(RenderLevelStageEvent event) {
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_LEVEL)) {
            RenderSystem.getModelViewStack().pushPose();
            RenderSystem.getModelViewStack().setIdentity();
            if (RenderHandler.MATRIX4F != null) RenderSystem.getModelViewMatrix().set(RenderHandler.MATRIX4F);
            endBatchesParticles();
            RenderSystem.getModelViewStack().popPose();
            RenderSystem.applyModelViewMatrix();
            endBatches();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void endBatchesParticles() {
        RenderHandler.copyDepthBuffer(RenderHandler.LODESTONE_DEPTH_CACHE);
        endBatchesParticles(RenderHandler.DELAYED_RENDER);
        endBatchesParticles(RenderHandler.LATE_DELAYED_RENDER);
    }

    @OnlyIn(Dist.CLIENT)
    public static void endBatches() {
        RenderHandler.copyDepthBuffer(RenderHandler.LODESTONE_DEPTH_CACHE);
        endBatches(RenderHandler.DELAYED_RENDER);
        endBatches(RenderHandler.LATE_DELAYED_RENDER);
    }

    @OnlyIn(Dist.CLIENT)
    public static void endBatchesParticles(RenderHandler.LodestoneRenderLayer renderLayer) {
        RenderHandler.beginBufferedRendering();
        RenderHandler.renderBufferedParticles(renderLayer, true);
        RenderHandler.renderBufferedParticles(renderLayer, false);
        RenderHandler.endBufferedRendering();
    }

    @OnlyIn(Dist.CLIENT)
    public static void endBatches(RenderHandler.LodestoneRenderLayer renderLayer) {
        RenderHandler.beginBufferedRendering();
        RenderHandler.renderBufferedBatches(renderLayer, true);
        RenderHandler.renderBufferedBatches(renderLayer, false);
        RenderHandler.endBufferedRendering();
    }
}
