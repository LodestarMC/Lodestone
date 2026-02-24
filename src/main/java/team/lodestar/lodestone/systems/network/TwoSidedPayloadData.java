package team.lodestar.lodestone.systems.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class TwoSidedPayloadData extends LodestoneNetworkPayloadData {

    public abstract void handleClient(final IPayloadContext context);

    public abstract void handleServer(final IPayloadContext context);
}