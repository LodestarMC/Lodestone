package com.sammy.ortus.handlers;

import com.mojang.blaze3d.vertex.*;
import com.sammy.ortus.capability.EntityDataCapability;
import com.sammy.ortus.setup.OrtusFireEffectRendererRegistry;
import com.sammy.ortus.systems.fireeffect.FireEffectInstance;
import com.sammy.ortus.systems.fireeffect.FireEffectRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;

public class FireEffectHandler {

    public static void entityUpdate(Entity entity) {
        FireEffectInstance instance = getFireEffectInstance(entity);
        if (instance != null) {
            instance.tick(entity);
            if (!instance.isValid()) {
                setCustomFireInstance(entity, null);
            }
        }
    }

    public static void onVanillaFireTimeUpdate(Entity entity) {
        setCustomFireInstance(entity, null);
    }

    public static FireEffectInstance getFireEffectInstance(Entity entity) {
        EntityDataCapability capability = EntityDataCapability.getCapability(entity).orElse(new EntityDataCapability());
        return capability.fireEffectInstance;
    }

    public static void setCustomFireInstance(Entity entity, FireEffectInstance instance) {
        EntityDataCapability.getCapability(entity).ifPresent(c -> {
            c.fireEffectInstance = instance;
            if (instance != null) {
                if (entity.getRemainingFireTicks() > 0) {
                    entity.setRemainingFireTicks(0);
                }
            }
            if (!entity.level.isClientSide) {
                EntityDataCapability.syncTrackingAndSelf(entity);
            }
        });
    }

    public static class ClientOnly {

        public static void renderUIMeteorFire(Minecraft pMinecraft, PoseStack pPoseStack) {
            if (pMinecraft.player != null) {
                if (getFireEffectInstance(pMinecraft.player) == null) {
                    return;
                }
            }
            FireEffectInstance instance = getFireEffectInstance(pMinecraft.player);
            FireEffectRenderer<FireEffectInstance> renderer = OrtusFireEffectRendererRegistry.RENDERERS.get(instance.type);
            if (renderer.canRender(instance)) {
                renderer.renderScreen(instance, pMinecraft, pPoseStack);
            }
        }

        public static void renderWorldMeteorFire(PoseStack pMatrixStack, MultiBufferSource pBuffer, Camera camera, Entity pEntity) {
            if (getFireEffectInstance(pEntity) == null) {
                return;
            }
            FireEffectInstance instance = getFireEffectInstance(pEntity);
            FireEffectRenderer<FireEffectInstance> renderer = OrtusFireEffectRendererRegistry.RENDERERS.get(instance.type);
            if (renderer.canRender(instance)) {
                renderer.renderWorld(instance, pMatrixStack, pBuffer, camera, pEntity);
            }
        }

        private static void fireVertex(PoseStack.Pose pMatrixEntry, VertexConsumer pBuffer, float pX, float pY, float pZ, float pTexU, float pTexV) {
            pBuffer.vertex(pMatrixEntry.pose(), pX, pY, pZ).color(255, 255, 255, 255).uv(pTexU, pTexV).overlayCoords(0, 10).uv2(240).normal(pMatrixEntry.normal(), 0.0F, 1.0F, 0.0F).endVertex();
        }
    }
}