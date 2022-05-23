package com.sammy.ortus.network;

import com.sammy.ortus.network.packet.OrtusPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Function;

public interface IPacketRegisterable {
    static <T extends OrtusPacket> void register(Class<T> type, Function<FriendlyByteBuf, T> decoder, SimpleChannel instance, int index) {
        instance.registerMessage(index, type, T::encode, decoder, T::handle);
    }
}
