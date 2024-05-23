package team.lodestar.lodestone.network.interaction;

import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import team.lodestar.lodestone.capability.LodestonePlayerDataCapability;
import team.lodestar.lodestone.events.types.RightClickEmptyServer;
import team.lodestar.lodestone.systems.network.LodestoneServerPacket;

import java.util.function.Supplier;

public class UpdateRightClickPacket extends LodestoneServerPacket {

    private final boolean rightClickHeld;

    public UpdateRightClickPacket(boolean rightClick) {
        this.rightClickHeld = rightClick;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(rightClickHeld);
    }

    @Override
    public void executeServer(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, PacketSender responseSender, SimpleChannel channel) {
        if (rightClickHeld) {
            RightClickEmptyServer.onRightClickEmptyServer(player);
        }
        LodestonePlayerDataCapability.getCapabilityOptional(player).ifPresent(c -> c.rightClickHeld = rightClickHeld);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, UpdateRightClickPacket.class, UpdateRightClickPacket::encode, UpdateRightClickPacket::decode, UpdateRightClickPacket::handle);
    }

    public static UpdateRightClickPacket decode(FriendlyByteBuf buf) {
        return new UpdateRightClickPacket(buf.readBoolean());
    }
}