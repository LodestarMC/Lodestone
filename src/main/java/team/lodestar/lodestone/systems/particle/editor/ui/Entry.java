package team.lodestar.lodestone.systems.particle.editor.ui;

public abstract class Entry {
    public final String label;
    public final boolean editable;
    public final boolean header;

    public Entry(String label, boolean editable, boolean header) {
        this.label = label;
        this.editable = editable;
        this.header = header;
    }

    public abstract String value();

    public void click(int button) {
    }

    public void scroll(double amount) {
    }

    public void nudge(int direction) {
        scroll(direction);
    }

    public void commit(String value) {
    }

    public boolean adjustable() {
        return false;
    }
}
