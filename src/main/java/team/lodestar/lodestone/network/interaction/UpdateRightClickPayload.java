package team.lodestar.lodestone.network.interaction;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.events.types.RightClickEmptyServer;
import team.lodestar.lodestone.registry.common.LodestoneAttachmentTypes;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;

public class UpdateRightClickPayload extends OneSidedPayloadData {

    private boolean rightClickHeld;

    public UpdateRightClickPayload(FriendlyByteBuf byteBuf) {
        super(byteBuf);
        rightClickHeld = byteBuf.readBoolean();

    }

    @Override
    public void handle(IPayloadContext context) {
        if (rightClickHeld) {
            RightClickEmptyServer.onRightClickEmptyServer(context.player());
        }

        var data = context.player().getData(LodestoneAttachmentTypes.PLAYER_DATA);
        data.rightClickHeld = rightClickHeld;
        context.player().setData(LodestoneAttachmentTypes.PLAYER_DATA, data);
    }

    @Override
    public void serialize(FriendlyByteBuf byteBuf) {
        byteBuf.writeBoolean(rightClickHeld);
    }
}
