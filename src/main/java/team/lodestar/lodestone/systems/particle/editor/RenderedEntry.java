package team.lodestar.lodestone.systems.particle.editor;

class RenderedEntry {
    private static final int CONTROL_SIZE = 16;
    private static final int CONTROL_GAP = 3;

    final Entry entry;
    final int x;
    final int y;
    final int w;
    final int h;
    final FloatingTabPanel owner;
    final int clipTop;
    final int clipBottom;

    RenderedEntry(Entry entry, int x, int y, int w, int h, FloatingTabPanel owner, int clipTop, int clipBottom) {
        this.entry = entry;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.owner = owner;
        this.clipTop = clipTop;
        this.clipBottom = clipBottom;
    }

    boolean contains(double mouseX, double mouseY) {
        int clippedTop = Math.max(y, clipTop);
        int clippedBottom = Math.min(y + h, clipBottom);
        return mouseX >= x && mouseX <= x + w && mouseY >= clippedTop && mouseY <= clippedBottom;
    }

    boolean minusContains(double mouseX, double mouseY) {
        int minusX = x + w - CONTROL_SIZE * 2 - CONTROL_GAP * 2;
        int clippedTop = Math.max(y + 1, clipTop);
        int clippedBottom = Math.min(y + h - 1, clipBottom);
        return mouseX >= minusX && mouseX <= minusX + CONTROL_SIZE && mouseY >= clippedTop && mouseY <= clippedBottom;
    }

    boolean plusContains(double mouseX, double mouseY) {
        int plusX = x + w - CONTROL_SIZE - CONTROL_GAP;
        int clippedTop = Math.max(y + 1, clipTop);
        int clippedBottom = Math.min(y + h - 1, clipBottom);
        return mouseX >= plusX && mouseX <= plusX + CONTROL_SIZE && mouseY >= clippedTop && mouseY <= clippedBottom;
    }
}
