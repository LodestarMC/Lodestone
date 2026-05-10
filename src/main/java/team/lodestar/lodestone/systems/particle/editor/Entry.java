package team.lodestar.lodestone.systems.particle.editor;

abstract class Entry {
    final String label;
    final boolean editable;
    final boolean header;
    Entry(String label, boolean editable, boolean header) {
        this.label = label;
        this.editable = editable;
        this.header = header;
    }

    abstract String value();
    void click(int button) {}
    void scroll(double amount) {}
    void nudge(int direction) {
        scroll(direction);
    }
    void commit(String value) {}

    boolean adjustable() {
        return false;
    }
    //just love how beutiful and awful this is
}
