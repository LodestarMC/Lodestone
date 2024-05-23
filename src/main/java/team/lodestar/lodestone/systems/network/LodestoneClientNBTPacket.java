package team.lodestar.lodestone.systems.network;

import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public abstract class LodestoneClientNBTPacket extends LodestoneClientPacket {
    protected CompoundTag data;

    public LodestoneClientNBTPacket(CompoundTag data) {
        this.data = data;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(data);
    }

    @Override
    public void executeClient(Minecraft client, ClientPacketListener listener, PacketSender responseSender, SimpleChannel channel) {
        executeClientNbt(client, listener, responseSender, channel, data);
    }

    @Environment(EnvType.CLIENT)
    public void executeClientNbt(Minecraft client, ClientPacketListener listener, PacketSender responseSender, SimpleChannel channel, CompoundTag data) {
    }
}