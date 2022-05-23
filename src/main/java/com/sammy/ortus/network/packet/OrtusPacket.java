package com.sammy.ortus.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Function;
import java.util.function.Supplier;

public class OrtusPacket {
    protected CompoundTag data;

    public OrtusPacket(CompoundTag tag) {
        data = tag;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(data);
    }


    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                OrtusPacket.ClientOnly.clientData(this,data,context);
            }
            else {
                serverExecute(context);
            }
        });
        context.get().setPacketHandled(true);
    }
    //Overwrite Methods that are called when a packet is recieved by a client or a server (note they do nothing in this class)
    public void serverExecute(Supplier<NetworkEvent.Context> context){}

    @OnlyIn(Dist.CLIENT)
    public void clientExecute(Supplier<NetworkEvent.Context> context){}

    public static <T extends OrtusPacket> void register(Class<T> type, Function<FriendlyByteBuf, T> decoder, SimpleChannel instance, int index) {
        instance.registerMessage(index, type, T::encode, decoder, T::handle);
    }

    public static class ClientOnly {
        public static void clientData(OrtusPacket packet,CompoundTag data, Supplier<NetworkEvent.Context> context) {
            packet.clientExecute(context);
        }
    }
}
