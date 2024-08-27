package team.lodestar.lodestone.network.interaction;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.capability.LodestonePlayerDataCapability;
import team.lodestar.lodestone.events.types.RightClickEmptyServer;
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
        LodestonePlayerDataCapability.getCapabilityOptional(context.player()).ifPresent(c -> c.rightClickHeld = rightClickHeld);
    }

    @Override
    public void serialize(FriendlyByteBuf byteBuf) {
        byteBuf.writeBoolean(rightClickHeld);
    }
}
