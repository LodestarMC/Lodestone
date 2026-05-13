package team.lodestar.lodestone.systems.particle.editor.data;

public enum PreviewPositionMode {
    CAMERA("camera"),
    FIXED("fixed");

    public final String label;

    PreviewPositionMode(String label) {
        this.label = label;
    }
}
