package team.lodestar.lodestone.systems.particle.editor.ui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;
import team.lodestar.lodestone.modules.core.easing.Easing;
import team.lodestar.lodestone.systems.particle.editor.EditorState;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public final class EntryFactory {
    private static final List<Easing> EASINGS = Easing.EASINGS.values().stream()
            .sorted(Comparator.comparing(easing -> easing.name))
            .toList();

    public Entry displayEntry(String label, String value) {
        return new Entry(label, false, false) {
            @Override
            public String value() {
                return value;
            }
        };
    }

    public Entry actionEntry(String label, Runnable action) {
        return new Entry(label, false, false) {
            @Override
            public String value() {
                return ">";
            }

            @Override
            public void click(int button) {
                action.run();
            }
        };
    }

    public Entry boolEntry(String label, BooleanSupplier getter, Consumer<Boolean> setter) {
        return new Entry(label, false, false) {
            @Override
            public String value() {
                return getter.getAsBoolean() ? "true" : "false";
            }

            @Override
            public void click(int button) {
                setter.accept(!getter.getAsBoolean());
            }
        };
    }

    public <T extends Enum<T>> Entry enumEntry(String label, Supplier<T> getter, Consumer<T> setter, T[] values, Function<T, String> namer) {
        return new Entry(label, false, false) {
            @Override
            public String value() {
                return namer.apply(getter.get());
            }

            @Override
            public void click(int button) {
                int index = indexOf(values, getter.get());
                int direction = button == GLFW.GLFW_MOUSE_BUTTON_RIGHT ? -1 : 1;
                setter.accept(values[Math.floorMod(index + direction, values.length)]);
            }

            @Override
            public void scroll(double amount) {
                int index = indexOf(values, getter.get());
                int direction = amount > 0 ? 1 : -1;
                setter.accept(values[Math.floorMod(index + direction, values.length)]);
            }

            @Override
            public boolean adjustable() {
                return true;
            }
        };
    }

    public Entry easingEntry(String label, Supplier<Easing> getter, Consumer<Easing> setter) {
        return new Entry(label, false, false) {
            @Override
            public String value() {
                return getter.get().name;
            }

            @Override
            public void click(int button) {
                int index = EASINGS.indexOf(getter.get());
                int direction = button == GLFW.GLFW_MOUSE_BUTTON_RIGHT ? -1 : 1;
                setter.accept(EASINGS.get(Math.floorMod(index + direction, EASINGS.size())));
            }

            @Override
            public void scroll(double amount) {
                int index = EASINGS.indexOf(getter.get());
                int direction = amount > 0 ? 1 : -1;
                setter.accept(EASINGS.get(Math.floorMod(index + direction, EASINGS.size())));
            }

            @Override
            public boolean adjustable() {
                return true;
            }
        };
    }

    public Entry intEntry(String label, IntSupplier getter, IntConsumer setter, int min, int max, int step) {
        return new Entry(label, true, false) {
            @Override
            public String value() {
                return String.valueOf(getter.getAsInt());
            }

            @Override
            public void commit(String value) {
                setter.accept(Mth.clamp(Integer.parseInt(value), min, max));
            }

            @Override
            public void scroll(double amount) {
                int multiplier = Screen.hasShiftDown() ? 10 : 1;
                int direction = amount > 0 ? 1 : -1;
                setter.accept(Mth.clamp(getter.getAsInt() + step * multiplier * direction, min, max));
            }

            @Override
            public boolean adjustable() {
                return true;
            }
        };
    }

    public Entry floatEntry(String label, Supplier<Float> getter, Consumer<Float> setter, float min, float max, float step) {
        return new Entry(label, true, false) {
            @Override
            public String value() {
                return EditorState.formatNumber(getter.get());
            }

            @Override
            public void commit(String value) {
                setter.accept(Mth.clamp(Float.parseFloat(value), min, max));
            }

            @Override
            public void scroll(double amount) {
                float multiplier = Screen.hasShiftDown() ? 10.0f : Screen.hasControlDown() ? 0.1f : 1.0f;
                float direction = amount > 0 ? 1.0f : -1.0f;
                setter.accept(Mth.clamp(getter.get() + step * multiplier * direction, min, max));
            }

            @Override
            public boolean adjustable() {
                return true;
            }
        };
    }

    public Entry doubleEntry(String label, DoubleSupplier getter, DoubleConsumer setter, double min, double max, double step) {
        return new Entry(label, true, false) {
            @Override
            public String value() {
                return EditorState.formatNumber(getter.getAsDouble());
            }

            @Override
            public void commit(String value) {
                setter.accept(Mth.clamp(Double.parseDouble(value), min, max));
            }

            @Override
            public void scroll(double amount) {
                double multiplier = Screen.hasShiftDown() ? 10.0 : Screen.hasControlDown() ? 0.1 : 1.0;
                double direction = amount > 0 ? 1.0 : -1.0;
                setter.accept(Mth.clamp(getter.getAsDouble() + step * multiplier * direction, min, max));
            }

            @Override
            public boolean adjustable() {
                return true;
            }
        };
    }

    public Entry hexEntry(String label, IntSupplier getter, IntConsumer setter) {
        return new Entry(label, true, false) {
            @Override
            public String value() {
                return "#" + String.format(Locale.ROOT, "%06X", getter.getAsInt() & 0xFFFFFF);
            }

            @Override
            public void commit(String value) {
                String clean = value.trim();
                if (clean.startsWith("#")) {
                    clean = clean.substring(1);
                }
                setter.accept(Integer.parseUnsignedInt(clean, 16) & 0xFFFFFF);
            }
        };
    }

    public Entry colorChannelsEntry(String label, IntSupplier getter, IntConsumer setter) {
        return new ColorChannelsEntry(label, getter, setter);
    }

    private static <T> int indexOf(T[] values, T value) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] == value) {
                return i;
            }
        }
        return 0;
    }
}
