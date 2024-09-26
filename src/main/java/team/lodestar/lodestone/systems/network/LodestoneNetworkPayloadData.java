package team.lodestar.lodestone.systems.network;

import net.minecraft.network.*;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import team.lodestar.lodestone.registry.common.LodestoneNetworkPayloads;

public abstract class LodestoneNetworkPayloadData implements CustomPacketPayload {

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return LodestoneNetworkPayloads.PayloadRegistryHelper.PAYLOAD_TO_TYPE.get(getClass());
    }

    public abstract void serialize(FriendlyByteBuf byteBuf);
}