package team.lodestar.lodestone.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import team.lodestar.lodestone.handlers.*;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;

import static team.lodestar.lodestone.LodestoneLib.RANDOM;

public class ClientRuntimeEvents {

    public static void clientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null) {
            if (minecraft.isPaused()) {
                return;
            }
            Camera camera = minecraft.gameRenderer.getMainCamera();
            WorldEventHandler.tick(minecraft.level);
            ScreenshakeHandler.clientTick(camera);
            ScreenParticleHandler.tickParticles();
        }
    }

    public static void cameraSetup(ViewportEvent.ComputeCameraAngles event) {
        ScreenshakeHandler.cameraSetup(event.getCamera());

    }

    public static void renderFog(ViewportEvent.RenderFog event) {
        RenderHandler.cacheFogData(event);
    }

    public static void fogColors(ViewportEvent.ComputeFogColor event) {
        RenderHandler.cacheFogData(event);
    }

    /**
     * The main render loop of Lodestone. We end all of our batches here.
     */
    public static void renderStages(RenderLevelStageEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        Camera camera = minecraft.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();
        float partial = event.getPartialTick().getGameTimeDeltaPartialTick(false);
        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();
        poseStack.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());

        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_SKY)) {
            WorldEventHandler.ClientOnly.renderWorldEvents(poseStack, partial);
        }

        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_PARTICLES)) {
            RenderHandler.MATRIX4F = new Matrix4f(RenderSystem.getModelViewMatrix());
        }
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_WEATHER)) {
            RenderHandler.endBatches();
        }

        poseStack.popPose();
    }

    public static void renderFrameEvent(RenderFrameEvent.Pre event) {//TODO Pre or Post?
        if (event != null) {
            ScreenParticleHandler.renderTick(event);
        }
    }
}