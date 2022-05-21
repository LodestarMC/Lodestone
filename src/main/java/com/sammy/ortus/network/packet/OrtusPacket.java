package com.sammy.ortus.network.packet;

import com.sammy.ortus.capability.EntityDataCapability;
import com.sammy.ortus.network.SyncEntityCapabilityDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class OrtusPacket {
    protected CompoundTag data;

    public OrtusPacket(CompoundTag tag) {
        data = tag;
    }

    public static OrtusPacket decode(FriendlyByteBuf buf) {
        return new OrtusPacket(buf.readNbt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(data);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                OrtusPacket.ClientOnly.syncData(data,context);
            }
            execute(data,context);
        });
        context.get().setPacketHandled(true);
    }
    public void execute(CompoundTag data,Supplier<NetworkEvent.Context> context){};
    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, OrtusPacket.class, OrtusPacket::encode, OrtusPacket::decode, OrtusPacket::handle);
    }

    public static class ClientOnly {
        public static void syncData(CompoundTag data,Supplier<NetworkEvent.Context> context) {
            Entity entity = context.get().getSender();
            if (entity != null) {
                EntityDataCapability.getCapability(entity).ifPresent(c -> c.deserializeNBT(data));
            }
        }
    }
}
