package team.lodestar.lodestone.network.capability;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.systems.network.TwoSidedPayloadData;

public class SyncCapabilityPayload extends TwoSidedPayloadData {

    private int entityID;

    public SyncCapabilityPayload(FriendlyByteBuf byteBuf) {
        super(byteBuf);
        this.entityID = byteBuf.readInt();
    }

    @Override
    public void serialize(FriendlyByteBuf byteBuf) {
        byteBuf.writeInt(entityID);
    }

    @Override
    public void handleClient(IPayloadContext context) {
        Entity entity = context.player().level().getEntity(entityID);
        //TODO: deserialize capability

    }

    @Override
    public void handleServer(IPayloadContext context) {
        Entity entity = context.player().level().getEntity(entityID);
        //TODO: deserialize capability
    }
}
