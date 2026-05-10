package team.lodestar.lodestone.systems.particle.editor;

class ParticleEditorProject {
    String name;
    ParticleEditorState state;
    boolean visible = true;

    ParticleEditorProject(String name, ParticleEditorState state) {
        this.name = name;
        this.state = state;
    }
}
