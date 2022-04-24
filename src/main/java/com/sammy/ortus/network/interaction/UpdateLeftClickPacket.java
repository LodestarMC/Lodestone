package com.sammy.ortus.network.interaction;

import com.sammy.ortus.capability.PlayerDataCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class UpdateLeftClickPacket {

    private final boolean leftClickHeld;
    public UpdateLeftClickPacket(boolean rightClick) {
        this.leftClickHeld = rightClick;
    }

    public static UpdateLeftClickPacket decode(FriendlyByteBuf buf) {
        return new UpdateLeftClickPacket(buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(leftClickHeld);
    }

    public void execute(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> PlayerDataCapability.getCapability(context.get().getSender()).ifPresent(c -> c.leftClickHeld = leftClickHeld));
        context.get().setPacketHandled(true);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, UpdateLeftClickPacket.class, UpdateLeftClickPacket::encode, UpdateLeftClickPacket::decode, UpdateLeftClickPacket::execute);
    }
}