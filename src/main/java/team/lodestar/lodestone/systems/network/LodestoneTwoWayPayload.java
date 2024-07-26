package team.lodestar.lodestone.systems.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class LodestoneTwoWayPayload implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, LodestoneTwoWayPayload> STREAM_CODEC =
            CustomPacketPayload.codec(LodestoneTwoWayPayload::write, LodestoneTwoWayPayload::new);

    public static final CustomPacketPayload.Type<LodestoneTwoWayPayload> TYPE = CustomPacketPayload.createType("lodestone/common");

    public LodestoneTwoWayPayload() {

    }
    
    public LodestoneTwoWayPayload(FriendlyByteBuf friendlyByteBuf) {

    }

    private void write(FriendlyByteBuf friendlyByteBuf) {
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Client {

        public static void handleClient(CustomPacketPayload payload, IPayloadContext ctx) {
        }
    }

    public static class Server {

        public static void handleServer(LodestoneTwoWayPayload payload, IPayloadContext ctx) {
        }
    }
}
