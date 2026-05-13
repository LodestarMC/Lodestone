package team.lodestar.lodestone.systems.particle.editor.data;

public enum SpawnDistributionMode {
    BUILDER_RANDOM("builder_random"),
    POINT("point"),
    SPHERE("sphere"),
    CIRCLE("circle"),
    CUBE("cube");

    public final String label;

    SpawnDistributionMode(String label) {
        this.label = label;
    }
}
