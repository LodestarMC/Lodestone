package team.lodestar.lodestone.systems.particle.editor;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

class CategoryEntry extends Entry {
    final String key;
    final Consumer<List<Entry>> builder;
    private final Set<String> collapsedCategories;

    CategoryEntry(String key, String label, Consumer<List<Entry>> builder, Set<String> collapsedCategories) {
        super(label, false, true);
        this.key = key;
        this.builder = builder;
        this.collapsedCategories = collapsedCategories;
    }

    @Override
    String value() {
        return collapsedCategories.contains(key) ? "+" : "-";
    }

    @Override
    void click(int button) {
        if (!collapsedCategories.remove(key)) {
            collapsedCategories.add(key);
        }
    }
}
