package team.lodestar.lodestone.network.worldevent;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.*;
import net.minecraft.resources.ResourceLocation;

import team.lodestar.lodestone.handlers.WorldEventHandler;
import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypes;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;
import team.lodestar.lodestone.systems.worldevent.*;

public class SyncWorldEventPayload extends OneSidedPayloadData {

    private final ResourceLocation type;
    private final boolean start;
    private final CompoundTag eventData;

    public SyncWorldEventPayload(FriendlyByteBuf byteBuf) {
        this(byteBuf.readResourceLocation(), byteBuf.readBoolean(), byteBuf.readNbt());
    }
    public SyncWorldEventPayload(WorldEventInstance instance, boolean start) {
        this(instance.type.id, start, instance.serializeNBT());
    }

    public SyncWorldEventPayload(ResourceLocation type, boolean start, CompoundTag eventData) {
        this.type = type;
        this.start = start;
        this.eventData = eventData;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void handle(IPayloadContext context) {
        WorldEventType eventType = LodestoneWorldEventTypes.WORLD_EVENT_TYPE_REGISTRY.get(type);
        ClientLevel level = Minecraft.getInstance().level;
        WorldEventHandler.addWorldEvent(level, start, eventType.createInstance(eventData));
    }

    @Override
    public void serialize(FriendlyByteBuf byteBuf) {
        byteBuf.writeResourceLocation(type);
        byteBuf.writeBoolean(start);
        byteBuf.writeNbt(eventData);
    }
}
