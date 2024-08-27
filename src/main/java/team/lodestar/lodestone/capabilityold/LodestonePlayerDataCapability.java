package team.lodestar.lodestone.capabilityold;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.helpers.NBTHelper;
import team.lodestar.lodestone.networkold.capability.SyncLodestonePlayerCapabilityPacket;

@Deprecated(forRemoval = true)
public class LodestonePlayerDataCapability {



    public static void syncServer(Player player, NBTHelper.TagFilter filter) {
        sync(player, PacketDistributor.SERVER.noArg(), filter);
    }

    public static void syncSelf(ServerPlayer player, NBTHelper.TagFilter filter) {
        sync(player, PacketDistributor.PLAYER.with(() -> player), filter);
    }

    public static void syncTrackingAndSelf(Player player, NBTHelper.TagFilter filter) {
        sync(player, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), filter);
    }

    public static void syncTracking(Player player, NBTHelper.TagFilter filter) {
        sync(player, PacketDistributor.TRACKING_ENTITY.with(() -> player), filter);
    }

    public static void sync(Player player, PacketDistributor.PacketTarget target, NBTHelper.TagFilter filter) {
        getCapabilityOptional(player).ifPresent(c -> LodestonePacketRegistry.LODESTONE_CHANNEL.send(target, new SyncLodestonePlayerCapabilityPacket(player.getUUID(), NBTHelper.filterTag(c.serializeNBT(), filter))));
    }

    public static void syncServer(Player player) {
        sync(player, PacketDistributor.SERVER.noArg());
    }

    public static void syncSelf(ServerPlayer player) {
        sync(player, PacketDistributor.PLAYER.with(() -> player));
    }

    public static void syncTrackingAndSelf(Player player) {
        sync(player, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player));
    }

    public static void syncTracking(Player player) {
        sync(player, PacketDistributor.TRACKING_ENTITY.with(() -> player));
    }

    public static void sync(Player player, PacketDistributor.PacketTarget target) {
        getCapabilityOptional(player).ifPresent(c -> LodestonePacketRegistry.LODESTONE_CHANNEL.send(target, new SyncLodestonePlayerCapabilityPacket(player.getUUID(), c.serializeNBT())));
    }

    public static LazyOptional<LodestonePlayerDataCapability> getCapabilityOptional(Player player) {
        return player.getCapability(CAPABILITY);
    }

    public static LodestonePlayerDataCapability getCapability(Player player) {
        return player.getCapability(CAPABILITY).orElse(new LodestonePlayerDataCapability());
    }


}