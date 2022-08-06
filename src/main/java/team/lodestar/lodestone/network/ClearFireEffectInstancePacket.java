package team.lodestar.lodestone.network;

import team.lodestar.lodestone.handlers.FireEffectHandler;
import team.lodestar.lodestone.systems.network.LodestoneClientPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ClearFireEffectInstancePacket extends LodestoneClientPacket {

    private final int entityID;

    public ClearFireEffectInstancePacket(int entityID) {
        this.entityID = entityID;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityID);
    }

    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        FireEffectHandler.setCustomFireInstance(Minecraft.getInstance().level.getEntity(entityID), null);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, ClearFireEffectInstancePacket.class, ClearFireEffectInstancePacket::encode, ClearFireEffectInstancePacket::decode, ClearFireEffectInstancePacket::handle);
    }

    public static ClearFireEffectInstancePacket decode(FriendlyByteBuf buf) {
        return new ClearFireEffectInstancePacket(buf.readInt());
    }
}