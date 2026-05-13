package team.lodestar.lodestone.systems.particle.editor.ui;

public class ClickZone {
    public final int x;
    public final int y;
    public final int w;
    public final int h;
    public final Runnable action;
    public final FloatingTabPanel owner;

    public ClickZone(int x, int y, int w, int h, Runnable action) {
        this(x, y, w, h, action, null);
    }

    public ClickZone(int x, int y, int w, int h, Runnable action, FloatingTabPanel owner) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.action = action;
        this.owner = owner;
    }

    public boolean contains(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    public boolean ownedBy(FloatingTabPanel target) {
        return owner == target;
    }
}
