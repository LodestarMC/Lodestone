package com.sammy.ortus.network.interaction;

import com.sammy.ortus.events.RuntimeEvents;
import com.sammy.ortus.events.types.RightClickEmptyServer;
import com.sammy.ortus.systems.network.OrtusServerPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class RightClickEmptyPacket extends OrtusServerPacket {

    public void execute(Supplier<NetworkEvent.Context> context) {
        RightClickEmptyServer.onRightClickEmptyServer(context.get().getSender());
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, RightClickEmptyPacket.class, RightClickEmptyPacket::encode, RightClickEmptyPacket::decode, RightClickEmptyPacket::handle);
    }

    public static RightClickEmptyPacket decode(FriendlyByteBuf buf) {
        return new RightClickEmptyPacket();
    }
}