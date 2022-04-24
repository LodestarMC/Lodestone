package com.sammy.ortus.network;

import com.sammy.ortus.capability.PlayerDataCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncPlayerCapabilityDataServerPacket {
    private final UUID uuid;
    private final CompoundTag tag;

    public SyncPlayerCapabilityDataServerPacket(UUID uuid, CompoundTag tag) {
        this.uuid = uuid;
        this.tag = tag;
    }

    public static SyncPlayerCapabilityDataServerPacket decode(FriendlyByteBuf buf) {
        return new SyncPlayerCapabilityDataServerPacket(buf.readUUID(), buf.readNbt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
        buf.writeNbt(tag);
    }

    public void execute(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player player = context.get().getSender().level.getPlayerByUUID(uuid);
            if (player != null) {
                PlayerDataCapability.getCapability(player).ifPresent(c -> c.deserializeNBT(tag));
            }
        });
        context.get().setPacketHandled(true);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, SyncPlayerCapabilityDataServerPacket.class, SyncPlayerCapabilityDataServerPacket::encode, SyncPlayerCapabilityDataServerPacket::decode, SyncPlayerCapabilityDataServerPacket::execute);
    }
}