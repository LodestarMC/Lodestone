package team.lodestar.lodestone.systems.particle.editor;

class ClickZone {
    final int x;
    final int y;
    final int w;
    final int h;
    final Runnable action;
    final FloatingTabPanel owner;

    ClickZone(int x, int y, int w, int h, Runnable action) {
        this(x, y, w, h, action, null);
    }

    ClickZone(int x, int y, int w, int h, Runnable action, FloatingTabPanel owner) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.action = action;
        this.owner = owner;
    }

    boolean contains(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    boolean ownedBy(FloatingTabPanel target) {
        return owner == target;
    }
}
