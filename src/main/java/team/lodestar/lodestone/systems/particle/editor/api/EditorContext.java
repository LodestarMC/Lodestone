package team.lodestar.lodestone.systems.particle.editor.api;

import team.lodestar.lodestone.systems.particle.editor.ParticleEditorScreen;
import team.lodestar.lodestone.systems.particle.editor.EditorState;
import team.lodestar.lodestone.systems.particle.editor.ui.CurveEntries;
import team.lodestar.lodestone.systems.particle.editor.ui.EntryFactory;

public final class EditorContext {
    private final ParticleEditorScreen screen;
    private final EntryFactory entries;
    private final CurveEntries curves = new CurveEntries(this);

    public EditorContext(ParticleEditorScreen screen, EntryFactory entries) {
        this.screen = screen;
        this.entries = entries;
    }

    public EditorState state() {
        return screen.state();
    }

    public EntryFactory entries() {
        return entries;
    }

    public CurveEntries curves() {
        return curves;
    }

    public String text(String key, String fallback) {
        return screen.editorText(key, fallback);
    }

    public void playScreenshakePreview() {
        screen.playScreenshakePreview();
    }
}
