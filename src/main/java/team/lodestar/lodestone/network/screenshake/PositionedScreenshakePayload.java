package team.lodestar.lodestone.network.screenshake;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
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
