package team.lodestar.lodestone.handlers;

import team.lodestar.lodestone.capability.LodestoneEntityDataCapability;
import team.lodestar.lodestone.network.ClearFireEffectInstancePacket;
import team.lodestar.lodestone.setup.LodestoneFireEffectRendererRegistry;
import team.lodestar.lodestone.setup.LodestonePacketRegistry;
import team.lodestar.lodestone.systems.fireeffect.FireEffectInstance;
import team.lodestar.lodestone.systems.fireeffect.FireEffectRenderer;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;

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
        return LodestoneEntityDataCapability.getCapability(entity).fireEffectInstance;
    }

    public static void setCustomFireInstance(Entity entity, FireEffectInstance instance) {
        LodestoneEntityDataCapability.getCapabilityOptional(entity).ifPresent(c -> {
            c.fireEffectInstance = instance;
            if (c.fireEffectInstance != null) {
                if (entity.getRemainingFireTicks() > 0) {
                    entity.setRemainingFireTicks(0);
                }
                if (!entity.level().isClientSide) {
                    c.fireEffectInstance.sync(entity);
                }
            }
            else if (!entity.level().isClientSide) {
                LodestonePacketRegistry.ORTUS_CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new ClearFireEffectInstancePacket(entity.getId()));
            }
        });
    }

    public static void serializeNBT(LodestoneEntityDataCapability capability, CompoundTag tag) {
        if (capability.fireEffectInstance != null) {
            capability.fireEffectInstance.serializeNBT(tag);
        }
    }

    public static void deserializeNBT(LodestoneEntityDataCapability capability, CompoundTag tag) {
        capability.fireEffectInstance = FireEffectInstance.deserializeNBT(tag);
    }

    public static class ClientOnly {
        public static void renderUIFireEffect(Minecraft pMinecraft, PoseStack pPoseStack) {
            if (pMinecraft.player != null) {
                if (getFireEffectInstance(pMinecraft.player) == null) {
                    return;
                }
            }
            FireEffectInstance instance = getFireEffectInstance(pMinecraft.player);
            FireEffectRenderer<FireEffectInstance> renderer = LodestoneFireEffectRendererRegistry.RENDERERS.get(instance.type);
            if (renderer != null) {
                if (renderer.canRender(instance)) {
                    renderer.renderScreen(instance, pMinecraft, pPoseStack);
                }
            }
        }

        public static void renderWorldFireEffect(PoseStack pMatrixStack, MultiBufferSource pBuffer, Camera camera, Entity pEntity) {
            if (getFireEffectInstance(pEntity) == null) {
                return;
            }
            FireEffectInstance instance = getFireEffectInstance(pEntity);
            FireEffectRenderer<FireEffectInstance> renderer = LodestoneFireEffectRendererRegistry.RENDERERS.get(instance.type);
            if (renderer != null) {
                if (renderer.canRender(instance)) {
                    renderer.renderWorld(instance, pMatrixStack, pBuffer, camera, pEntity);
                }
            }
        }
    }
}