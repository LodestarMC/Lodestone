package team.lodestar.lodestone.network.screenshake;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.helpers.ReflectionHelper;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

public class ScreenshakePayload extends OneSidedPayloadData {

    public int duration;
    public float intensity1, intensity2, intensity3;
    public Easing intensityCurveStartEasing, intensityCurveEndEasing;

    public static final Codec<ScreenshakePayload> CODEC = RecordCodecBuilder.create(obj -> obj.group(
            Codec.INT.fieldOf("duration").forGetter(p -> p.duration),
            Codec.FLOAT.fieldOf("intensity1").forGetter(p -> p.intensity1),
            Codec.FLOAT.fieldOf("intensity2").forGetter(p -> p.intensity2),
            Codec.FLOAT.fieldOf("intensity3").forGetter(p -> p.intensity3),
            Easing.CODEC.fieldOf("intensityCurveStartEasing").forGetter(p -> p.intensityCurveStartEasing),
            Easing.CODEC.fieldOf("intensityCurveEndEasing").forGetter(p -> p.intensityCurveEndEasing)
    ).apply(obj, ScreenshakePayload::new));

    public static final StreamCodec<ByteBuf, ScreenshakePayload> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public ScreenshakePayload(FriendlyByteBuf byteBuf) {
        ReflectionHelper.copyFields(this, STREAM_CODEC.decode(byteBuf));
    }

    public ScreenshakePayload(int duration, float intensity1, float intensity2, float intensity3, Easing intensityCurveStartEasing, Easing intensityCurveEndEasing) {
        this.duration = duration;
        this.intensity1 = intensity1;
        this.intensity2 = intensity2;
        this.intensity3 = intensity3;
        this.intensityCurveStartEasing = intensityCurveStartEasing;
        this.intensityCurveEndEasing = intensityCurveEndEasing;
    }

    public ScreenshakePayload(int duration, float intensity1, float intensity2, float intensity3, Easing intensityCurveStartEasing, Easing intensityCurveEndEasing) {
        this.duration = duration;
        this.intensity1 = intensity1;
        this.intensity2 = intensity2;
        this.intensity3 = intensity3;
        this.intensityCurveStartEasing = intensityCurveStartEasing;
        this.intensityCurveEndEasing = intensityCurveEndEasing;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(IPayloadContext context) {
        ScreenshakeHandler.addScreenshake(new ScreenshakeInstance(duration).setIntensity(intensity1, intensity2, intensity3).setEasing(intensityCurveStartEasing, intensityCurveEndEasing));
    }

    @Override
    public void serialize(FriendlyByteBuf byteBuf) {
        STREAM_CODEC.encode(byteBuf, this);
    }
}
