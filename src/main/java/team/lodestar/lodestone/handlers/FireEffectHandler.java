package team.lodestar.lodestone.handlers;

import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import team.lodestar.lodestone.network.ClearFireEffectInstancePayload;
import team.lodestar.lodestone.registry.common.LodestoneAttachmentTypes;
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
        return entity.getData(LodestoneAttachmentTypes.ENTITY_DATA).fireEffectInstance;
    }

    public static void setCustomFireInstance(Entity entity, FireEffectInstance instance) {
        var oldInstance = entity.getData(LodestoneAttachmentTypes.ENTITY_DATA);

        oldInstance.fireEffectInstance = instance;
        if (oldInstance.fireEffectInstance != null) {
            if (entity.getRemainingFireTicks() > 0) {
                entity.setRemainingFireTicks(0);
            }
            if (!entity.level().isClientSide) {
                oldInstance.fireEffectInstance.sync(entity);
            }
        } else if (!entity.level().isClientSide) {
            var buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeInt(entity.getId());
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new ClearFireEffectInstancePayload(buf));
            //LodestonePacketRegistry.LODESTONE_CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new ClearFireEffectInstancePacket(entity.getId()));
        }

        entity.setData(LodestoneAttachmentTypes.ENTITY_DATA, oldInstance);
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