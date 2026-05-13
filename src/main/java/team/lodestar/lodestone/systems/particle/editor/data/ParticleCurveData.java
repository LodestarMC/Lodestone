package team.lodestar.lodestone.systems.particle.editor.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import team.lodestar.lodestone.modules.core.easing.Easing;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.GenericParticleDataBuilder;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleDataBuilder;
import team.lodestar.lodestone.systems.particle.editor.EditorState;

public class ParticleCurveData {
    public static final Codec<ParticleCurveData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("start").forGetter(curve -> curve.start),
            Codec.FLOAT.fieldOf("middle").forGetter(curve -> curve.middle),
            Codec.FLOAT.fieldOf("end").forGetter(curve -> curve.end),
            Codec.FLOAT.optionalFieldOf("coefficient", 1.0f).forGetter(curve -> curve.coefficient),
            Codec.BOOL.optionalFieldOf("trinary", false).forGetter(curve -> curve.trinary),
            Codec.BOOL.optionalFieldOf("linked", false).forGetter(curve -> curve.linked),
            Codec.BOOL.optionalFieldOf("endpointsLinked", false).forGetter(curve -> curve.endpointsLinked),
            Easing.CODEC.optionalFieldOf("easing", Easing.LINEAR).forGetter(curve -> curve.startEasing),
            Easing.CODEC.optionalFieldOf("endEasing", Easing.LINEAR).forGetter(curve -> curve.endEasing)
    ).apply(instance, ParticleCurveData::create));

    public float start;
    public float middle;
    public float end;
    public float coefficient = 1.0f;
    public boolean trinary;
    public boolean linked;
    public boolean endpointsLinked;
    public Easing startEasing = Easing.LINEAR;
    public Easing endEasing = Easing.LINEAR;

    public ParticleCurveData(float start, float middle, float end, boolean trinary) {
        this.start = start;
        this.middle = middle;
        this.end = end;
        this.trinary = trinary;
    }

    private static ParticleCurveData create(float start, float middle, float end, float coefficient, boolean trinary, boolean linked, boolean endpointsLinked, Easing startEasing, Easing endEasing) {
        ParticleCurveData curve = new ParticleCurveData(start, middle, end, trinary);
        curve.coefficient = coefficient;
        curve.linked = linked;
        curve.endpointsLinked = endpointsLinked;
        curve.startEasing = startEasing != null ? startEasing : Easing.LINEAR;
        curve.endEasing = endEasing != null ? endEasing : Easing.LINEAR;
        if (curve.endpointsLinked) {
            curve.setFinal(curve.start);
        }
        return curve;
    }

    public GenericParticleData buildGeneric() {
        GenericParticleDataBuilder builder = trinary
                ? GenericParticleData.create(start, middle, end)
                : GenericParticleData.create(start, middle);
        return builder.setCoefficient(coefficient).setEasing(startEasing, endEasing).build();
    }

    public void setLinkedValue(float value) {
        start = value;
        middle = value;
        end = value;
        trinary = false;
    }

    public void setStart(float value) {
        start = value;
        if (endpointsLinked) {
            setFinal(value);
        }
    }

    public void setFinal(float value) {
        if (trinary) {
            end = value;
        } else {
            middle = value;
        }
        if (endpointsLinked) {
            start = value;
        }
    }

    public float finalValue() {
        return trinary ? end : middle;
    }

    public void setEndpointsLinked(boolean linked) {
        endpointsLinked = linked;
        if (linked) {
            setFinal(start);
        }
    }

    public void setTrinary(boolean value) {
        trinary = value;
        if (endpointsLinked) {
            setFinal(start);
        }
    }

    public SpinParticleData buildSpin(float spinOffset) {
        SpinParticleDataBuilder builder = trinary
                ? SpinParticleData.create(start, middle, end)
                : SpinParticleData.create(start, middle);
        return builder.setSpinOffset(spinOffset).setCoefficient(coefficient).setEasing(startEasing, endEasing).build();
    }

    public String toJavaGeneric(String factory) {
        String create = trinary
                ? "%s.create(%sf, %sf, %sf)".formatted(factory, EditorState.formatNumber(start), EditorState.formatNumber(middle), EditorState.formatNumber(end))
                : "%s.create(%sf, %sf)".formatted(factory, EditorState.formatNumber(start), EditorState.formatNumber(middle));
        return create + ".setCoefficient(" + EditorState.formatNumber(coefficient) + "f).setEasing(Easing." + EditorState.easingConstant(startEasing) + ", Easing." + EditorState.easingConstant(endEasing) + ")";
    }
}
