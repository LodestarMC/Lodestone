package team.lodestar.lodestone.systems.particle.editor;

enum ParticleEditorTab {
    EMITTER("Source"),
    MOTION("Movement"),
    APPEARANCE("Visuals"),
    EFFECTS("Effects");

    final String label;

    ParticleEditorTab(String label) {
        this.label = label;
    }
}
