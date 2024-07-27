package team.lodestar.lodestone.networkold.interaction;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
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
    public void execute(Supplier<NetworkEvent.Context> context) {
        if (rightClickHeld) {
            RightClickEmptyServer.onRightClickEmptyServer(context.get().getSender());
        }
        LodestonePlayerDataCapability.getCapabilityOptional(context.get().getSender()).ifPresent(c -> c.rightClickHeld = rightClickHeld);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, UpdateRightClickPacket.class, UpdateRightClickPacket::encode, UpdateRightClickPacket::decode, UpdateRightClickPacket::handle);
    }

    public static UpdateRightClickPacket decode(FriendlyByteBuf buf) {
        return new UpdateRightClickPacket(buf.readBoolean());
    }
}