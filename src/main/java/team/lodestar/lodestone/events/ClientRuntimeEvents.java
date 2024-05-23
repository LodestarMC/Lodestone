package team.lodestar.lodestone.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import team.lodestar.lodestone.capability.LodestonePlayerDataCapability;
import team.lodestar.lodestone.handlers.*;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.systems.client.ClientTickCounter;

import static team.lodestar.lodestone.LodestoneLib.RANDOM;

public class ClientRuntimeEvents {

    public static void clientTick(Minecraft minecraft) {
        if (minecraft.level != null) {
            ClientTickCounter.clientTick();
            if (minecraft.isPaused()) {
                return;
            }
            Camera camera = minecraft.gameRenderer.getMainCamera();
            GhostBlockHandler.tickGhosts();
            WorldEventHandler.tick(minecraft.level);
            PlacementAssistantHandler.tick(minecraft.player, minecraft.hitResult);
            ScreenshakeHandler.clientTick(camera, RANDOM);
            LodestonePlayerDataCapability.ClientOnly.clientTick();
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
            GhostBlockHandler.renderGhosts(poseStack);
            WorldEventHandler.ClientOnly.renderWorldEvents(poseStack, partial);
        }

        if (stage == Stage.AFTER_PARTICLES) {
            RenderHandler.MATRIX4F = new Matrix4f(RenderSystem.getModelViewMatrix());
        }
        if (stage == Stage.AFTER_WEATHER) {
            RenderHandler.endBatchesEarly();
        }

        poseStack.popPose();
    }
}