package team.lodestar.lodestone.systems.particle.editor.project;

import team.lodestar.lodestone.systems.particle.editor.EditorState;

public class EditorProject {
    public String name;
    public EditorState state;
    public boolean visible = true;

    public EditorProject(String name, EditorState state) {
        this.name = name;
        this.state = state;
    }
}
