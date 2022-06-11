package com.sammy.ortus.network.packet;

import com.sammy.ortus.capability.OrtusPlayerDataCapability;
import com.sammy.ortus.network.screenshake.ScreenshakePacket;
import com.sammy.ortus.systems.network.OrtusTwoWayNBTPacket;
import com.sammy.ortus.systems.network.OrtusTwoWayPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.UUID;
import java.util.function.Supplier;

@SuppressWarnings("ConstantConditions")
public class SyncOrtusPlayerCapabilityPacket extends OrtusTwoWayNBTPacket {

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

    @OnlyIn(value = Dist.CLIENT)
    @Override
    public void clientExecute(Supplier<NetworkEvent.Context> context, CompoundTag data) {
        Player player = Minecraft.getInstance().level.getPlayerByUUID(uuid);
        OrtusPlayerDataCapability.getCapabilityOptional(player).ifPresent(c -> c.deserializeNBT(data));
    }

    @Override
    public void serverExecute(Supplier<NetworkEvent.Context> context, CompoundTag data) {
        Player player = context.get().getSender().level.getPlayerByUUID(uuid);
        OrtusPlayerDataCapability.getCapabilityOptional(player).ifPresent(c -> c.deserializeNBT(data));
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, SyncOrtusPlayerCapabilityPacket.class, SyncOrtusPlayerCapabilityPacket::encode, SyncOrtusPlayerCapabilityPacket::decode, SyncOrtusPlayerCapabilityPacket::handle);
    }

    public static SyncOrtusPlayerCapabilityPacket decode(FriendlyByteBuf buf) {
        return new SyncOrtusPlayerCapabilityPacket(buf.readNbt());
    }
}