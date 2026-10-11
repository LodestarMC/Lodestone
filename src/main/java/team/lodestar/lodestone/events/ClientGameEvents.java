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
import team.lodestar.lodestone.handlers.screenparticle.ItemScreenParticleHandler;
import team.lodestar.lodestone.modules.rendering.*;
import team.lodestar.lodestone.modules.toolkit.worldevent.*;
import team.lodestar.lodestone.registry.client.LodestoneModels;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderSystem;
import team.lodestar.lodestone.systems.rendering.renderpass.RenderPassHandler;


@EventBusSubscriber(value = Dist.CLIENT)
public class ClientGameEvents {

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event) {
        var minecraft = Minecraft.getInstance();
        var level = minecraft.level;
        if (level == null) {
            return;
        }
        if (minecraft.isPaused()) {
            return;
        }
        var camera = minecraft.gameRenderer.getMainCamera();
        WorldEventHandler.tick(level);
        ScreenshakeHandler.clientTick(level, camera);
        ItemScreenParticleHandler.tickItemParticles();
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
        var stage = event.getStage();
        if (stage.equals(RenderLevelStageEvent.Stage.AFTER_SKY)) {
            var minecraft = Minecraft.getInstance();
            var camera = minecraft.gameRenderer.getMainCamera();
            float partial = event.getPartialTick().getGameTimeDeltaPartialTick(false);
            var poseStack = event.getPoseStack();
            WorldEventRenderHandler.renderWorldEvents(minecraft.level, poseStack, camera, partial);
        }
        else if (stage.equals(RenderLevelStageEvent.Stage.AFTER_WEATHER)) {
            LodestoneRenderingSystem.render();
        }
    }

    @SubscribeEvent
    public static void renderFrameEvent(RenderFrameEvent.Post event) {
        if (event != null) {
            ItemScreenParticleHandler.renderTick(event);
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