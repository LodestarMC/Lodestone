package team.lodestar.lodestone.systems.particle.editor;

import java.util.function.DoubleFunction;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;

class ShapeSizeEntry extends Entry {
    private final ParticleEditorState state;

    ShapeSizeEntry(String label, ParticleEditorState state, DoubleFunction<String> formatter) {
        super(label, false, false);
        this.state = state;
    }

    @Override
    String value() {
        return "";
    }

    int axisCount() {
        if (state.outputMode == ParticleOutputMode.SCREEN || state.spawnDistribution == SpawnDistributionMode.CIRCLE) {
            return 2;
        }
        return 3;
    }

    String axisLabel(int axis) {
        return switch (axisToField(axis)) {
            case 0 -> "X";
            case 1 -> "Y";
            default -> "Z";
        };
    }

    double axisValue(int axis) {
        return switch (axisToField(axis)) {
            case 0 -> state.shapeSizeX;
            case 1 -> state.shapeSizeY;
            default -> state.shapeSizeZ;
        };
    }

    String axisEntryLabel(int axis) {
        return label + " " + axisLabel(axis);
    }
    void nudgeAxis(int axis, int direction) {
        double multiplier = Screen.hasShiftDown() ? 10.0 : Screen.hasControlDown() ? 0.1 : 1.0;
        setAxis(axis, axisValue(axis) + 0.01 * multiplier * direction);
    }

    private void setAxis(int axis, double rawValue) {
        double value = Mth.clamp(rawValue, 0.0, 64.0);
        switch (axisToField(axis)) {
            case 0 -> state.shapeSizeX = value;
            case 1 -> state.shapeSizeY = value;
            default -> state.shapeSizeZ = value;
        }
    }

    private int axisToField(int axis) {
        if (state.outputMode == ParticleOutputMode.WORLD && state.spawnDistribution == SpawnDistributionMode.CIRCLE && axis == 1) {
            return 2;
        }
        return axis;
    }
}
