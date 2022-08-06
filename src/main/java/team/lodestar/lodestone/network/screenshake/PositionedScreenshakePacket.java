package team.lodestar.lodestone.network.screenshake;

import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.screenshake.PositionedScreenshakeInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class PositionedScreenshakePacket extends ScreenshakePacket {
    public final Vec3 position;
    public final float falloffDistance;
    public final float maxDistance;
    public final Easing falloffEasing;

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
    public void execute(Supplier<NetworkEvent.Context> context) {
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

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, PositionedScreenshakePacket.class, PositionedScreenshakePacket::encode, PositionedScreenshakePacket::decode, PositionedScreenshakePacket::handle);
    }

    public static PositionedScreenshakePacket decode(FriendlyByteBuf buf) {
        //TODO: this is messy, but oh well
        return ((PositionedScreenshakePacket)new PositionedScreenshakePacket(
                buf.readInt(),
                new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
                buf.readFloat(),
                buf.readFloat(),
                Easing.valueOf(buf.readUtf())
        ).setIntensity(
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat()
        ).setEasing(
                Easing.valueOf(buf.readUtf()),
                Easing.valueOf(buf.readUtf())
        ));
    }
}