package team.lodestar.lodestone.network.interaction;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;

public class ResetRightClickDelayPayload extends OneSidedPayloadData {
    public ResetRightClickDelayPayload(ResourceLocation type) {
        super(type);
    }

    @Override
    public void handle(IPayloadContext context) {
        Minecraft.getInstance().rightClickDelay = 0;
    }

    @Override
    public void deserialize(CompoundTag tag) {

    }

    @Override
    public void serialize(CompoundTag tag) {

    }
}
