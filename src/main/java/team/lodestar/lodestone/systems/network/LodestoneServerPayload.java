package team.lodestar.lodestone.systems.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class LodestoneServerPayload implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, LodestoneServerPayload> STREAM_CODEC =
            CustomPacketPayload.codec(LodestoneServerPayload::write, LodestoneServerPayload::new);

    public static final CustomPacketPayload.Type<LodestoneServerPayload> TYPE = CustomPacketPayload.createType("lodestone/server");


    public LodestoneServerPayload() {

    }

    public LodestoneServerPayload(FriendlyByteBuf friendlyByteBuf) {

    }

    private void write(FriendlyByteBuf friendlyByteBuf) {
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(LodestoneServerPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            handleServer(payload, ctx);
        });
    }

    public static void handleServer(LodestoneServerPayload payload, IPayloadContext ctx) {

    }
}
