package team.lodestar.lodestone.systems.network;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class OneSidedPayloadData extends LodestoneNetworkPayloadData {
    public OneSidedPayloadData(ResourceLocation type) {
        super(type);
    }

    public abstract void handle(final IPayloadContext context);
}
