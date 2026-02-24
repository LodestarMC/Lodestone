package team.lodestar.lodestone.systems.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class OneSidedPayloadData extends LodestoneNetworkPayloadData {

    public abstract void handle(final IPayloadContext context);
}