package team.lodestar.lodestone.systems.particle.editor;

import org.lwjgl.glfw.GLFW;

import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

class OptionalCategoryEntry extends Entry {
    private final String key;
    final EditorModule module;
    private final BooleanSupplier enabled;
    private final Consumer<Boolean> setter;
    private final Set<String> collapsedCategories;
    private final Runnable stateEdited;

    OptionalCategoryEntry(String key, String label, EditorModule module, BooleanSupplier enabled, Consumer<Boolean> setter, Set<String> collapsedCategories, Runnable stateEdited) {
        super(label, false, true);
        this.key = key;
        this.module = module;
        this.enabled = enabled;
        this.setter = setter;
        this.collapsedCategories = collapsedCategories;
        this.stateEdited = stateEdited;
    }

    @Override
    String value() {
        if (!enabled.getAsBoolean()) {
            return "+";
        }
        return collapsedCategories.contains(key) ? ">" : "-";
    }

    @Override
    void click(int button) {
        if (!enabled.getAsBoolean()) {
            setter.accept(true);
            collapsedCategories.remove(key);
            stateEdited.run();
            return;
        }
        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            setter.accept(false);
            collapsedCategories.remove(key);
            stateEdited.run();
            return;
        }
        if (!collapsedCategories.remove(key)) {
            collapsedCategories.add(key);
        }
    }

    boolean canDetach() {
        return module != null && enabled.getAsBoolean();
    }
}
