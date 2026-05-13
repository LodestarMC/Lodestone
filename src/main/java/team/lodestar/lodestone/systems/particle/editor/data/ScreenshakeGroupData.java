package team.lodestar.lodestone.systems.particle.editor.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import team.lodestar.lodestone.modules.core.easing.Easing;

public class ScreenshakeGroupData {
    public static final Codec<ScreenshakeGroupData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("enabled", false).forGetter(data -> data.enabled),
            Codec.BOOL.optionalFieldOf("onBurst", false).forGetter(data -> data.onBurst),
            Codec.BOOL.optionalFieldOf("positioned", false).forGetter(data -> data.positioned),
            Codec.INT.optionalFieldOf("duration", 20).forGetter(data -> data.duration),
            Codec.FLOAT.optionalFieldOf("startStrength", 0.25f).forGetter(data -> data.startStrength),
            Codec.FLOAT.optionalFieldOf("middleStrength", 0.0f).forGetter(data -> data.middleStrength),
            Codec.FLOAT.optionalFieldOf("endStrength", 0.0f).forGetter(data -> data.endStrength),
            Easing.CODEC.optionalFieldOf("startEasing", Easing.SINE_OUT).forGetter(data -> data.startEasing),
            Easing.CODEC.optionalFieldOf("endEasing", Easing.SINE_IN).forGetter(data -> data.endEasing),
            Codec.FLOAT.optionalFieldOf("coefficient", 1.0f).forGetter(data -> data.coefficient),
            Codec.FLOAT.optionalFieldOf("falloffDistance", 16.0f).forGetter(data -> data.falloffDistance),
            Easing.CODEC.optionalFieldOf("falloffEasing", Easing.SINE_IN_OUT).forGetter(data -> data.falloffEasing)
    ).apply(instance, ScreenshakeGroupData::new));

    public boolean enabled;
    public boolean onBurst;
    public boolean positioned;
    public int duration;
    public float startStrength;
    public float middleStrength;
    public float endStrength;
    public Easing startEasing;
    public Easing endEasing;
    public float coefficient;
    public float falloffDistance;
    public Easing falloffEasing;

    public ScreenshakeGroupData(boolean enabled, boolean onBurst, boolean positioned, int duration, float startStrength, float middleStrength, float endStrength, Easing startEasing, Easing endEasing, float coefficient, float falloffDistance, Easing falloffEasing) {
        this.enabled = enabled;
        this.onBurst = onBurst;
        this.positioned = positioned;
        this.duration = duration;
        this.startStrength = startStrength;
        this.middleStrength = middleStrength;
        this.endStrength = endStrength;
        this.startEasing = startEasing != null ? startEasing : Easing.SINE_OUT;
        this.endEasing = endEasing != null ? endEasing : Easing.SINE_IN;
        this.coefficient = coefficient;
        this.falloffDistance = falloffDistance;
        this.falloffEasing = falloffEasing != null ? falloffEasing : Easing.SINE_IN_OUT;
    }

    public static ScreenshakeGroupData createDefault() {
        return new ScreenshakeGroupData(false, false, false, 20, 0.25f, 0.0f, 0.0f, Easing.SINE_OUT, Easing.SINE_IN, 1.0f, 16.0f, Easing.SINE_IN_OUT);
    }
}
