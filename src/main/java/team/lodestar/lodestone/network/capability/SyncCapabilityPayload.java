package team.lodestar.lodestone.network.capability;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.systems.network.TwoSidedPayloadData;

public class SyncCapabilityPayload extends TwoSidedPayloadData {

    private int entityID;

    public SyncCapabilityPayload(ResourceLocation type) {
        super(type);
    }

    @Override
    public void deserialize(CompoundTag tag) {
        this.entityID = tag.getInt("entityId");
    }

    @Override
    public void serialize(CompoundTag emptyTag) {
        emptyTag.putInt("entityId", entityID);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClient(IPayloadContext context) {
        Entity entity = Minecraft.getInstance().player.level().getEntity(entityID);
        //TODO: deserialize capability

    }

    @Override
    public void handleServer(IPayloadContext context) {
        Entity entity = context.player().level().getEntity(entityID);
        //TODO: deserialize capability
    }
}
