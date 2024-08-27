package team.lodestar.lodestone.capabilityold;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import team.lodestar.lodestone.helpers.NBTHelper;
import team.lodestar.lodestone.systems.fireeffect.FireEffectInstance;

@Deprecated(forRemoval = true)
public class LodestoneEntityDataCapability  {



    public static void syncTrackingAndSelf(Entity entity) {
        sync(entity, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity));
    }

    public static void syncTracking(Entity entity) {
        sync(entity, PacketDistributor.TRACKING_ENTITY.with(() -> entity));
    }

    public static void sync(Entity entity, PacketDistributor.PacketTarget target) {
        getCapabilityOptional(entity).ifPresent(c -> LodestonePacketRegistry.LODESTONE_CHANNEL.send(target, new SyncLodestoneEntityCapabilityPacket(entity.getId(), c.serializeNBT())));
    }

    public static void syncTrackingAndSelf(Entity entity, NBTHelper.TagFilter filter) {
        sync(entity, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), filter);
    }

    public static void syncTracking(Entity entity, NBTHelper.TagFilter filter) {
        sync(entity, PacketDistributor.TRACKING_ENTITY.with(() -> entity), filter);
    }

    public static void sync(Entity entity, PacketDistributor.PacketTarget target, NBTHelper.TagFilter filter) {
        getCapabilityOptional(entity).ifPresent(c -> LodestonePacketRegistry.LODESTONE_CHANNEL.send(target, new SyncLodestoneEntityCapabilityPacket(entity.getId(), NBTHelper.filterTag(c.serializeNBT(), filter))));
    }

    public static LazyOptional<LodestoneEntityDataCapability> getCapabilityOptional(Entity entity) {
        return entity.getCapability(CAPABILITY);
    }

    public static LodestoneEntityDataCapability getCapability(Entity entity) {
        return entity.getCapability(CAPABILITY).orElse(new LodestoneEntityDataCapability());
    }
}