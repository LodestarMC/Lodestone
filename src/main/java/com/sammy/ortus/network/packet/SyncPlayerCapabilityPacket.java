package com.sammy.ortus.network.packet;

import com.sammy.ortus.capability.PlayerDataCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class SyncPlayerCapabilityPacket extends OrtusSyncPacket{
    private final UUID uuid;
    public SyncPlayerCapabilityPacket(UUID uuid, CompoundTag tag) {
        super(handleTag(uuid,tag));
        this.uuid = uuid;
    }
    public static CompoundTag handleTag(UUID id,CompoundTag tag){
        tag.putUUID("SyncPlayerCapabilityPacket",id);
        return tag;
    }

    @Override
    public CompoundTag getClientTag(Supplier<NetworkEvent.Context> context) {
        assert Minecraft.getInstance().level != null;
        Player player = Minecraft.getInstance().level.getPlayerByUUID(uuid);
        assert player != null;
        return PlayerDataCapability.getCapability(player).orElse(new PlayerDataCapability()).serializeNBT();

    }

    @Override
    public CompoundTag getServerTag(Supplier<NetworkEvent.Context> context) {
        Player player = Objects.requireNonNull(context.get().getSender()).level.getPlayerByUUID(uuid);
        assert player != null;
        return PlayerDataCapability.getCapability(player).orElse(new PlayerDataCapability()).serializeNBT();
    }

    @Override
    public void modifyClient(Supplier<NetworkEvent.Context> context, CompoundTag tag) {
        assert Minecraft.getInstance().level != null;
        Player player = Minecraft.getInstance().level.getPlayerByUUID(uuid);
        assert player != null;
        PlayerDataCapability.getCapability(player).ifPresent(c -> c.deserializeNBT(tag));

    }

    @Override
    public void modifyServer(Supplier<NetworkEvent.Context> context, CompoundTag tag) {
        Player player = Objects.requireNonNull(context.get().getSender()).level.getPlayerByUUID(uuid);
        assert player != null;
        PlayerDataCapability.getCapability(player).ifPresent(c -> c.deserializeNBT(tag));
    }
}
