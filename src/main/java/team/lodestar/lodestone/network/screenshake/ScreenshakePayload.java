package team.lodestar.lodestone.network.screenshake;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

public class ScreenshakePayload extends OneSidedPayloadData {

    public int duration;
    public float intensity1, intensity2, intensity3;
    public Easing intensityCurveStartEasing = Easing.LINEAR, intensityCurveEndEasing = Easing.LINEAR;

    public ScreenshakePayload(ResourceLocation type) {
        super(type);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(IPayloadContext context) {
        ScreenshakeHandler.addScreenshake(new ScreenshakeInstance(duration).setIntensity(intensity1, intensity2, intensity3).setEasing(intensityCurveStartEasing, intensityCurveEndEasing));
    }

    @Override
    public void deserialize(CompoundTag tag) {
        duration = tag.getInt("duration");
        intensity1 = tag.getFloat("intensity1");
        intensity2 = tag.getFloat("intensity2");
        intensity3 = tag.getFloat("intensity3");
        intensityCurveStartEasing = Easing.valueOf(tag.getString("intensityCurveStartEasing"));
        intensityCurveEndEasing = Easing.valueOf(tag.getString("intensityCurveEndEasing"));
    }

    @Override
    public void serialize(CompoundTag tag) {
        tag.putInt("duration", duration);
        tag.putFloat("intensity1", intensity1);
        tag.putFloat("intensity2", intensity2);
        tag.putFloat("intensity3", intensity3);
        tag.putString("intensityCurveStartEasing", intensityCurveStartEasing.name);
        tag.putString("intensityCurveEndEasing", intensityCurveEndEasing.name);
    }
}
