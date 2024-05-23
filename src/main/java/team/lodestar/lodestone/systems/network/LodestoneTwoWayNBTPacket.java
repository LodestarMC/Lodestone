package team.lodestar.lodestone.systems.network;

import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public abstract class LodestoneTwoWayNBTPacket extends LodestoneTwoWayPacket {
    protected CompoundTag data;

    public LodestoneTwoWayNBTPacket(CompoundTag data) {
        this.data = data;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(data);
    }

    @Override
    public void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, PacketSender responseSender, SimpleChannel channel) {
        serverExecute(server, player, listener, responseSender, channel, data);
    }

    public void serverExecute(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, PacketSender responseSender, SimpleChannel channel, CompoundTag data) {
    }

    @Override
    public void handle(Minecraft client, ClientPacketListener listener, PacketSender responseSender, SimpleChannel channel) {
        super.handle(client, listener, responseSender, channel);
        clientExecute(client, listener, responseSender, channel, data);
    }

    @Environment(EnvType.CLIENT)
    public void clientExecute(Minecraft client, ClientPacketListener listener, PacketSender responseSender, SimpleChannel channel, CompoundTag data) {
    }
}