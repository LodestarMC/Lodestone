package team.lodestar.lodestone.systems.network;

import net.minecraft.network.FriendlyByteBuf;

public abstract class TwoSidedPayloadData extends LodestoneNetworkPayloadData {

    public abstract void handleClient(final IPayloadContext context);

    public abstract void handleServer(final IPayloadContext context);
}