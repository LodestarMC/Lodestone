package team.lodestar.lodestone.network.interaction;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.events.types.RightClickEmptyServer;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;

public class RightClickEmptyPayload extends OneSidedPayloadData {

    public RightClickEmptyPayload(ResourceLocation type) {
        super(type);
    }

    @Override
    public void handle(IPayloadContext context) {
        RightClickEmptyServer.onRightClickEmptyServer(context.player());
    }

    @Override
    public void deserialize(CompoundTag tag) {

    }

    @Override
    public void serialize(CompoundTag tag) {

    }
}
