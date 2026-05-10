package team.lodestar.lodestone.systems.particle.editor;

class TabButton {
    final ParticleEditorTab tab;
    final int x;
    final int y;
    final int w;
    final int h;

    TabButton(ParticleEditorTab tab, int x, int y, int w, int h) {
        this.tab = tab;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    boolean contains(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }
}
