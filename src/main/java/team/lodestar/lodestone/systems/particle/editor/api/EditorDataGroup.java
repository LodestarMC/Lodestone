package team.lodestar.lodestone.systems.particle.editor.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import team.lodestar.lodestone.systems.particle.editor.EditorState;
import team.lodestar.lodestone.systems.particle.editor.ui.Entry;

import java.util.List;
import java.util.function.Supplier;

public abstract class EditorDataGroup<T> {
    private final String key;
    private final String translationKey;
    private final String fallback;
    private final EditorTab tab;
    private final int order;
    private final Codec<T> codec;

    public EditorDataGroup(String key, String translationKey, String fallback, EditorTab tab, int order, Codec<T> codec) {
        this.key = key;
        this.translationKey = translationKey;
        this.fallback = fallback;
        this.tab = tab;
        this.order = order;
        this.codec = codec;
    }

    public static <T> EditorGroupBuilder<T> builder(String key, Codec<T> codec, Supplier<T> defaultFactory) {
        return new EditorGroupBuilder<>(key, codec, defaultFactory);
    }

    public String key() {
        return key;
    }

    public String translationKey() {
        return translationKey;
    }

    public String fallback() {
        return fallback;
    }

    public EditorTab tab() {
        return tab;
    }

    public int order() {
        return order;
    }

    public abstract T get(EditorState state);

    public abstract void set(EditorState state, T data);

    public abstract void buildEntries(EditorContext context, List<Entry> entries);

    public boolean isEnabled(EditorState state) {
        return true;
    }

    public void setEnabled(EditorState state, boolean enabled) {
    }

    public void write(JsonObject groups, EditorState state) {
        codec.encodeStart(JsonOps.INSTANCE, get(state))
                .resultOrPartial(message -> {
                })
                .ifPresent(element -> groups.add(key, element));
    }

    public void read(JsonElement element, EditorState state) {
        codec.parse(JsonOps.INSTANCE, element)
                .resultOrPartial(message -> {
                })
                .ifPresent(data -> set(state, data));
    }
}
