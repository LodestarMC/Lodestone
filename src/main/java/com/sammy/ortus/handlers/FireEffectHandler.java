package com.sammy.ortus.handlers;

import com.mojang.blaze3d.vertex.*;
import com.sammy.ortus.capability.OrtusEntityDataCapability;
import com.sammy.ortus.capability.OrtusWorldDataCapability;
import com.sammy.ortus.helpers.NBTHelper;
import com.sammy.ortus.network.ClearFireEffectInstancePacket;
import com.sammy.ortus.network.capability.SyncOrtusEntityCapabilityPacket;
import com.sammy.ortus.setup.OrtusFireEffectRendererRegistry;
import com.sammy.ortus.setup.worldevent.OrtusWorldEventTypeRegistry;
import com.sammy.ortus.systems.fireeffect.FireEffectInstance;
import com.sammy.ortus.systems.fireeffect.FireEffectRenderer;
import com.sammy.ortus.systems.worldevent.WorldEventInstance;
import com.sammy.ortus.systems.worldevent.WorldEventType;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;

import static com.sammy.ortus.setup.OrtusPacketRegistry.ORTUS_CHANNEL;

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
        return OrtusEntityDataCapability.getCapability(entity).fireEffectInstance;
    }

    public static void setCustomFireInstance(Entity entity, FireEffectInstance instance) {
        OrtusEntityDataCapability.getCapabilityOptional(entity).ifPresent(c -> {
            c.fireEffectInstance = instance;
            if (c.fireEffectInstance != null) {
                if (entity.getRemainingFireTicks() > 0) {
                    entity.setRemainingFireTicks(0);
                }
                if (!entity.level.isClientSide) {
                    c.fireEffectInstance.sync(entity);
                }
            }
            else if (!entity.level.isClientSide) {
                ORTUS_CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new ClearFireEffectInstancePacket(entity.getId()));
            }
        });
    }

    public static void serializeNBT(OrtusEntityDataCapability capability, CompoundTag tag) {
        if (capability.fireEffectInstance != null) {
            capability.fireEffectInstance.serializeNBT(tag);
        }
    }

    public static void deserializeNBT(OrtusEntityDataCapability capability, CompoundTag tag) {
        capability.fireEffectInstance = FireEffectInstance.deserializeNBT(tag);
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
            if (renderer != null) {
                if (renderer.canRender(instance)) {
                    renderer.renderScreen(instance, pMinecraft, pPoseStack);
                }
            }
        }

        public static void renderWorldMeteorFire(PoseStack pMatrixStack, MultiBufferSource pBuffer, Camera camera, Entity pEntity) {
            if (getFireEffectInstance(pEntity) == null) {
                return;
            }
            FireEffectInstance instance = getFireEffectInstance(pEntity);
            FireEffectRenderer<FireEffectInstance> renderer = OrtusFireEffectRendererRegistry.RENDERERS.get(instance.type);
            if (renderer != null) {
                if (renderer.canRender(instance)) {
                    renderer.renderWorld(instance, pMatrixStack, pBuffer, camera, pEntity);
                }
            }
        }

        private static void fireVertex(PoseStack.Pose pMatrixEntry, VertexConsumer pBuffer, float pX, float pY, float pZ, float pTexU, float pTexV) {
            pBuffer.vertex(pMatrixEntry.pose(), pX, pY, pZ).color(255, 255, 255, 255).uv(pTexU, pTexV).overlayCoords(0, 10).uv2(240).normal(pMatrixEntry.normal(), 0.0F, 1.0F, 0.0F).endVertex();
        }
    }
}