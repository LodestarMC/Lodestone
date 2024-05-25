package team.lodestar.lodestone.network.worldevent;

import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.handlers.WorldEventHandler;
import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypeRegistry;
import team.lodestar.lodestone.systems.network.LodestoneClientPacket;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

public class SyncWorldEventPacket extends LodestoneClientPacket {
    ResourceLocation type;
    public boolean start;
    public CompoundTag eventData;

    public SyncWorldEventPacket(ResourceLocation type, boolean start, CompoundTag eventData) {
        this.type = type;
        this.start = start;
        this.eventData = eventData;
    }

    public SyncWorldEventPacket(FriendlyByteBuf buf) {
        type = buf.readResourceLocation();
        start = buf.readBoolean();
        eventData = new CompoundTag();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(type);
        buf.writeBoolean(start);
        buf.writeNbt(eventData);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void executeClient(Minecraft client, ClientPacketListener listener, PacketSender responseSender, SimpleChannel channel) {
        WorldEventType eventType = LodestoneWorldEventTypeRegistry.EVENT_TYPES.get(type);
        ClientLevel level = Minecraft.getInstance().level;
        WorldEventHandler.addWorldEvent(level, start, eventType.createInstance(eventData));
    }
}