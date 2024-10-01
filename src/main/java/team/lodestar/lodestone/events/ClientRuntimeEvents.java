package team.lodestar.lodestone.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import team.lodestar.lodestone.handlers.*;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.helpers.ShadersHelper;

import static team.lodestar.lodestone.LodestoneLib.RANDOM;

public class ClientRuntimeEvents {

    public static void clientTick(Minecraft minecraft) {
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


    /**
     * The main render loop of Lodestone. We end all of our batches here.
     */
    public static void renderStages(PoseStack poseStack, float partial, Stage stage) {
        Minecraft minecraft = Minecraft.getInstance();
        Camera camera = minecraft.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();
        poseStack.pushPose();
        poseStack.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());

        if (stage == Stage.AFTER_SKY) {
            WorldEventHandler.ClientOnly.renderWorldEvents(poseStack, partial);
        }

        if (stage == Stage.AFTER_PARTICLES) {
            if (!ShadersHelper.isShadersEnabled()) {
                RenderHandler.MATRIX4F = new Matrix4f(RenderSystem.getModelViewMatrix());
            }
        }

        if (!ShadersHelper.isShadersEnabled()) {
            if (stage == Stage.AFTER_WEATHER) {
                RenderHandler.endBatches();
            }
        } else {
            ShadersHelper.renderStages(stage);
        }

        poseStack.popPose();
    }
}