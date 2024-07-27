package team.lodestar.lodestone.networkold.interaction;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.events.types.RightClickEmptyServer;
import team.lodestar.lodestone.systems.network.LodestoneServerPacket;

import java.util.function.Supplier;

public class RightClickEmptyPacket extends LodestoneServerPacket {

    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        RightClickEmptyServer.onRightClickEmptyServer(context.get().getSender());
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, RightClickEmptyPacket.class, RightClickEmptyPacket::encode, RightClickEmptyPacket::decode, RightClickEmptyPacket::handle);
    }

    public static RightClickEmptyPacket decode(FriendlyByteBuf buf) {
        return new RightClickEmptyPacket();
    }
}