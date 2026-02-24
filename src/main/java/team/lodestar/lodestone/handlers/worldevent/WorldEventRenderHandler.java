package team.lodestar.lodestone.handlers.worldevent;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import team.lodestar.lodestone.handlers.rendering.*;
import team.lodestar.lodestone.handlers.events.types.worldevent.WorldEventRenderEvent;
import team.lodestar.lodestone.registry.client.LodestoneWorldEventRenderers;
import team.lodestar.lodestone.registry.common.LodestoneAttachmentTypes;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventRenderer;

public class WorldEventRenderHandler {
    public static void renderWorldEvents(ClientLevel level, PoseStack poseStack, Camera camera, float partialTicks) {
        Vec3 cameraPos = camera.getPosition();
        poseStack.pushPose();
        poseStack.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());

        var worldData = level.getData(LodestoneAttachmentTypes.WORLD_EVENT_DATA);
        for (WorldEventInstance instance : worldData.activeWorldEvents) {
            WorldEventRenderer<WorldEventInstance> renderer = LodestoneWorldEventRenderers.RENDERERS.get(instance.type);
            if (renderer != null) {
                if (renderer.canRender(instance)) {
                    MultiBufferSource.BufferSource target = LodestoneRenderHandler.DEFERRED_RENDER.getTarget();
                    NeoForge.EVENT_BUS.post(new WorldEventRenderEvent(instance, renderer, poseStack, target, partialTicks));
                    renderer.render(instance, poseStack, target, partialTicks);
                }
            }
        }
        poseStack.popPose();
    }
}
