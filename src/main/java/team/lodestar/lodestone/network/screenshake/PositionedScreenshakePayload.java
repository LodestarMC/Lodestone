package team.lodestar.lodestone.network.screenshake;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.screenshake.PositionedScreenshakeInstance;

public class PositionedScreenshakePayload extends ScreenshakePayload {

    public final Vec3 position;
    public final float falloffDistance;
    public final float maxDistance;
    public final Easing falloffEasing;

    public PositionedScreenshakePayload(FriendlyByteBuf byteBuf) {
        super(byteBuf);
        position = new Vec3(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
        falloffDistance = byteBuf.readFloat();
        maxDistance = byteBuf.readFloat();
        falloffEasing = Easing.valueOf(byteBuf.readUtf());
    }

    public PositionedScreenshakePayload(int duration, float intensity1, float intensity2, float intensity3, Easing intensityCurveStartEasing, Easing intensityCurveEndEasing, Vec3 position, float falloffDistance, float maxDistance, Easing falloffEasing) {
        super(duration, intensity1, intensity2, intensity3, intensityCurveStartEasing, intensityCurveEndEasing);
        this.position = position;
        this.falloffDistance = falloffDistance;
        this.maxDistance = maxDistance;
        this.falloffEasing = falloffEasing;
    }


    @Override
    public void handle(IPayloadContext context) {
        ScreenshakeHandler.addScreenshake(new PositionedScreenshakeInstance(duration, position, falloffDistance, maxDistance, falloffEasing).setIntensity(intensity1, intensity2, intensity3).setEasing(intensityCurveStartEasing, intensityCurveEndEasing));
    }

    @Override
    public void serialize(FriendlyByteBuf byteBuf) {
        super.serialize(byteBuf);
        byteBuf.writeDouble(position.x);
        byteBuf.writeDouble(position.y);
        byteBuf.writeDouble(position.z);
        byteBuf.writeFloat(falloffDistance);
        byteBuf.writeFloat(maxDistance);
        byteBuf.writeUtf(falloffEasing.name);
    }
}
