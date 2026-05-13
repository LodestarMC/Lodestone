package team.lodestar.lodestone.systems.particle.editor.ui;

import team.lodestar.lodestone.systems.particle.editor.api.EditorDataGroup;
import team.lodestar.lodestone.systems.particle.editor.api.EditorTab;

import java.util.List;
import java.util.function.Consumer;

public class FloatingTabPanel {
    public static final int HEADER_HEIGHT = 28;
    public static final int DOCK_WIDTH = 46;

    public final EditorTab tab;
    public final EditorDataGroup<?> group;
    public final String categoryKey;
    public final String categoryLabel;
    public final Consumer<List<Entry>> categoryBuilder;
    public int x;
    public int y;
    public int w;
    public int h;
    public int scroll;
    public int contentHeight;

    public FloatingTabPanel(EditorTab tab, int x, int y, int w, int h) {
        this.tab = tab;
        this.group = null;
        this.categoryKey = null;
        this.categoryLabel = null;
        this.categoryBuilder = null;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public FloatingTabPanel(EditorDataGroup<?> group, int x, int y, int w, int h) {
        this.tab = null;
        this.group = group;
        this.categoryKey = group.key();
        this.categoryLabel = null;
        this.categoryBuilder = null;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public FloatingTabPanel(String categoryKey, String categoryLabel, Consumer<List<Entry>> categoryBuilder, int x, int y, int w, int h) {
        this.tab = null;
        this.group = null;
        this.categoryKey = categoryKey;
        this.categoryLabel = categoryLabel;
        this.categoryBuilder = categoryBuilder;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public boolean contains(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    public boolean headerContains(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + HEADER_HEIGHT;
    }

    public boolean dockContains(double mouseX, double mouseY) {
        return mouseX >= dockX() && mouseX <= dockX() + DOCK_WIDTH && mouseY >= y + 5 && mouseY <= y + 22;
    }

    public int dockX() {
        return x + w - DOCK_WIDTH - 8;
    }

    public boolean resizeHandleContains(double mouseX, double mouseY) {
        return mouseX >= x + w - 14 && mouseX <= x + w && mouseY >= y + h - 14 && mouseY <= y + h;
    }

    public int bodyHeight() {
        return Math.max(40, h - HEADER_HEIGHT - 22);
    }
}
