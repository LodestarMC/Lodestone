package team.lodestar.lodestone.systems.network;
/*
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class LodestoneTwoWayPacket {

    public void encode(FriendlyByteBuf buf) {
    }

    public final void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (context.get().getDirection().getReceptionSide().equals(LogicalSide.CLIENT)) {
                LodestoneTwoWayPacket.ClientOnly.clientData(this, context);
            } else {
                serverExecute(context);
            }
        });
        context.get().setPacketHandled(true);
    }

    //Overwrite Methods that are called when a packet is received by a client or a server (note they do nothing in this class)
    public void serverExecute(Supplier<NetworkEvent.Context> context) {
    }

    @OnlyIn(Dist.CLIENT)
    public void clientExecute(Supplier<NetworkEvent.Context> context) {
    }

    public static class ClientOnly {
        public static void clientData(LodestoneTwoWayPacket packet, Supplier<NetworkEvent.Context> context) {
            packet.clientExecute(context);
        }
    }
}

 */