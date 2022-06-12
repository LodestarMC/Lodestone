package com.sammy.ortus.network.screenshake;

import com.sammy.ortus.handlers.ScreenshakeHandler;
import com.sammy.ortus.systems.network.OrtusClientPacket;
import com.sammy.ortus.systems.screenshake.PositionedScreenshakeInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class PositionedScreenshakePacket extends OrtusClientPacket {
    Vec3 position;
    public float falloffDistance;
    public float maxDistance;
    float intensity;
    float falloffTransformSpeed;
    int timeBeforeFastFalloff;
    float slowFalloff;
    float fastFalloff;

    public PositionedScreenshakePacket(Vec3 position, float falloffDistance, float maxDistance, float intensity, float falloffTransformSpeed, int timeBeforeFastFalloff, float slowFalloff, float fastFalloff) {
        this.position = position;
        this.falloffDistance = falloffDistance;
        this.maxDistance = maxDistance;
        this.intensity = intensity;
        this.falloffTransformSpeed = falloffTransformSpeed;
        this.timeBeforeFastFalloff = timeBeforeFastFalloff;
        this.slowFalloff = slowFalloff;
        this.fastFalloff = fastFalloff;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeFloat(falloffDistance);
        buf.writeFloat(maxDistance);
        buf.writeFloat(intensity);
        buf.writeFloat(falloffTransformSpeed);
        buf.writeInt(timeBeforeFastFalloff);
        buf.writeFloat(slowFalloff);
        buf.writeFloat(fastFalloff);
    }

    @OnlyIn(value = Dist.CLIENT)
    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        ScreenshakeHandler.addScreenshake(new PositionedScreenshakeInstance(position, falloffDistance, maxDistance, intensity, falloffTransformSpeed, timeBeforeFastFalloff, slowFalloff, fastFalloff));
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, PositionedScreenshakePacket.class, PositionedScreenshakePacket::encode, PositionedScreenshakePacket::decode, PositionedScreenshakePacket::handle);
    }

    public static PositionedScreenshakePacket decode(FriendlyByteBuf buf) {
        return new PositionedScreenshakePacket(new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readInt(), buf.readFloat(), buf.readFloat());
    }
}