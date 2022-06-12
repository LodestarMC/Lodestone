package com.sammy.ortus.network;

import com.sammy.ortus.handlers.FireEffectHandler;
import com.sammy.ortus.handlers.ScreenshakeHandler;
import com.sammy.ortus.network.screenshake.ScreenshakePacket;
import com.sammy.ortus.systems.network.OrtusClientPacket;
import com.sammy.ortus.systems.screenshake.ScreenshakeInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ClearFireEffectInstancePacket extends OrtusClientPacket {

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
        instance.registerMessage(index, ScreenshakePacket.class, ScreenshakePacket::encode, ScreenshakePacket::decode, ScreenshakePacket::handle);
    }

    public static ClearFireEffectInstancePacket decode(FriendlyByteBuf buf) {
        return new ClearFireEffectInstancePacket(buf.readInt());
    }
}