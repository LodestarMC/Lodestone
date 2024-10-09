package team.lodestar.lodestone.network.screenshake;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.helpers.ReflectionHelper;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.screenshake.PositionedScreenshakeInstance;

public class PositionedScreenshakePayload extends ScreenshakePayload {

    public Vec3 position;
    public float falloffDistance;
    public float maxDistance;
    public Easing falloffEasing;

    public static final Codec<PositionedScreenshakePayload> CODEC = RecordCodecBuilder.create(obj -> obj.group(
            ScreenshakePayload.CODEC.fieldOf("parent").forGetter(p -> p),
            Vec3.CODEC.fieldOf("position").forGetter(p -> p.position),
            Codec.FLOAT.fieldOf("falloffDistance").forGetter(p -> p.falloffDistance),
            Codec.FLOAT.fieldOf("maxDistance").forGetter(p -> p.maxDistance),
            Easing.CODEC.fieldOf("falloffEasing").forGetter(p -> p.falloffEasing)
    ).apply(obj, PositionedScreenshakePayload::new));

    public static final StreamCodec<ByteBuf, PositionedScreenshakePayload> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public PositionedScreenshakePayload(FriendlyByteBuf byteBuf) {
        super(byteBuf);
        ReflectionHelper.copyFields(this, STREAM_CODEC.decode(byteBuf));
    }

    public PositionedScreenshakePayload(ScreenshakePayload parent, Vec3 position, float falloffDistance, float maxDistance, Easing falloffEasing) {
        this(
                parent.duration, parent.intensity1, parent.intensity2, parent.intensity3,
                parent.intensityCurveStartEasing, parent.intensityCurveEndEasing,
                position, falloffDistance, maxDistance, falloffEasing
        );
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
        STREAM_CODEC.encode(byteBuf, this);
    }
}
