package com.sammy.ortus.network.interaction;

import com.sammy.ortus.events.RuntimeEvents;
import com.sammy.ortus.events.types.RightClickEmptyServer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class RightClickEmptyPacket {


    public RightClickEmptyPacket() {
    }

    public static RightClickEmptyPacket decode(FriendlyByteBuf buf) {
        return new RightClickEmptyPacket();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void execute(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> RightClickEmptyServer.onRightClickEmptyServer(context.get().getSender()));
        context.get().setPacketHandled(true);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, RightClickEmptyPacket.class, RightClickEmptyPacket::encode, RightClickEmptyPacket::decode, RightClickEmptyPacket::execute);
    }
}