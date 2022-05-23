package com.sammy.ortus.network.packet;

import com.sammy.ortus.capability.OrtusPlayerDataCapability;
import com.sammy.ortus.systems.network.OrtusSyncPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

@SuppressWarnings("ConstantConditions")
public class SyncOrtusPlayerCapabilityPacket extends OrtusSyncPacket {
    public static final String PLAYER_UUID = "player_uuid";

    private final UUID uuid;

    public SyncOrtusPlayerCapabilityPacket(CompoundTag tag) {
        super(tag);
        this.uuid = tag.getUUID(PLAYER_UUID);
    }

    public SyncOrtusPlayerCapabilityPacket(UUID uuid, CompoundTag tag) {
        super(handleTag(uuid, tag));
        this.uuid = uuid;
    }

    public static CompoundTag handleTag(UUID id, CompoundTag tag) {
        tag.putUUID(PLAYER_UUID, id);
        return tag;
    }

    @Override
    public void modifyClient(Supplier<NetworkEvent.Context> context, CompoundTag tag) {
        Player player = Minecraft.getInstance().level.getPlayerByUUID(uuid);
        OrtusPlayerDataCapability.getCapabilityOptional(player).ifPresent(c -> c.deserializeNBT(tag));
    }

    @Override
    public void modifyServer(Supplier<NetworkEvent.Context> context, CompoundTag tag) {
        Player player = context.get().getSender().level.getPlayerByUUID(uuid);
        OrtusPlayerDataCapability.getCapabilityOptional(player).ifPresent(c -> c.deserializeNBT(tag));
    }
}