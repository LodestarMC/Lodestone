package team.lodestar.lodestone.networkold.screenshake;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.network.LodestoneClientPacket;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

import java.util.function.Supplier;

public class ScreenshakePacket extends LodestoneClientPacket {
    public final int duration;
    public float intensity1, intensity2, intensity3;
    public Easing intensityCurveStartEasing = Easing.LINEAR, intensityCurveEndEasing = Easing.LINEAR;

    public ScreenshakePacket(int duration) {
        this.duration = duration;
    }

    public ScreenshakePacket setIntensity(float intensity) {
        return setIntensity(intensity, intensity);
    }

    public ScreenshakePacket setIntensity(float intensity1, float intensity2) {
        return setIntensity(intensity1, intensity2, intensity2);
    }

    public ScreenshakePacket setIntensity(float intensity1, float intensity2, float intensity3) {
        this.intensity1 = intensity1;
        this.intensity2 = intensity2;
        this.intensity3 = intensity3;
        return this;
    }

    public ScreenshakePacket setEasing(Easing easing) {
        this.intensityCurveStartEasing = easing;
        this.intensityCurveEndEasing = easing;
        return this;
    }

    public ScreenshakePacket setEasing(Easing intensityCurveStartEasing, Easing intensityCurveEndEasing) {
        this.intensityCurveStartEasing = intensityCurveStartEasing;
        this.intensityCurveEndEasing = intensityCurveEndEasing;
        return this;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(duration);
        buf.writeFloat(intensity1);
        buf.writeFloat(intensity2);
        buf.writeFloat(intensity3);
        buf.writeUtf(intensityCurveStartEasing.name);
        buf.writeUtf(intensityCurveEndEasing.name);
    }

    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        //TODO: fix this
        ScreenshakeHandler.addScreenshake(new ScreenshakeInstance(duration).setIntensity(intensity1, intensity2, intensity3).setEasing(intensityCurveStartEasing, intensityCurveEndEasing));
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, ScreenshakePacket.class, ScreenshakePacket::encode, ScreenshakePacket::decode, ScreenshakePacket::handle);
    }

    public static ScreenshakePacket decode(FriendlyByteBuf buf) {
        return new ScreenshakePacket(
                buf.readInt()
        ).setIntensity(
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat()
        ).setEasing(
                Easing.valueOf(buf.readUtf()),
                Easing.valueOf(buf.readUtf())
        );
    }
}