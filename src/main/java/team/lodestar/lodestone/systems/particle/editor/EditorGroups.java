package team.lodestar.lodestone.systems.particle.editor;

import com.google.gson.JsonObject;
import net.minecraft.util.Mth;
import team.lodestar.lodestone.systems.particle.editor.data.ColorGroupData;
import team.lodestar.lodestone.systems.particle.editor.data.ParticleCurveGroupData;
import team.lodestar.lodestone.systems.particle.editor.data.ParticleOutputMode;
import team.lodestar.lodestone.systems.particle.editor.data.ScreenshakeGroupData;
import team.lodestar.lodestone.systems.particle.editor.data.SpinGroupData;
import team.lodestar.lodestone.systems.particle.editor.data.TransparencyGroupData;
import team.lodestar.lodestone.systems.particle.editor.api.EditorContext;
import team.lodestar.lodestone.systems.particle.editor.api.EditorDataGroup;
import team.lodestar.lodestone.systems.particle.editor.api.EditorTab;
import team.lodestar.lodestone.systems.particle.editor.ui.Entry;
import team.lodestar.lodestone.systems.particle.editor.ui.EntryFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class EditorGroups {
    private static final List<EditorDataGroup<?>> GROUPS = new ArrayList<>();

    public static final EditorDataGroup<ColorGroupData> COLOR = register(new EditorDataGroup<>(
            "appearance.color",
            "section.color",
            "Color",
            EditorTab.APPEARANCE,
            10,
            ColorGroupData.CODEC
    ) {
        @Override
        public ColorGroupData get(EditorState state) {
            return state.color;
        }

        @Override
        public void set(EditorState state, ColorGroupData data) {
            state.color = data;
        }

        @Override
        public void buildEntries(EditorContext context, List<Entry> entries) {
            ColorGroupData data = context.state().color;
            EntryFactory factory = context.entries();
            entries.add(factory.hexEntry(context.text("entry.start_hex", "Start Hex"), () -> data.startColor, value -> data.startColor = value));
            entries.add(factory.colorChannelsEntry(context.text("entry.start_rgb", "Start RGB"), () -> data.startColor, value -> data.startColor = value));
            entries.add(factory.hexEntry(context.text("entry.end_hex", "End Hex"), () -> data.endColor, value -> data.endColor = value));
            entries.add(factory.colorChannelsEntry(context.text("entry.end_rgb", "End RGB"), () -> data.endColor, value -> data.endColor = value));
            entries.add(factory.actionEntry(context.text("action.swap_colors", "Swap Colors"), () -> {
                int color = data.startColor;
                data.startColor = data.endColor;
                data.endColor = color;
            }));
            entries.add(factory.actionEntry(context.text("action.copy_start_to_end", "Copy Start To End"), () -> data.endColor = data.startColor));
            entries.add(factory.floatEntry(context.text("entry.color_coefficient", "Color Coefficient"), () -> data.coefficient, value -> data.coefficient = value, 0.0f, 8.0f, 0.05f));
            entries.add(factory.easingEntry(context.text("entry.color_easing", "Color Easing"), () -> data.easing, value -> data.easing = value));
        }

        @Override
        public boolean isEnabled(EditorState state) {
            return state.color.enabled;
        }

        @Override
        public void setEnabled(EditorState state, boolean enabled) {
            state.color.enabled = enabled;
        }
    });

    public static final EditorDataGroup<TransparencyGroupData> TRANSPARENCY = register(new EditorDataGroup<>(
            "curve.transparency",
            "section.transparency",
            "Transparency",
            EditorTab.APPEARANCE,
            20,
            TransparencyGroupData.CODEC
    ) {
        @Override
        public TransparencyGroupData get(EditorState state) {
            return state.transparency;
        }

        @Override
        public void set(EditorState state, TransparencyGroupData data) {
            state.transparency = data;
        }

        @Override
        public void buildEntries(EditorContext context, List<Entry> entries) {
            context.curves().addCurveFields(entries, "transparency", "Transparency", context.state().transparency.curve, 0.0f, 1.0f, 0.025f);
        }

        @Override
        public boolean isEnabled(EditorState state) {
            return state.transparency.enabled;
        }

        @Override
        public void setEnabled(EditorState state, boolean enabled) {
            state.transparency.enabled = enabled;
        }
    });

    public static final EditorDataGroup<ParticleCurveGroupData> SCALE = register(new EditorDataGroup<>(
            "curve.scale",
            "section.scale",
            "Scale",
            EditorTab.APPEARANCE,
            30,
            ParticleCurveGroupData.CODEC
    ) {
        @Override
        public ParticleCurveGroupData get(EditorState state) {
            return state.scale;
        }

        @Override
        public void set(EditorState state, ParticleCurveGroupData data) {
            state.scale = data;
        }

        @Override
        public void buildEntries(EditorContext context, List<Entry> entries) {
            context.curves().addLinkedCurveSectionEntries(entries, "scale", "Scale", context.state().scale.curve, value -> context.state().scale.enabled = value, 0.0f, 16.0f, 0.025f);
        }

        @Override
        public boolean isEnabled(EditorState state) {
            return state.scale.enabled;
        }

        @Override
        public void setEnabled(EditorState state, boolean enabled) {
            state.scale.enabled = enabled;
        }
    });

    public static final EditorDataGroup<ParticleCurveGroupData> LENGTH = register(new EditorDataGroup<>(
            "curve.length",
            "section.length",
            "Length",
            EditorTab.APPEARANCE,
            40,
            ParticleCurveGroupData.CODEC
    ) {
        @Override
        public ParticleCurveGroupData get(EditorState state) {
            return state.length;
        }

        @Override
        public void set(EditorState state, ParticleCurveGroupData data) {
            state.length = data;
        }

        @Override
        public void buildEntries(EditorContext context, List<Entry> entries) {
            context.curves().addLinkedCurveSectionEntries(entries, "length", "Length", context.state().length.curve, value -> context.state().length.enabled = value, 0.0f, 32.0f, 0.025f);
        }

        @Override
        public boolean isEnabled(EditorState state) {
            return state.length.enabled;
        }

        @Override
        public void setEnabled(EditorState state, boolean enabled) {
            state.length.enabled = enabled;
        }
    });

    public static final EditorDataGroup<SpinGroupData> SPIN = register(new EditorDataGroup<>(
            "appearance.spin",
            "section.spin",
            "Spin",
            EditorTab.APPEARANCE,
            50,
            SpinGroupData.CODEC
    ) {
        @Override
        public SpinGroupData get(EditorState state) {
            return state.spin;
        }

        @Override
        public void set(EditorState state, SpinGroupData data) {
            state.spin = data;
        }

        @Override
        public void buildEntries(EditorContext context, List<Entry> entries) {
            SpinGroupData data = context.state().spin;
            entries.add(context.entries().floatEntry(context.text("entry.spin_offset", "Spin Offset"), () -> data.offset, value -> data.offset = value, -Mth.TWO_PI, Mth.TWO_PI, 0.05f));
            context.curves().addCurveFields(entries, "spin_speed", "Spin Speed", data.curve, -Mth.TWO_PI, Mth.TWO_PI, 0.025f);
        }

        @Override
        public boolean isEnabled(EditorState state) {
            return state.spin.enabled;
        }

        @Override
        public void setEnabled(EditorState state, boolean enabled) {
            state.spin.enabled = enabled;
        }
    });

    public static final EditorDataGroup<ScreenshakeGroupData> SCREENSHAKE = register(new EditorDataGroup<>(
            "effects.screenshake",
            "section.screenshake",
            "Screenshake",
            EditorTab.EFFECTS,
            10,
            ScreenshakeGroupData.CODEC
    ) {
        @Override
        public ScreenshakeGroupData get(EditorState state) {
            return state.screenshake;
        }

        @Override
        public void set(EditorState state, ScreenshakeGroupData data) {
            state.screenshake = data;
        }

        @Override
        public void buildEntries(EditorContext context, List<Entry> entries) {
            EditorState state = context.state();
            ScreenshakeGroupData data = state.screenshake;
            EntryFactory factory = context.entries();
            entries.add(factory.boolEntry(context.text("entry.shake_on_burst", "Shake On Burst"), () -> data.onBurst, value -> data.onBurst = value));
            entries.add(factory.intEntry(context.text("entry.shake_duration", "Shake Duration"), () -> data.duration, value -> data.duration = value, 1, 1200, 1));
            entries.add(factory.floatEntry(context.text("entry.shake_start", "Shake Start"), () -> data.startStrength, value -> data.startStrength = value, 0.0f, 10.0f, 0.05f));
            entries.add(factory.floatEntry(context.text("entry.shake_middle", "Shake Middle"), () -> data.middleStrength, value -> data.middleStrength = value, 0.0f, 10.0f, 0.05f));
            entries.add(factory.floatEntry(context.text("entry.shake_end", "Shake End"), () -> data.endStrength, value -> data.endStrength = value, 0.0f, 10.0f, 0.05f));
            entries.add(factory.easingEntry(context.text("entry.shake_start_easing", "Shake Start Easing"), () -> data.startEasing, value -> data.startEasing = value));
            entries.add(factory.easingEntry(context.text("entry.shake_end_easing", "Shake End Easing"), () -> data.endEasing, value -> data.endEasing = value));
            entries.add(factory.floatEntry(context.text("entry.shake_coefficient", "Shake Coefficient"), () -> data.coefficient, value -> data.coefficient = value, 0.05f, 8.0f, 0.05f));
            if (state.outputMode == ParticleOutputMode.WORLD) {
                entries.add(factory.boolEntry(context.text("entry.position_falloff", "Position Falloff"), () -> data.positioned, value -> data.positioned = value));
            }
            if (state.outputMode == ParticleOutputMode.WORLD && data.positioned) {
                entries.add(factory.floatEntry(context.text("entry.falloff_distance", "Falloff Distance"), () -> data.falloffDistance, value -> data.falloffDistance = value, 0.0f, 256.0f, 0.5f));
                entries.add(factory.easingEntry(context.text("entry.falloff_easing", "Falloff Easing"), () -> data.falloffEasing, value -> data.falloffEasing = value));
            }
            entries.add(factory.actionEntry(context.text("action.preview_screenshake", "Preview Screenshake"), context::playScreenshakePreview));
        }

        @Override
        public boolean isEnabled(EditorState state) {
            return state.screenshake.enabled;
        }

        @Override
        public void setEnabled(EditorState state, boolean enabled) {
            state.screenshake.enabled = enabled;
        }
    });

    private EditorGroups() {
    }

    public static List<EditorDataGroup<?>> byTab(EditorTab tab) {
        return GROUPS.stream()
                .filter(group -> group.tab().equals(tab))
                .sorted(Comparator.comparingInt(EditorDataGroup::order))
                .toList();
    }

    static List<String> defaultCollapsedCategories() {
        return GROUPS.stream()
                .map(EditorDataGroup::key)
                .toList();
    }

    public static void writeProjectData(JsonObject root, EditorState state) {
        JsonObject groups = new JsonObject();
        for (EditorDataGroup<?> group : GROUPS) {
            group.write(groups, state);
        }
        root.add("groups", groups);
    }

    public static void readProjectData(JsonObject root, EditorState state) {
        if (!root.has("groups") || !root.get("groups").isJsonObject()) {
            return;
        }
        JsonObject groups = root.getAsJsonObject("groups");
        for (EditorDataGroup<?> group : GROUPS) {
            if (groups.has(group.key())) {
                group.read(groups.get(group.key()), state);
            }
        }
    }

    public static synchronized <T> EditorDataGroup<T> register(EditorDataGroup<T> group) {
        for (EditorDataGroup<?> registered : GROUPS) {
            if (registered.key().equals(group.key())) {
                throw new IllegalArgumentException("Duplicate particle editor group: " + group.key());
            }
        }
        GROUPS.add(group);
        return group;
    }
}
