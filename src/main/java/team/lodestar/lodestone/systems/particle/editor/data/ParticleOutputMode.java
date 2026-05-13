package team.lodestar.lodestone.systems.particle.editor.data;

public enum ParticleOutputMode {
    WORLD("world"),
    SCREEN("screen");

    public final String label;

    ParticleOutputMode(String label) {
        this.label = label;
    }
}
