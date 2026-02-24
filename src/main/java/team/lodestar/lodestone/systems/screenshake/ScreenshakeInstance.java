package team.lodestar.lodestone.systems.screenshake;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import io.netty.buffer.*;
import net.minecraft.client.*;
import net.minecraft.network.codec.*;
import net.minecraft.util.*;
import net.minecraft.world.phys.*;
import team.lodestar.lodestone.systems.easing.*;
import team.lodestar.lodestone.systems.easing.*;

import java.util.*;

public class ScreenshakeInstance {

    public record ScreenshakePositionData(Vec3 center, float falloffDistance, Easing falloffCurve) {
        //TODO: Add a way to have the falloff distance be defined as a starting, middle and ending value too so that the screenshake distance can change with time
        public static final Codec<ScreenshakePositionData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Vec3.CODEC.fieldOf("center").forGetter(data -> data.center),
                Codec.FLOAT.fieldOf("falloffDistance").forGetter(data -> data.falloffDistance),
                Easing.CODEC.fieldOf("falloffCurve").forGetter(data -> data.falloffCurve)
        ).apply(instance, ScreenshakePositionData::new));

        public static StreamCodec<ByteBuf, ScreenshakePositionData> STREAM_CODEC = ByteBufCodecs.fromCodec(ScreenshakePositionData.CODEC);
    }

    public static final Codec<ScreenshakeInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("duration").forGetter(data -> data.duration),
            Codec.FLOAT.fieldOf("startingStrength").forGetter(data -> data.startingStrength),
            Codec.FLOAT.fieldOf("middleStrength").forGetter(data -> data.middleStrength),
            Codec.FLOAT.fieldOf("endingStrength").forGetter(data -> data.endingStrength),
            Easing.CODEC.fieldOf("startingCurve").forGetter(data -> data.startingCurve),
            Easing.CODEC.fieldOf("endingCurve").forGetter(data -> data.endingCurve),
            Codec.FLOAT.fieldOf("coefficient").forGetter(data -> data.coefficient),
            ScreenshakePositionData.CODEC.optionalFieldOf("positionData").forGetter(data -> Optional.of(data.positionData))
    ).apply(instance, ScreenshakeInstance::new));

    public static StreamCodec<ByteBuf, ScreenshakeInstance> STREAM_CODEC = ByteBufCodecs.fromCodec(ScreenshakeInstance.CODEC);

    protected final int duration;
    protected final float startingStrength, middleStrength, endingStrength;
    protected final Easing startingCurve, endingCurve;
    protected final float coefficient;

    protected final ScreenshakePositionData positionData;
    protected int progress;

    protected boolean expired;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public ScreenshakeInstance(int duration, float startingStrength, float middleStrength, float endingStrength, Easing startingCurve, Easing endingCurve, float coefficient, Optional<ScreenshakePositionData> positionData) {
        this.duration = duration;
        this.startingStrength = startingStrength;
        this.middleStrength = middleStrength;
        this.endingStrength = endingStrength;
        this.startingCurve = startingCurve;
        this.endingCurve = endingCurve;
        this.coefficient = coefficient;
        this.positionData = positionData.orElse(null);
    }

    public void tick() {
        if (progress < duration) {
            progress++;
            if (progress == duration) {
                expired = true;
            }
        }
    }

    public float getStrength(Camera camera) {
        float strength = getStrength();
        if (positionData == null) {
            return strength;
        }
        Vec3 cameraPos = camera.getPosition();
        Vec3 center = positionData.center();
        float distance = (float) cameraPos.distanceTo(center);
        float falloffDistance = positionData.falloffDistance();
        if (distance > falloffDistance || falloffDistance == 0) {
            return 0;
        }
        float falloff = 1 - positionData.falloffCurve().clamped(distance / falloffDistance, 0, 1);
        return strength * falloff;
    }

    protected float getStrength() {
        if (expired) {
            return 0;
        }
        float percentage = (progress * coefficient) / (float) duration;
        if (isTrinary()) {
            if (percentage >= 0.5f) {
                float delta = endingCurve.clamped((percentage - 0.5f)*2, 0, 1);
                return Mth.lerp(delta, middleStrength, endingStrength);
            } else {
                float delta = startingCurve.clamped(percentage*2, 0, 1);
                return Mth.lerp(delta, startingStrength, middleStrength);
            }
        } else {
            float delta = startingCurve.clamped(percentage, 0, 1);
            return Mth.lerp(delta, startingStrength, middleStrength);
        }
    }

    public boolean isExpired() {
        return expired;
    }
    public boolean isTrinary() {
        return endingStrength != middleStrength;
    }
}