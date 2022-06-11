package com.sammy.ortus.network.screenshake;

import com.sammy.ortus.handlers.ScreenshakeHandler;
import com.sammy.ortus.systems.network.OrtusClientPacket;
import com.sammy.ortus.systems.screenshake.ScreenshakeInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ScreenshakePacket extends OrtusClientPacket {
    float intensity;
    float falloffTransformSpeed;
    int timeBeforeFastFalloff;
    float slowFalloff;
    float fastFalloff;

    public ScreenshakePacket(float intensity, float falloffTransformSpeed, int timeBeforeFastFalloff, float slowFalloff, float fastFalloff) {
        this.intensity = intensity;
        this.falloffTransformSpeed = falloffTransformSpeed;
        this.timeBeforeFastFalloff = timeBeforeFastFalloff;
        this.slowFalloff = slowFalloff;
        this.fastFalloff = fastFalloff;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeFloat(intensity);
        buf.writeFloat(falloffTransformSpeed);
        buf.writeInt(timeBeforeFastFalloff);
        buf.writeFloat(slowFalloff);
        buf.writeFloat(fastFalloff);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        ScreenshakeHandler.addScreenshake(new ScreenshakeInstance(intensity, falloffTransformSpeed, timeBeforeFastFalloff, slowFalloff, fastFalloff));
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, ScreenshakePacket.class, ScreenshakePacket::encode, ScreenshakePacket::decode, ScreenshakePacket::handle);
    }

    public static ScreenshakePacket decode(FriendlyByteBuf buf) {
        return new ScreenshakePacket(buf.readFloat(), buf.readFloat(), buf.readInt(), buf.readFloat(), buf.readFloat());
    }
}