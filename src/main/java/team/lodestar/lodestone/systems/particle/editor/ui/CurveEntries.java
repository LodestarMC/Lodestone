package team.lodestar.lodestone.systems.particle.editor.ui;

import team.lodestar.lodestone.systems.particle.editor.EditorState;
import team.lodestar.lodestone.systems.particle.editor.data.CurveEditMode;
import team.lodestar.lodestone.systems.particle.editor.data.ParticleCurveData;
import team.lodestar.lodestone.systems.particle.editor.api.EditorContext;

import java.util.List;
import java.util.function.Consumer;

public final class CurveEntries {
    private final EditorContext context;

    public CurveEntries(EditorContext context) {
        this.context = context;
    }

    public void addLinkedCurveSectionEntries(List<Entry> entries, String key, String label, ParticleCurveData curve, Consumer<Boolean> setter, float min, float max, float step) {
        EditorState state = context.state();
        setter.accept(true);
        if (state.scaleLengthLinked && isScaleLengthLinkKey(key)) {
            state.scale.enabled = true;
            state.length.enabled = true;
            entries.add(context.entries().floatEntry(context.text("entry.scale_length_value", "Scale / Length"), () -> state.scale.curve.start, this::setScaleLengthLinkedValue, 0.0f, 32.0f, step));
            return;
        }
        entries.add(context.entries().enumEntry(context.text("entry." + key + "_mode", label + " Mode"), () -> curve.linked ? CurveEditMode.SINGLE : CurveEditMode.CURVE, value -> {
            curve.linked = value == CurveEditMode.SINGLE;
            if (curve.linked) {
                curve.setLinkedValue(curve.start);
            }
        }, CurveEditMode.values(), this::curveEditModeLabel));
        if (curve.linked) {
            entries.add(context.entries().floatEntry(context.text("entry." + key + "_value", label), () -> curve.start, value -> curve.setLinkedValue(value), min, max, step));
        } else {
            addCurveFields(entries, key, label, curve, min, max, step);
        }
    }

    public void addCurveFields(List<Entry> entries, String key, String label, ParticleCurveData curve, float min, float max, float step) {
        entries.add(context.entries().boolEntry(context.text("entry." + key + "_sync_endpoints", "Sync Start/End"), () -> curve.endpointsLinked, curve::setEndpointsLinked));
        entries.add(context.entries().floatEntry(context.text("entry." + key + "_start", label + " Start"), () -> curve.start, value -> curve.setStart(value), min, max, step));
        entries.add(context.entries().floatEntry(context.text("entry." + key + (curve.trinary ? "_middle" : "_end"), label + (curve.trinary ? " Middle" : " End")), () -> curve.middle, value -> {
            if (curve.trinary) {
                curve.middle = value;
            } else {
                curve.setFinal(value);
            }
        }, min, max, step));
        entries.add(context.entries().boolEntry(context.text("entry." + key + "_trinary", label + " Trinary"), () -> curve.trinary, curve::setTrinary));
        if (curve.trinary) {
            entries.add(context.entries().floatEntry(context.text("entry." + key + "_end", label + " End"), curve::finalValue, value -> curve.setFinal(value), min, max, step));
            entries.add(context.entries().easingEntry(context.text("entry." + key + "_end_easing", label + " End Easing"), () -> curve.endEasing, value -> curve.endEasing = value));
        }
        entries.add(context.entries().floatEntry(context.text("entry." + key + "_coefficient", label + " Coefficient"), () -> curve.coefficient, value -> curve.coefficient = value, 0.0f, 8.0f, 0.05f));
        entries.add(context.entries().easingEntry(context.text("entry." + key + "_easing", label + " Easing"), () -> curve.startEasing, value -> curve.startEasing = value));
    }

    public void setScaleLengthLinkedValue(float value) {
        EditorState state = context.state();
        state.scale.curve.linked = true;
        state.length.curve.linked = true;
        state.scale.curve.setLinkedValue(value);
        state.length.curve.setLinkedValue(value);
    }

    public static boolean isScaleLengthLinkKey(String key) {
        return "curve.scale".equals(key) || "curve.length".equals(key) || "scale".equals(key) || "length".equals(key);
    }

    private String curveEditModeLabel(CurveEditMode value) {
        return context.text("curve_mode." + value.label, value.label);
    }
}
