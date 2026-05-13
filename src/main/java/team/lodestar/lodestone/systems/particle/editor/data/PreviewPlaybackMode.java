package team.lodestar.lodestone.systems.particle.editor.data;

public enum PreviewPlaybackMode {
    LOOPING("looping"),
    ONE_SHOT("one_shot");

    public final String label;

    PreviewPlaybackMode(String label) {
        this.label = label;
    }
}
