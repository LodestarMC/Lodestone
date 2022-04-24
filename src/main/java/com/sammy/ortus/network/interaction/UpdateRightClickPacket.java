package com.sammy.ortus.network.interaction;

import com.sammy.ortus.capability.PlayerDataCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class UpdateRightClickPacket {

    private final boolean rightClickHeld;
    public UpdateRightClickPacket(boolean rightClick) {
        this.rightClickHeld = rightClick;
    }

    public static UpdateRightClickPacket decode(FriendlyByteBuf buf) {
        return new UpdateRightClickPacket(buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(rightClickHeld);
    }

    public void execute(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> PlayerDataCapability.getCapability(context.get().getSender()).ifPresent(c -> c.rightClickHeld = rightClickHeld));
        context.get().setPacketHandled(true);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, UpdateRightClickPacket.class, UpdateRightClickPacket::encode, UpdateRightClickPacket::decode, UpdateRightClickPacket::execute);
    }
}