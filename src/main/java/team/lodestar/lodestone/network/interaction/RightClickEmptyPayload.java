package team.lodestar.lodestone.network.interaction;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.events.types.RightClickEmptyServer;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;

public class RightClickEmptyPayload extends OneSidedPayloadData {

    public RightClickEmptyPayload(FriendlyByteBuf byteBuf) {
        super(byteBuf);
    }

    @Override
    public void handle(IPayloadContext context) {
        RightClickEmptyServer.onRightClickEmptyServer(context.player());
    }


    @Override
    public void serialize(FriendlyByteBuf byteBuf) {

    }
}
