package team.lodestar.lodestone.network.screenshake;

import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.screenshake.PositionedScreenshakeInstance;

import java.util.function.Supplier;

public class PositionedScreenshakePacket extends ScreenshakePacket {
    public final Vec3 position;
    public final float falloffDistance;
    public final float maxDistance;
    public final Easing falloffEasing;

    public PositionedScreenshakePacket(FriendlyByteBuf buf) {
        super(buf.readInt());
        //TODO: this is messy, but oh well
        position = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        falloffDistance = buf.readFloat();
        maxDistance = buf.readFloat();
        falloffEasing = Easing.valueOf(buf.readUtf());
    }

    public PositionedScreenshakePacket(int duration, Vec3 position, float falloffDistance, float maxDistance, Easing falloffEasing) {
        super(duration);
        this.position = position;
        this.falloffDistance = falloffDistance;
        this.maxDistance = maxDistance;
        this.falloffEasing = falloffEasing;
    }

    public PositionedScreenshakePacket(int duration, Vec3 position, float falloffDistance, float maxDistance) {
        this(duration, position, falloffDistance, maxDistance, Easing.LINEAR);
    }

    @Override
    public void executeClient(Minecraft client, ClientPacketListener listener, PacketSender responseSender, SimpleChannel channel) {
        ScreenshakeHandler.addScreenshake(new PositionedScreenshakeInstance(duration, position, falloffDistance, maxDistance, falloffEasing).setIntensity(intensity1, intensity2, intensity3).setEasing(intensityCurveStartEasing, intensityCurveEndEasing));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(duration);
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeFloat(falloffDistance);
        buf.writeFloat(maxDistance);
        buf.writeUtf(falloffEasing.name);
        buf.writeFloat(intensity1);
        buf.writeFloat(intensity2);
        buf.writeFloat(intensity3);
        buf.writeUtf(intensityCurveStartEasing.name);
        buf.writeUtf(intensityCurveEndEasing.name);
    }


}