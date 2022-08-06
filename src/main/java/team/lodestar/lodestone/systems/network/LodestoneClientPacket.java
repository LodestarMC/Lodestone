package team.lodestar.lodestone.systems.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class LodestoneClientPacket {

    public void encode(FriendlyByteBuf buf) {
    }

    public final void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (context.get().getDirection().getReceptionSide().equals(LogicalSide.CLIENT)) {
                LodestoneClientPacket.ClientOnly.clientData(this, context);
            }
        });
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public void execute(Supplier<NetworkEvent.Context> context) {
    }

    public static class ClientOnly {
        public static void clientData(LodestoneClientPacket packet, Supplier<NetworkEvent.Context> context) {
            packet.execute(context);
        }
    }
}