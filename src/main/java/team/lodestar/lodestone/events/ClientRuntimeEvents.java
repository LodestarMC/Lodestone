package team.lodestar.lodestone.events;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.*;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.AddAttributeTooltipsEvent;
import net.neoforged.neoforge.event.GameShuttingDownEvent;
import net.neoforged.neoforge.event.level.*;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.handlers.*;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.registry.client.LodestoneOBJModels;
import team.lodestar.lodestone.systems.easing.*;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderSystem;


@EventBusSubscriber(value = Dist.CLIENT)
public class ClientRuntimeEvents {

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        final ClientLevel level = minecraft.level;
        if (level != null) {
            if (minecraft.isPaused()) {
                return;
            }
            Camera camera = minecraft.gameRenderer.getMainCamera();
            WorldEventHandler.tick(level);
            ScreenshakeHandler.clientTick(level, camera);
            ScreenParticleHandler.tickParticles();
        }
    }

    @SubscribeEvent
    public static void cameraSetup(ViewportEvent.ComputeCameraAngles event) {
        ScreenshakeHandler.computeAngles(event);
    }

    @SubscribeEvent
    public static void addAttributeTooltips(AddAttributeTooltipsEvent event) {
        ItemEventHandler.addAttributeTooltips(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void renderFog(ViewportEvent.RenderFog event) {
        RenderHandler.cacheFogData(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void fogColors(ViewportEvent.ComputeFogColor event) {
        RenderHandler.cacheFogData(event);
    }

    /**
     * The main render loop of Lodestone. We end all of our batches here.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
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

        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_WEATHER)) {
            RenderHandler.endBatches();
        }


        poseStack.popPose();
    }

    @SubscribeEvent
    public static void renderFrameEvent(RenderFrameEvent.Post event) {
        if (event != null) {
            ScreenParticleHandler.renderTick(event);
        }
    }

    @SubscribeEvent
    public static void shutdownEvent(GameShuttingDownEvent event) {
        LodestoneRenderSystem.wrap(() -> {
            LodestoneOBJModels.cleanup();
            LodestoneRenderSystem.destroyBufferObjects();
            LodestoneLib.LOGGER.info("Shutting down Lodestone");
        });
    }
}