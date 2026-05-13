package team.lodestar.lodestone.systems.particle.editor.data;

public enum BehaviorMode {
    BILLBOARD("billboard"),
    DIRECTIONAL("directional"),
    POINTY_DIRECTIONAL("pointy_directional"),
    SPARK("spark");

    public final String label;

    BehaviorMode(String label) {
        this.label = label;
    }
}
