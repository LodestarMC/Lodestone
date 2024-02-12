package team.lodestar.lodestone.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import team.lodestar.lodestone.capability.LodestonePlayerDataCapability;
import team.lodestar.lodestone.handlers.*;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.systems.client.ClientTickCounter;

import static team.lodestar.lodestone.LodestoneLib.RANDOM;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientRuntimeEvents {

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.END)) {
            Minecraft minecraft = Minecraft.getInstance();
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
                LodestonePlayerDataCapability.ClientOnly.clientTick(event);
                ScreenParticleHandler.tickParticles();
            }
        }
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
        float partial = event.getPartialTick();
        PoseStack poseStack = event.getPoseStack();
        LevelRenderer levelRenderer = Minecraft.getInstance().levelRenderer;
        poseStack.pushPose();
        poseStack.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());

        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_SKY)) {
            GhostBlockHandler.renderGhosts(poseStack);
            WorldEventHandler.ClientOnly.renderWorldEvents(poseStack, partial);
        }

        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_PARTICLES)) {
            RenderHandler.MATRIX4F = new Matrix4f(RenderSystem.getModelViewMatrix());
        }
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_WEATHER)) {
            Matrix4f last = new Matrix4f(RenderSystem.getModelViewMatrix());
            final boolean isFabulous = levelRenderer.transparencyChain != null;
            if (isFabulous) {
                RenderHandler.LODESTONE_TRANSLUCENT_TARGET.clear(Minecraft.ON_OSX);
                RenderHandler.LODESTONE_TRANSLUCENT_TARGET.bindWrite(false);
            }
            RenderHandler.beginBufferedRendering(poseStack);
            RenderHandler.renderBufferedParticles(true);
            if (RenderHandler.MATRIX4F != null) {
                RenderSystem.getModelViewMatrix().set(RenderHandler.MATRIX4F);
            }
            RenderHandler.renderBufferedBatches(true);
            if (isFabulous) {
                RenderHandler.LODESTONE_TRANSLUCENT_TARGET.unbindWrite();
                RenderHandler.LODESTONE_ADDITIVE_TARGET.clear(Minecraft.ON_OSX);
                RenderHandler.LODESTONE_ADDITIVE_TARGET.bindWrite(false);
            }
            RenderHandler.renderBufferedBatches(false);
            RenderSystem.getModelViewMatrix().set(last);
            RenderHandler.renderBufferedParticles(false);

            RenderHandler.endBufferedRendering(poseStack);
            if (isFabulous) {
                levelRenderer.getCloudsTarget().bindWrite(false);
            }
        }
//        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_LEVEL)) {
//            final PostChain transparencyChain = levelRenderer.transparencyChain;
//            if (transparencyChain != null) {
//                RenderHandler.LODESTONE_TARGET.blitToScreen(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
//            }
//        }
        poseStack.popPose();
    }

    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event) {
        ScreenParticleHandler.renderTick(event);
        ClientTickCounter.renderTick(event);
    }
}