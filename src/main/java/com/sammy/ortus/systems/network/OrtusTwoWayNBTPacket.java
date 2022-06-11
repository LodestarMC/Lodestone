package com.sammy.ortus.systems.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class OrtusTwoWayNBTPacket extends OrtusTwoWayPacket {
    protected CompoundTag data;

    public OrtusTwoWayNBTPacket(CompoundTag data) {
        this.data = data;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(data);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (context.get().getDirection().getReceptionSide().equals(LogicalSide.CLIENT)) {
                OrtusTwoWayNBTPacket.ClientOnly.clientData(this, data, context);
            } else {
                serverExecute(context, data);
            }
        });
        context.get().setPacketHandled(true);
    }

    @Override
    public final void serverExecute(Supplier<NetworkEvent.Context> context) {
        serverExecute(context, data);
    }

    @Override
    public final void clientExecute(Supplier<NetworkEvent.Context> context) {
        clientExecute(context, data);
    }

    public void serverExecute(Supplier<NetworkEvent.Context> context, CompoundTag data) {
    }

    @OnlyIn(Dist.CLIENT)
    public void clientExecute(Supplier<NetworkEvent.Context> context, CompoundTag data) {
    }

    public static class ClientOnly {
        public static void clientData(OrtusTwoWayNBTPacket packet, CompoundTag data, Supplier<NetworkEvent.Context> context) {
            packet.clientExecute(context, data);
        }
    }
}