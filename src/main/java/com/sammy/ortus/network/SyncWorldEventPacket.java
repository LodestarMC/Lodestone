package com.sammy.ortus.network;

import com.sammy.ortus.handlers.WorldEventHandler;
import com.sammy.ortus.registry.worldevent.OrtusWorldEventTypes;
import com.sammy.ortus.systems.worldevent.WorldEventInstance;
import com.sammy.ortus.systems.worldevent.WorldEventType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class SyncWorldEventPacket {
    String type;
    public boolean start;
    public CompoundTag eventData;

    public SyncWorldEventPacket(String type, boolean start, CompoundTag eventData) {
        this.type = type;
        this.start = start;
        this.eventData = eventData;
    }

    public static SyncWorldEventPacket decode(FriendlyByteBuf buf) {
        return new SyncWorldEventPacket(buf.readUtf(), buf.readBoolean(), buf.readNbt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(type);
        buf.writeBoolean(start);
        buf.writeNbt(eventData);
    }

    public void execute(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ClientOnly.addWorldEvent(type, start, eventData);
            }
        });
        context.get().setPacketHandled(true);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, SyncWorldEventPacket.class, SyncWorldEventPacket::encode, SyncWorldEventPacket::decode, SyncWorldEventPacket::execute);
    }

    public static class ClientOnly {
        public static void addWorldEvent(String type, boolean start, CompoundTag eventData) {
            WorldEventType eventType = OrtusWorldEventTypes.EVENT_TYPES.get(type);
            ClientLevel level = Minecraft.getInstance().level;
            WorldEventInstance instance = WorldEventHandler.addWorldEvent(level, eventType.createInstance(eventData));
            if (start) {
                instance.start(level);
            }
        }
    }
}