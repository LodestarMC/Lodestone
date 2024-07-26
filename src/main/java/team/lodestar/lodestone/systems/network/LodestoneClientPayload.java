package team.lodestar.lodestone.systems.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.BrainDebugPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class LodestoneClientPayload implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, LodestoneClientPayload> STREAM_CODEC =
           CustomPacketPayload.codec(LodestoneClientPayload::write, LodestoneClientPayload::new);

    public static final CustomPacketPayload.Type<LodestoneClientPayload> TYPE = CustomPacketPayload.createType("lodestone/client");

    public LodestoneClientPayload() {

    }

    public LodestoneClientPayload(FriendlyByteBuf friendlyByteBuf) {

    }

    private void write(FriendlyByteBuf friendlyByteBuf) {
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(LodestoneClientPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
           handleClient(payload, ctx);
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(LodestoneClientPayload payload, IPayloadContext ctx) {

    }
}
