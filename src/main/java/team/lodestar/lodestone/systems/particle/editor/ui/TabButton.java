package team.lodestar.lodestone.systems.particle.editor.ui;

import team.lodestar.lodestone.systems.particle.editor.api.EditorTab;

public class TabButton {
    public final EditorTab tab;
    public final int x;
    public final int y;
    public final int w;
    public final int h;

    public TabButton(EditorTab tab, int x, int y, int w, int h) {
        this.tab = tab;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public boolean contains(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }
}
