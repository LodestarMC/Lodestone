package team.lodestar.lodestone.systems.network;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class TwoSidedPayloadData extends LodestoneNetworkPayloadData {
    public TwoSidedPayloadData(ResourceLocation type) {
        super(type);
    }

    public abstract void handleClient(final IPayloadContext context);

    public abstract void handleServer(final IPayloadContext context);
}