package team.lodestar.lodestone.network.interaction;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.systems.network.LodestoneClientPacket;

import java.util.function.Supplier;

public class ResetRightClickDelayPacket extends LodestoneClientPacket {

    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        Minecraft.getInstance().rightClickDelay = 0;
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, ResetRightClickDelayPacket.class, ResetRightClickDelayPacket::encode, ResetRightClickDelayPacket::decode, ResetRightClickDelayPacket::handle);
    }

    public static ResetRightClickDelayPacket decode(FriendlyByteBuf buf) {
        return new ResetRightClickDelayPacket();
    }
}