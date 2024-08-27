package team.lodestar.lodestone.network.interaction;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;

public class ResetRightClickDelayPayload extends OneSidedPayloadData {

    public ResetRightClickDelayPayload(FriendlyByteBuf byteBuf) {
        super(byteBuf);
    }

    @Override
    public void handle(IPayloadContext context) {
        Minecraft.getInstance().rightClickDelay = 0;
    }

    @Override
    public void serialize(FriendlyByteBuf tag) {

    }
}
