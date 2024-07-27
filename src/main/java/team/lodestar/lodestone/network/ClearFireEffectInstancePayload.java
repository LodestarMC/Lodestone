package team.lodestar.lodestone.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.handlers.FireEffectHandler;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;

public class ClearFireEffectInstancePayload extends OneSidedPayloadData {

    private int entityId;

    public ClearFireEffectInstancePayload(ResourceLocation type) {
        super(type);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(IPayloadContext context) {
        FireEffectHandler.setCustomFireInstance(Minecraft.getInstance().level.getEntity(entityId), null);
    }

    @Override
    public void deserialize(CompoundTag tag) {
        entityId = tag.getInt("entityId");
    }

    @Override
    public void serialize(CompoundTag tag) {
        tag.putInt("entityId", entityId);
    }
}
