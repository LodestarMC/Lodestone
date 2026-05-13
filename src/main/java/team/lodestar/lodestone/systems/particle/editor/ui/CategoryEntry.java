package team.lodestar.lodestone.systems.particle.editor.ui;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class CategoryEntry extends Entry {
    public final String key;
    public final Consumer<List<Entry>> builder;
    private final Set<String> collapsedCategories;

    public CategoryEntry(String key, String label, Consumer<List<Entry>> builder, Set<String> collapsedCategories) {
        super(label, false, true);
        this.key = key;
        this.builder = builder;
        this.collapsedCategories = collapsedCategories;
    }

    @Override
    public String value() {
        return collapsedCategories.contains(key) ? "+" : "-";
    }

    @Override
    public void click(int button) {
        if (!collapsedCategories.remove(key)) {
            collapsedCategories.add(key);
        }
    }
}
