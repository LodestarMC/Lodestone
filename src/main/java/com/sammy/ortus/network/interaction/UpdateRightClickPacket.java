package com.sammy.ortus.network.interaction;

import com.sammy.ortus.capability.OrtusPlayerDataCapability;
import com.sammy.ortus.events.types.RightClickEmptyServer;
import com.sammy.ortus.systems.network.OrtusServerPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class UpdateRightClickPacket extends OrtusServerPacket {

    private final boolean rightClickHeld;

    public UpdateRightClickPacket(boolean rightClick) {
        this.rightClickHeld = rightClick;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(rightClickHeld);
    }

    public void execute(Supplier<NetworkEvent.Context> context) {
        if (rightClickHeld) {
            RightClickEmptyServer.onRightClickEmptyServer(context.get().getSender());
        }
        OrtusPlayerDataCapability.getCapabilityOptional(context.get().getSender()).ifPresent(c -> c.rightClickHeld = rightClickHeld);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, UpdateRightClickPacket.class, UpdateRightClickPacket::encode, UpdateRightClickPacket::decode, UpdateRightClickPacket::handle);
    }

    public static UpdateRightClickPacket decode(FriendlyByteBuf buf) {
        return new UpdateRightClickPacket(buf.readBoolean());
    }
}