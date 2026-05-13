package team.lodestar.lodestone.systems.particle.editor.api;

import team.lodestar.lodestone.systems.particle.editor.ui.Entry;

import java.util.List;

@FunctionalInterface
public interface EditorGroupEntries<T> {
    void buildEntries(EditorContext context, T data, List<Entry> entries);
}
