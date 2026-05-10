package team.lodestar.lodestone.systems.particle.editor;

import java.util.List;
import java.util.function.Consumer;

class FloatingTabPanel {
    static final int HEADER_HEIGHT = 28;
    static final int DOCK_WIDTH = 46;

    final ParticleEditorTab tab;
    final EditorModule module;
    final String categoryKey;
    final String categoryLabel;
    final Consumer<List<Entry>> categoryBuilder;
    int x;
    int y;
    int w;
    int h;
    int scroll;
    int contentHeight;

    FloatingTabPanel(ParticleEditorTab tab, int x, int y, int w, int h) {
        this.tab = tab;
        this.module = null;
        this.categoryKey = null;
        this.categoryLabel = null;
        this.categoryBuilder = null;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    FloatingTabPanel(EditorModule module, int x, int y, int w, int h) {
        this.tab = null;
        this.module = module;
        this.categoryKey = module.key;
        this.categoryLabel = null;
        this.categoryBuilder = null;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    FloatingTabPanel(String categoryKey, String categoryLabel, Consumer<List<Entry>> categoryBuilder, int x, int y, int w, int h) {
        this.tab = null;
        this.module = null;
        this.categoryKey = categoryKey;
        this.categoryLabel = categoryLabel;
        this.categoryBuilder = categoryBuilder;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    boolean contains(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    boolean headerContains(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + HEADER_HEIGHT;
    }

    boolean dockContains(double mouseX, double mouseY) {
        return mouseX >= dockX() && mouseX <= dockX() + DOCK_WIDTH && mouseY >= y + 5 && mouseY <= y + 22;
    }

    int dockX() {
        return x + w - DOCK_WIDTH - 8;
    }

    boolean resizeHandleContains(double mouseX, double mouseY) {
        return mouseX >= x + w - 14 && mouseX <= x + w && mouseY >= y + h - 14 && mouseY <= y + h;
    }

    int bodyHeight() {
        return Math.max(40, h - HEADER_HEIGHT - 22);
    }
}

//never again.
