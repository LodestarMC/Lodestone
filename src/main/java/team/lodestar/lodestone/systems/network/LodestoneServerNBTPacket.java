package team.lodestar.lodestone.systems.network;

import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;


public abstract class LodestoneServerNBTPacket extends LodestoneServerPacket {
    protected CompoundTag data;

    public LodestoneServerNBTPacket(CompoundTag data) {
        this.data = data;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(data);
    }

    @Override
    public void executeServer(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, PacketSender responseSender, SimpleChannel channel) {
        executeServerNbt(server, player, listener, responseSender, channel, data);
    }

    public void executeServerNbt(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, PacketSender responseSender, SimpleChannel channel, CompoundTag data) {
    }
}