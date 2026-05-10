package team.lodestar.lodestone.systems.particle.editor;

enum PreviewPositionMode {
    CAMERA("camera"),
    FIXED("fixed");

    final String label;

    PreviewPositionMode(String label) {
        this.label = label;
    }
}
