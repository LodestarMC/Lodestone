package team.lodestar.lodestone.systems.particle.editor.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import team.lodestar.lodestone.modules.core.easing.Easing;

public class TransparencyGroupData {
    public static final Codec<TransparencyGroupData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("enabled", true).forGetter(data -> data.enabled),
            ParticleCurveData.CODEC.fieldOf("curve").forGetter(data -> data.curve)
    ).apply(instance, TransparencyGroupData::new));

    public boolean enabled;
    public ParticleCurveData curve;

    public TransparencyGroupData(boolean enabled, ParticleCurveData curve) {
        this.enabled = enabled;
        this.curve = curve;
    }

    public static TransparencyGroupData createDefault() {
        ParticleCurveData curve = new ParticleCurveData(0.9f, 0.0f, 0.0f, false);
        curve.startEasing = Easing.SINE_IN;
        return new TransparencyGroupData(true, curve);
    }
}
