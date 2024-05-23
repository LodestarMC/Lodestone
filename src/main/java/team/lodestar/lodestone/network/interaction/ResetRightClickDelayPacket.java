package team.lodestar.lodestone.network.interaction;

import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import team.lodestar.lodestone.systems.network.LodestoneClientPacket;

import java.util.function.Supplier;

public class ResetRightClickDelayPacket extends LodestoneClientPacket {

    @Override
    public void executeClient(Minecraft client, ClientPacketListener listener, PacketSender responseSender, SimpleChannel channel) {
        Minecraft.getInstance().rightClickDelay = 0;
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, ResetRightClickDelayPacket.class, ResetRightClickDelayPacket::encode, ResetRightClickDelayPacket::decode, ResetRightClickDelayPacket::handle);
    }

    public static ResetRightClickDelayPacket decode(FriendlyByteBuf buf) {
        return new ResetRightClickDelayPacket();
    }
}