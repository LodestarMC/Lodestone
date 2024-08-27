package team.lodestar.lodestone.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.handlers.FireEffectHandler;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;

public class ClearFireEffectInstancePayload extends OneSidedPayloadData {

    private final int entityId;

    public ClearFireEffectInstancePayload(FriendlyByteBuf byteBuf) {
        super(byteBuf);
        entityId = byteBuf.readInt();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(IPayloadContext context) {
        if (Minecraft.getInstance().level != null) {
            FireEffectHandler.setCustomFireInstance(Minecraft.getInstance().level.getEntity(entityId), null);
        }
    }

    @Override
    public void serialize(FriendlyByteBuf byteBuf) {
        byteBuf.writeInt(entityId);
    }
}
