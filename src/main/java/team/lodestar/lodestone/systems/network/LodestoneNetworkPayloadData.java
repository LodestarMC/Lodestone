package team.lodestar.lodestone.systems.network;

import net.minecraft.network.*;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import team.lodestar.lodestone.registry.common.LodestonePayloadRegistry;

public abstract class LodestoneNetworkPayloadData implements CustomPacketPayload {

    public final CustomPacketPayload.Type<? extends LodestoneNetworkPayloadData> dataType;

    public LodestoneNetworkPayloadData(FriendlyByteBuf byteBuf) {
        this.dataType = LodestonePayloadRegistry.CHANNEL_MAP.get(byteBuf.readUtf()).getPayloadType(byteBuf.readUtf());
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return dataType;
    }

    public final ResourceLocation getPacketType() {
        return type().id();
    }

    public abstract void serialize(FriendlyByteBuf byteBuf);

    public static void serializePacketType(LodestoneNetworkPayloadData data, FriendlyByteBuf byteBuf) {
        var packetType = data.getPacketType();
        byteBuf.writeUtf(packetType.getNamespace());
        byteBuf.writeUtf(packetType.getPath());
    }
}