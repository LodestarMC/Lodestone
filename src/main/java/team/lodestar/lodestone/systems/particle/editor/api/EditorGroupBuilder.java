package team.lodestar.lodestone.systems.particle.editor.api;

import com.mojang.serialization.Codec;
import team.lodestar.lodestone.systems.particle.editor.EditorGroups;
import team.lodestar.lodestone.systems.particle.editor.EditorState;
import team.lodestar.lodestone.systems.particle.editor.ui.Entry;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class EditorGroupBuilder<T> {
    private final String key;
    private final Codec<T> codec;
    private final Supplier<T> defaultFactory;
    private String translationKey;
    private String fallback;
    private EditorTab tab = EditorTab.APPEARANCE;
    private int order = 1000;
    private EditorGroupEntries<T> entries;
    private Predicate<T> enabledGetter = data -> true;
    private BiConsumer<T, Boolean> enabledSetter = (data, enabled) -> {
    };

    public EditorGroupBuilder(String key, Codec<T> codec, Supplier<T> defaultFactory) {
        this.key = Objects.requireNonNull(key, "key");
        this.codec = Objects.requireNonNull(codec, "codec");
        this.defaultFactory = Objects.requireNonNull(defaultFactory, "defaultFactory");
        this.translationKey = "section." + key.replace('.', '_');
        this.fallback = key;
    }

    public EditorGroupBuilder<T> label(String translationKey, String fallback) {
        this.translationKey = Objects.requireNonNull(translationKey, "translationKey");
        this.fallback = Objects.requireNonNull(fallback, "fallback");
        return this;
    }

    public EditorGroupBuilder<T> tab(EditorTab tab) {
        this.tab = Objects.requireNonNull(tab, "tab");
        return this;
    }

    public EditorGroupBuilder<T> order(int order) {
        this.order = order;
        return this;
    }

    public EditorGroupBuilder<T> entries(EditorGroupEntries<T> entries) {
        this.entries = Objects.requireNonNull(entries, "entries");
        return this;
    }

    public EditorGroupBuilder<T> enabled(Predicate<T> getter, BiConsumer<T, Boolean> setter) {
        this.enabledGetter = Objects.requireNonNull(getter, "getter");
        this.enabledSetter = Objects.requireNonNull(setter, "setter");
        return this;
    }

    public EditorDataGroup<T> build() {
        if (entries == null) {
            throw new IllegalStateException("Particle editor group " + key + " is missing entries builder");
        }
        return new EditorDataGroup<>(key, translationKey, fallback, tab, order, codec) {
            @Override
            public T get(EditorState state) {
                return state.customGroupData(key, defaultFactory);
            }

            @Override
            public void set(EditorState state, T data) {
                state.setCustomGroupData(key, data);
            }

            @Override
            public void buildEntries(EditorContext context, java.util.List<Entry> output) {
                entries.buildEntries(context, get(context.state()), output);
            }

            @Override
            public boolean isEnabled(EditorState state) {
                return enabledGetter.test(get(state));
            }

            @Override
            public void setEnabled(EditorState state, boolean enabled) {
                enabledSetter.accept(get(state), enabled);
            }
        };
    }

    public EditorDataGroup<T> register() {
        return EditorGroups.register(build());
    }
}
