package team.lodestar.lodestone.network.worldevent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.handlers.WorldEventHandler;
import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypeRegistry;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

public class SyncWorldEventPayload extends OneSidedPayloadData {

    ResourceLocation type;
    public boolean start;
    public CompoundTag eventData;

    public SyncWorldEventPayload(ResourceLocation type) {
        super(type);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(IPayloadContext context) {
        WorldEventType eventType = LodestoneWorldEventTypeRegistry.WORLD_EVENT_TYPE_REGISTRY.get(type);
        ClientLevel level = Minecraft.getInstance().level;
        WorldEventHandler.addWorldEvent(level, start, eventType.createInstance(eventData));
    }

    @Override
    public void deserialize(CompoundTag tag) {
        type = tag.getString(); //TODO
        start = tag.getBoolean("start");
        eventData = tag.getCompound("eventData");
    }

    @Override
    public void serialize(CompoundTag tag) {
        tag.putString();//TODO
        tag.putBoolean("start", start);
        tag.put("eventData", eventData);
    }
}
