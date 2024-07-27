package team.lodestar.lodestone.network.interaction;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.capability.LodestonePlayerDataCapability;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;

public class UpdateLeftClickPayload extends OneSidedPayloadData {
    public UpdateLeftClickPayload(ResourceLocation type) {
        super(type);
    }

    @Override
    public void handle(IPayloadContext context) {
        LodestonePlayerDataCapability.getCapabilityOptional(context.player()).ifPresent(c -> c.leftClickHeld = leftClickHeld);
    }

    @Override
    public void deserialize(CompoundTag tag) {

    }

    @Override
    public void serialize(CompoundTag tag) {

    }
}
