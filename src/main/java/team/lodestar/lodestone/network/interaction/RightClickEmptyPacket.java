package team.lodestar.lodestone.network.interaction;

import io.github.fabricators_of_create.porting_lib.entity.events.PlayerEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.PlayerInteractionEvents;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import team.lodestar.lodestone.events.LodestoneInteractionEvent;
import team.lodestar.lodestone.systems.network.LodestoneServerPacket;

import java.util.function.Supplier;

public class RightClickEmptyPacket extends LodestoneServerPacket {

    @Override
    public void executeServer(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, PacketSender responseSender, SimpleChannel channel) {
        LodestoneInteractionEvent.RIGHT_CLICK_EMPTY.invoker().onRightClickEmpty(player);
    }

    public static RightClickEmptyPacket decode(FriendlyByteBuf buf) {
        return new RightClickEmptyPacket();
    }
}