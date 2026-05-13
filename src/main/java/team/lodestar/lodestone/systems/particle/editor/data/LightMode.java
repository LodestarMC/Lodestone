package team.lodestar.lodestone.systems.particle.editor.data;

public enum LightMode {
    FULL_BRIGHT("full_bright"),
    NATURAL("natural"),
    CUSTOM("custom");

    public final String label;

    LightMode(String label) {
        this.label = label;
    }
}
