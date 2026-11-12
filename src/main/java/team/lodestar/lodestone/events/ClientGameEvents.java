package team.lodestar.lodestone.events;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.GameShuttingDownEvent;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.handlers.*;
import team.lodestar.lodestone.handlers.item_events.ItemEventHandler;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.modules.rendering.*;
import team.lodestar.lodestone.modules.toolkit.worldevent.*;
import team.lodestar.lodestone.registry.client.LodestoneModels;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderSystem;
import team.lodestar.lodestone.systems.rendering.renderpass.RenderPassHandler;


@EventBusSubscriber(value = Dist.CLIENT)
public class ClientGameEvents {

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
        LodestoneRenderingSystem.cacheFogData(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void fogColors(ViewportEvent.ComputeFogColor event) {
        LodestoneRenderingSystem.cacheFogColors(event);
    }

    /**
     * The main render loop of Lodestone. We end all of our batches here.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void renderStages(RenderLevelStageEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        Camera camera = minecraft.gameRenderer.getMainCamera();
        float partial = event.getPartialTick().getGameTimeDeltaPartialTick(false);
        PoseStack poseStack = event.getPoseStack();
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_SKY)) {
            WorldEventRenderHandler.renderWorldEvents(minecraft.level, poseStack, camera, partial);
        }

        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_WEATHER)) {
            LodestoneRenderingSystem.render();
        }
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
            LodestoneModels.cleanup();
            LodestoneRenderSystem.destroyBufferObjects();
            RenderPassHandler.close();
            LodestoneLib.LOGGER.info("Shutting down Lodestone");
        });
    }
}