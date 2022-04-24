package com.sammy.ortus.network;

import com.sammy.ortus.capability.EntityDataCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class SyncEntityCapabilityDataPacket {
    private final int entityId;
    private final CompoundTag tag;

    public SyncEntityCapabilityDataPacket(int entityId, CompoundTag tag) {
        this.entityId = entityId;
        this.tag = tag;
    }

    public static SyncEntityCapabilityDataPacket decode(FriendlyByteBuf buf) {
        return new SyncEntityCapabilityDataPacket(buf.readInt(), buf.readNbt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeNbt(tag);
    }

    public void execute(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ClientOnly.syncData(entityId, tag);
            }
        });
        context.get().setPacketHandled(true);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, SyncEntityCapabilityDataPacket.class, SyncEntityCapabilityDataPacket::encode, SyncEntityCapabilityDataPacket::decode, SyncEntityCapabilityDataPacket::execute);
    }

    public static class ClientOnly {
        public static void syncData(int entityId, CompoundTag tag) {
            Entity entity = Minecraft.getInstance().level.getEntity(entityId);
            if (entity != null) {
                EntityDataCapability.getCapability(entity).ifPresent(c -> c.deserializeNBT(tag));
            }
        }
    }
}