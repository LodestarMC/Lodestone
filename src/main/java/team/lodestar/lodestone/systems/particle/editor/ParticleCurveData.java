package team.lodestar.lodestone.systems.particle.editor;

import team.lodestar.lodestone.modules.core.easing.Easing;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.GenericParticleDataBuilder;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleDataBuilder;

class ParticleCurveData {
    float start;
    float middle;
    float end;
    float coefficient = 1.0f;
    boolean trinary;
    boolean linked;
    boolean endpointsLinked;
    Easing startEasing = Easing.LINEAR;
    Easing endEasing = Easing.LINEAR;

    ParticleCurveData(float start, float middle, float end, boolean trinary) {
        this.start = start;
        this.middle = middle;
        this.end = end;
        this.trinary = trinary;
    }

    GenericParticleData buildGeneric() {
        GenericParticleDataBuilder builder = trinary
                ? GenericParticleData.create(start, middle, end)
                : GenericParticleData.create(start, middle);
        return builder.setCoefficient(coefficient).setEasing(startEasing, endEasing).build();
    }

    void setLinkedValue(float value) {
        start = value;
        middle = value;
        end = value;
        trinary = false;
    }

    void setStart(float value) {
        start = value;
        if (endpointsLinked) {
            setFinal(value);
        }
    }

    void setFinal(float value) {
        if (trinary) {
            end = value;
        } else {
            middle = value;
        }
        if (endpointsLinked) {
            start = value;
        }
    }

    float finalValue() {
        return trinary ? end : middle;
    }

    void setEndpointsLinked(boolean linked) {
        endpointsLinked = linked;
        if (linked) {
            setFinal(start);
        }
    }

    void setTrinary(boolean value) {
        trinary = value;
        if (endpointsLinked) {
            setFinal(start);
        }
    }

    SpinParticleData buildSpin(float spinOffset) {
        SpinParticleDataBuilder builder = trinary
                ? SpinParticleData.create(start, middle, end)
                : SpinParticleData.create(start, middle);
        return builder.setSpinOffset(spinOffset).setCoefficient(coefficient).setEasing(startEasing, endEasing).build();
    }

    String toJavaGeneric(String factory) {
        String create = trinary
                ? "%s.create(%sf, %sf, %sf)".formatted(factory, ParticleEditorState.formatNumber(start), ParticleEditorState.formatNumber(middle), ParticleEditorState.formatNumber(end))
                : "%s.create(%sf, %sf)".formatted(factory, ParticleEditorState.formatNumber(start), ParticleEditorState.formatNumber(middle));
        return create + ".setCoefficient(" + ParticleEditorState.formatNumber(coefficient) + "f).setEasing(Easing." + ParticleEditorState.easingConstant(startEasing) + ", Easing." + ParticleEditorState.easingConstant(endEasing) + ")";
    }
}
