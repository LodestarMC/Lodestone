package team.lodestar.lodestone.network.interaction;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.registry.common.LodestoneAttachmentTypes;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;

public class UpdateLeftClickPayload extends OneSidedPayloadData {

    private final boolean leftClickHeld;

    public UpdateLeftClickPayload(FriendlyByteBuf byteBuf) {
        super(byteBuf);
        leftClickHeld = byteBuf.readBoolean();
    }

    @Override
    public void serialize(FriendlyByteBuf byteBuf) {
        byteBuf.writeBoolean(leftClickHeld);
    }

    @Override
    public void handle(IPayloadContext context) {
        var data = context.player().getData(LodestoneAttachmentTypes.PLAYER_DATA);
        data.leftClickHeld = leftClickHeld;
        context.player().setData(LodestoneAttachmentTypes.PLAYER_DATA, data);
    }
}
