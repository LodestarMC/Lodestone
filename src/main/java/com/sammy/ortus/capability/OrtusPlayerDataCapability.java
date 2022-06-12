package com.sammy.ortus.capability;

import com.sammy.ortus.OrtusLib;
import com.sammy.ortus.helpers.NBTHelper;
import com.sammy.ortus.network.interaction.UpdateLeftClickPacket;
import com.sammy.ortus.network.interaction.UpdateRightClickPacket;
import com.sammy.ortus.network.capability.SyncOrtusPlayerCapabilityPacket;
import com.sammy.ortus.systems.capability.OrtusCapability;
import com.sammy.ortus.systems.capability.OrtusCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.PacketDistributor;

import static com.sammy.ortus.setup.OrtusPacketRegistry.ORTUS_CHANNEL;

public class OrtusPlayerDataCapability implements OrtusCapability {

    public static Capability<OrtusPlayerDataCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public boolean hasJoinedBefore;
    public boolean rightClickHeld;
    public int rightClickTime;
    public boolean leftClickHeld;
    public int leftClickTime;


    public OrtusPlayerDataCapability() {
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(OrtusPlayerDataCapability.class);
    }

    public static void attachPlayerCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            final OrtusPlayerDataCapability capability = new OrtusPlayerDataCapability();
            event.addCapability(OrtusLib.ortusPrefix("player_data"), new OrtusCapabilityProvider<>(OrtusPlayerDataCapability.CAPABILITY, () -> capability));
        }
    }

    public static void playerJoin(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            OrtusPlayerDataCapability.getCapabilityOptional(serverPlayer).ifPresent(capability -> capability.hasJoinedBefore = true);
            syncSelf(serverPlayer);
        }
    }

    public static void syncPlayerCapability(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Player player) {
            if (player.level instanceof ServerLevel) {
                syncTracking(player);
            }
        }
    }

    public static void playerTick(TickEvent.PlayerTickEvent event) {
        OrtusPlayerDataCapability.getCapabilityOptional(event.player).ifPresent(c -> {
            c.rightClickTime = c.rightClickHeld ? c.rightClickTime + 1 : 0;
            c.leftClickTime = c.leftClickHeld ? c.leftClickTime + 1 : 0;
        });
    }

    public static void playerClone(PlayerEvent.Clone event) {
        event.getOriginal().revive();
        OrtusPlayerDataCapability.getCapabilityOptional(event.getOriginal()).ifPresent(o -> OrtusPlayerDataCapability.getCapabilityOptional(event.getPlayer()).ifPresent(c -> {
            c.deserializeNBT(o.serializeNBT());
        }));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("firstTimeJoin", hasJoinedBefore);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        hasJoinedBefore = tag.getBoolean("firstTimeJoin");
    }

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
        getCapabilityOptional(player).ifPresent(c -> ORTUS_CHANNEL.send(target, new SyncOrtusPlayerCapabilityPacket(player.getUUID(), NBTHelper.filterTag(c.serializeNBT(), filter))));
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
        getCapabilityOptional(player).ifPresent(c -> ORTUS_CHANNEL.send(target, new SyncOrtusPlayerCapabilityPacket(player.getUUID(), c.serializeNBT())));
    }

    public static LazyOptional<OrtusPlayerDataCapability> getCapabilityOptional(Player player) {
        return player.getCapability(CAPABILITY);
    }

    public static OrtusPlayerDataCapability getCapability(Player player) {
        return player.getCapability(CAPABILITY).orElse(new OrtusPlayerDataCapability());
    }

    public static class ClientOnly {
        public static void clientTick(TickEvent.ClientTickEvent event) {
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;
            OrtusPlayerDataCapability.getCapabilityOptional(player).ifPresent(c -> {
                boolean left = minecraft.options.keyAttack.isDown();
                boolean right = minecraft.options.keyUse.isDown();
                if (left != c.leftClickHeld) {
                    c.leftClickHeld = left;
                    ORTUS_CHANNEL.send(PacketDistributor.SERVER.noArg(), new UpdateLeftClickPacket(c.leftClickHeld));
                }
                if (right != c.rightClickHeld) {
                    c.rightClickHeld = right;
                    ORTUS_CHANNEL.send(PacketDistributor.SERVER.noArg(), new UpdateRightClickPacket(c.rightClickHeld));
                }
            });
        }
    }
}