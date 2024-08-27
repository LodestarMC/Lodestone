package team.lodestar.lodestone.network.interaction;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.capability.LodestonePlayerDataCapability;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;

public class UpdateLeftClickPayload extends OneSidedPayloadData {

    public UpdateLeftClickPayload(FriendlyByteBuf byteBuf) {
        super(byteBuf);
    }

    @Override
    public void serialize(FriendlyByteBuf byteBuf) {

    }

    @Override
    public void handle(IPayloadContext context) {
        LodestonePlayerDataCapability.getCapabilityOptional(context.player()).ifPresent(c -> c.leftClickHeld = leftClickHeld);
    }
}
