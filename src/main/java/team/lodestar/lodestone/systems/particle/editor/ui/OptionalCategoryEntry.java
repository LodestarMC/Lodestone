package team.lodestar.lodestone.systems.particle.editor.ui;

import org.lwjgl.glfw.GLFW;
import team.lodestar.lodestone.systems.particle.editor.api.EditorDataGroup;

import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class OptionalCategoryEntry extends Entry {
    private final String key;
    public final EditorDataGroup<?> group;
    private final BooleanSupplier enabled;
    private final Consumer<Boolean> setter;
    private final Set<String> collapsedCategories;
    private final Runnable stateEdited;

    public OptionalCategoryEntry(String key, String label, EditorDataGroup<?> group, BooleanSupplier enabled, Consumer<Boolean> setter, Set<String> collapsedCategories, Runnable stateEdited) {
        super(label, false, true);
        this.key = key;
        this.group = group;
        this.enabled = enabled;
        this.setter = setter;
        this.collapsedCategories = collapsedCategories;
        this.stateEdited = stateEdited;
    }

    @Override
    public String value() {
        if (!enabled.getAsBoolean()) {
            return "+";
        }
        return collapsedCategories.contains(key) ? ">" : "-";
    }

    @Override
    public void click(int button) {
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

    public boolean canDetach() {
        return group != null && enabled.getAsBoolean();
    }

    public String key() {
        return key;
    }
}
