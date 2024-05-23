package team.lodestar.lodestone.handlers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import team.lodestar.lodestone.component.LodestoneComponents;
import team.lodestar.lodestone.component.LodestoneEntityComponent;
import team.lodestar.lodestone.network.ClearFireEffectInstancePacket;
import team.lodestar.lodestone.registry.client.LodestoneFireEffectRendererRegistry;
import team.lodestar.lodestone.registry.common.LodestonePacketRegistry;
import team.lodestar.lodestone.systems.fireeffect.FireEffectInstance;
import team.lodestar.lodestone.systems.fireeffect.FireEffectRenderer;

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
        return LodestoneComponents.LODESTONE_ENTITY_COMPONENT.get(entity).fireEffectInstance;
    }

    public static void setCustomFireInstance(Entity entity, FireEffectInstance instance) {
        LodestoneComponents.LODESTONE_ENTITY_COMPONENT.maybeGet(entity).ifPresent(c -> {
            c.fireEffectInstance = instance;
            if (c.fireEffectInstance != null) {
                if (entity.getRemainingFireTicks() > 0) {
                    entity.setRemainingFireTicks(0);
                }
                if (!entity.level().isClientSide) {
                    c.fireEffectInstance.sync(entity);
                }
            } else if (!entity.level().isClientSide) {
                LodestonePacketRegistry.LODESTONE_CHANNEL.sendToClientsTracking(new ClearFireEffectInstancePacket(entity.getId()), entity);
            }
        });
    }

    public static void serializeNBT(LodestoneEntityComponent capability, CompoundTag tag) {
        if (capability.fireEffectInstance != null) {
            capability.fireEffectInstance.serializeNBT(tag);
        }
    }

    public static void deserializeNBT(LodestoneEntityComponent capability, CompoundTag tag) {
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