package team.lodestar.lodestone.core.network;

import net.minecraft.network.*;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public abstract class LodestoneNetworkPayloadData implements CustomPacketPayload {

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return LodestonePayloadRegistryHelper.PAYLOAD_TO_TYPE.get(getClass());
    }

    public abstract void serialize(RegistryFriendlyByteBuf byteBuf);
}