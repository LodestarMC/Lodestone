package team.lodestar.lodestone.events;

import net.minecraftforge.client.event.EntityViewRenderEvent;
import team.lodestar.lodestone.handlers.*;
import team.lodestar.lodestone.systems.client.ClientTickCounter;
import com.mojang.blaze3d.vertex.PoseStack;
import team.lodestar.lodestone.LodestoneLibClient;
import team.lodestar.lodestone.capability.LodestonePlayerDataCapability;
import team.lodestar.lodestone.helpers.util.AnimationTickHolder;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.gui.screens.SimpleOptionsSubScreen;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static team.lodestar.lodestone.LodestoneLib.RANDOM;
import static team.lodestar.lodestone.setup.LodestoneOptionRegistry.OPTIONS;

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
                PlacementAssistantHandler.clientTick();
                ScreenshakeHandler.clientTick(camera, RANDOM);
                LodestonePlayerDataCapability.ClientOnly.clientTick(event);
                ScreenParticleHandler.tickParticles();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void renderFog(EntityViewRenderEvent.RenderFogEvent event) {
        RenderHandler.cacheFogData(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void fogColors(EntityViewRenderEvent.FogColors event) {
        RenderHandler.cacheFogData(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void renderLast(RenderLevelLastEvent event) {
        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        float partial = AnimationTickHolder.getPartialTicks();
        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();
        poseStack.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());
        GhostBlockHandler.renderGhosts(poseStack);
        LodestoneLibClient.OUTLINER.renderOutlines(poseStack, partial);
        WorldEventHandler.ClientOnly.renderWorldEvents(event);
        RenderHandler.renderBufferedBatches(event);
        poseStack.popPose();
    }

    @SubscribeEvent
    public static void setupScreen(ScreenEvent.InitScreenEvent.Post event) {
        if (event.getScreen() instanceof SimpleOptionsSubScreen subScreen) {
            subScreen.list.addSmall(OPTIONS.stream().filter(e -> e.canAdd(event)).toArray(Option[]::new));
        }
    }

    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event) {
        ScreenParticleHandler.renderParticles(event);
        ClientTickCounter.renderTick(event);
    }
}