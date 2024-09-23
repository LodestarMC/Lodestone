package team.lodestar.lodestone.network.screenshake;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

public class ScreenshakePayload extends OneSidedPayloadData {

    public final int duration;
    public final float intensity1, intensity2, intensity3;
    public final Easing intensityCurveStartEasing, intensityCurveEndEasing;

    public ScreenshakePayload(FriendlyByteBuf byteBuf) {
        duration = byteBuf.readInt();
        intensity1 = byteBuf.readFloat();
        intensity2 = byteBuf.readFloat();
        intensity3 = byteBuf.readFloat();
        intensityCurveStartEasing = Easing.valueOf(byteBuf.readUtf());
        intensityCurveEndEasing = Easing.valueOf(byteBuf.readUtf());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(IPayloadContext context) {
        ScreenshakeHandler.addScreenshake(new ScreenshakeInstance(duration).setIntensity(intensity1, intensity2, intensity3).setEasing(intensityCurveStartEasing, intensityCurveEndEasing));
    }

    @Override
    public void serialize(FriendlyByteBuf byteBuf) {
        byteBuf.writeInt(duration);
        byteBuf.writeFloat(intensity1);
        byteBuf.writeFloat(intensity2);
        byteBuf.writeFloat(intensity3);
        byteBuf.writeUtf(intensityCurveStartEasing.name);
        byteBuf.writeUtf(intensityCurveEndEasing.name);
    }
}
