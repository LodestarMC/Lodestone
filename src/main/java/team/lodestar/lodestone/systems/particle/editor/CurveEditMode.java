package team.lodestar.lodestone.systems.particle.editor;

enum CurveEditMode {
    SINGLE("single"),
    CURVE("curve");

    final String label;

    CurveEditMode(String label) {
        this.label = label;
    }
}
