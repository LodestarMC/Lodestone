package team.lodestar.lodestone.network.interaction;

import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import team.lodestar.lodestone.systems.network.LodestoneClientPacket;

import java.util.function.Supplier;

public class ResetRightClickDelayPacket extends LodestoneClientPacket {

    public ResetRightClickDelayPacket(FriendlyByteBuf buf) {

    }

    @Override
    public void executeClient(Minecraft client, ClientPacketListener listener, PacketSender responseSender, SimpleChannel channel) {
        Minecraft.getInstance().rightClickDelay = 0;
    }
}