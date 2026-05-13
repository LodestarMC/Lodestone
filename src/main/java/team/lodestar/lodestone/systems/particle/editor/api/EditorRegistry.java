package team.lodestar.lodestone.systems.particle.editor.api;

import com.mojang.serialization.Codec;
import team.lodestar.lodestone.systems.particle.editor.EditorGroups;

import java.util.List;
import java.util.function.Supplier;

public final class EditorRegistry {
    private EditorRegistry() {
    }

    public static EditorTab tab(String key, String label, String icon, int order) {
        return EditorTab.register(key, label, icon, order);
    }

    public static <T> EditorGroupBuilder<T> group(String key, Codec<T> codec, Supplier<T> defaultFactory) {
        return EditorDataGroup.builder(key, codec, defaultFactory);
    }

    public static <T> EditorDataGroup<T> registerGroup(EditorDataGroup<T> group) {
        return EditorGroups.register(group);
    }

    public static List<EditorTab> tabs() {
        return EditorTab.values();
    }

    public static List<EditorDataGroup<?>> groups(EditorTab tab) {
        return EditorGroups.byTab(tab);
    }
}
