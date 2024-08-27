package team.lodestar.lodestone.network.worldevent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.handlers.WorldEventHandler;
import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypeRegistry;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

public class SyncWorldEventPayload extends OneSidedPayloadData {

    final ResourceLocation type;
    final boolean start;
    final CompoundTag eventData;

    public SyncWorldEventPayload(FriendlyByteBuf byteBuf) {
        super(byteBuf);
        type = ResourceLocation.parse(byteBuf.readUtf());
        start = byteBuf.readBoolean();
        eventData = byteBuf.readNbt();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(IPayloadContext context) {
        WorldEventType eventType = LodestoneWorldEventTypeRegistry.WORLD_EVENT_TYPE_REGISTRY.get(type);
        ClientLevel level = Minecraft.getInstance().level;
        WorldEventHandler.addWorldEvent(level, start, eventType.createInstance(eventData));
    }

    @Override
    public void serialize(FriendlyByteBuf byteBuf) {
        byteBuf.writeUtf(type.toString());
        byteBuf.writeBoolean(start);
        byteBuf.writeNbt(eventData);
    }
}
