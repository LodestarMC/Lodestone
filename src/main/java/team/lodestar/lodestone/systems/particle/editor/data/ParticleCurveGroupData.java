package team.lodestar.lodestone.systems.particle.editor.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import team.lodestar.lodestone.modules.core.easing.Easing;

public class ParticleCurveGroupData {
    public static final Codec<ParticleCurveGroupData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("enabled", true).forGetter(data -> data.enabled),
            ParticleCurveData.CODEC.fieldOf("curve").forGetter(data -> data.curve)
    ).apply(instance, ParticleCurveGroupData::new));

    public boolean enabled;
    public ParticleCurveData curve;

    public ParticleCurveGroupData(boolean enabled, ParticleCurveData curve) {
        this.enabled = enabled;
        this.curve = curve;
    }

    public static ParticleCurveGroupData createDefault(float value) {
        ParticleCurveData curve = new ParticleCurveData(value, value, value, false);
        curve.linked = true;
        curve.setLinkedValue(value);
        curve.startEasing = Easing.SINE_IN_OUT;
        return new ParticleCurveGroupData(true, curve);
    }
}
