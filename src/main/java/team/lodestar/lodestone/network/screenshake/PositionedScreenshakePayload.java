package team.lodestar.lodestone.network.screenshake;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.screenshake.PositionedScreenshakeInstance;

public class PositionedScreenshakePayload extends ScreenshakePayload {

    public Vec3 position;
    public float falloffDistance;
    public float maxDistance;
    public Easing falloffEasing;

    public PositionedScreenshakePayload(ResourceLocation type) {
        super(type);
    }

    @Override
    public void deserialize(CompoundTag tag) {
        super.deserialize(tag);
        position = new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
        falloffDistance = tag.getFloat("falloffDistance");
        maxDistance = tag.getFloat("maxDistance");
        falloffEasing = Easing.valueOf(tag.getString("falloffEasing"));
    }

    @Override
    public void handle(IPayloadContext context) {
        ScreenshakeHandler.addScreenshake(new PositionedScreenshakeInstance(duration, position, falloffDistance, maxDistance, falloffEasing).setIntensity(intensity1, intensity2, intensity3).setEasing(intensityCurveStartEasing, intensityCurveEndEasing));
    }

    @Override
    public void serialize(CompoundTag tag) {
        super.serialize(tag);
        tag.putDouble("x", position.x);
        tag.putDouble("y", position.y);
        tag.putDouble("z", position.z);
        tag.putFloat("falloffDistance", falloffDistance);
        tag.putFloat("maxDistance", maxDistance);
        tag.putString("falloffEasing", falloffEasing.name);
    }
}
