package team.lodestar.lodestone.systems.particle.editor.data;

public enum LivePreviewScope {
    ACTIVE("active"),
    VISIBLE_PROJECTS("visible_projects");

    public final String label;

    LivePreviewScope(String label) {
        this.label = label;
    }
}
