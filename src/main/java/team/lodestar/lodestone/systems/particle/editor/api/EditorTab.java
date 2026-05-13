package team.lodestar.lodestone.systems.particle.editor.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class EditorTab {
    private static final List<EditorTab> TABS = new ArrayList<>();

    public static final EditorTab EMITTER = register("emitter", "Source", "S", 0);
    public static final EditorTab MOTION = register("motion", "Movement", "M", 10);
    public static final EditorTab APPEARANCE = register("appearance", "Visuals", "V", 20);
    public static final EditorTab EFFECTS = register("effects", "Effects", "Fx", 30);

    public final String key;
    public final String label;
    public final String icon;
    public final int order;

    private EditorTab(String key, String label, String icon, int order) {
        this.key = key;
        this.label = label;
        this.icon = icon;
        this.order = order;
    }

    public static synchronized EditorTab register(String key, String label, String icon, int order) {
        for (EditorTab tab : TABS) {
            if (tab.key.equals(key)) {
                throw new IllegalArgumentException("Duplicate particle editor tab: " + key);
            }
        }
        EditorTab tab = new EditorTab(key, label, icon, order);
        TABS.add(tab);
        TABS.sort(Comparator.comparingInt((EditorTab value) -> value.order).thenComparing(value -> value.key));
        return tab;
    }

    public static List<EditorTab> values() {
        return List.copyOf(TABS);
    }

    @Override
    public boolean equals(Object object) {
        return this == object || object instanceof EditorTab tab && key.equals(tab.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
