package team.lodestar.lodestone.systems.particle.editor.ui;

public class RenderedEntry {
    private static final int CONTROL_SIZE = 16;
    private static final int CONTROL_GAP = 3;

    public final Entry entry;
    public final int x;
    public final int y;
    public final int w;
    public final int h;
    public final FloatingTabPanel owner;
    public final int clipTop;
    public final int clipBottom;

    public RenderedEntry(Entry entry, int x, int y, int w, int h, FloatingTabPanel owner, int clipTop, int clipBottom) {
        this.entry = entry;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.owner = owner;
        this.clipTop = clipTop;
        this.clipBottom = clipBottom;
    }

    public boolean contains(double mouseX, double mouseY) {
        int clippedTop = Math.max(y, clipTop);
        int clippedBottom = Math.min(y + h, clipBottom);
        return mouseX >= x && mouseX <= x + w && mouseY >= clippedTop && mouseY <= clippedBottom;
    }

    public boolean minusContains(double mouseX, double mouseY) {
        int minusX = x + w - CONTROL_SIZE * 2 - CONTROL_GAP * 2;
        int clippedTop = Math.max(y + 1, clipTop);
        int clippedBottom = Math.min(y + h - 1, clipBottom);
        return mouseX >= minusX && mouseX <= minusX + CONTROL_SIZE && mouseY >= clippedTop && mouseY <= clippedBottom;
    }

    public boolean plusContains(double mouseX, double mouseY) {
        int plusX = x + w - CONTROL_SIZE - CONTROL_GAP;
        int clippedTop = Math.max(y + 1, clipTop);
        int clippedBottom = Math.min(y + h - 1, clipBottom);
        return mouseX >= plusX && mouseX <= plusX + CONTROL_SIZE && mouseY >= clippedTop && mouseY <= clippedBottom;
    }
}
