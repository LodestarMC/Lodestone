package team.lodestar.lodestone.systems.particle.editor.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class SpinGroupData {
    public static final Codec<SpinGroupData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("enabled", true).forGetter(data -> data.enabled),
            Codec.FLOAT.optionalFieldOf("offset", 0.0f).forGetter(data -> data.offset),
            ParticleCurveData.CODEC.fieldOf("curve").forGetter(data -> data.curve)
    ).apply(instance, SpinGroupData::new));

    public boolean enabled;
    public float offset;
    public ParticleCurveData curve;

    public SpinGroupData(boolean enabled, float offset, ParticleCurveData curve) {
        this.enabled = enabled;
        this.offset = offset;
        this.curve = curve;
    }

    public static SpinGroupData createDefault() {
        return new SpinGroupData(true, 0.0f, new ParticleCurveData(0.0f, 0.0f, 0.0f, false));
    }
}
