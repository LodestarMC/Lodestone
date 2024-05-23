package team.lodestar.lodestone.network.interaction;

import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import team.lodestar.lodestone.events.LodestoneInteractionEvent;
import team.lodestar.lodestone.systems.network.LodestoneServerPacket;

public class RightClickEmptyPacket extends LodestoneServerPacket {

    public RightClickEmptyPacket(FriendlyByteBuf buf) {

    }

    @Override
    public void executeServer(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, PacketSender responseSender, SimpleChannel channel) {
        LodestoneInteractionEvent.RIGHT_CLICK_EMPTY.invoker().onRightClickEmpty(player);
    }
}