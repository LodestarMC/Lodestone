package team.lodestar.lodestone.systems.particle.editor;

enum EditorModule {
    COLOR("appearance.color", "section.color", "Color", ParticleEditorTab.APPEARANCE),
    TRANSPARENCY("curve.transparency", "section.transparency", "Transparency", ParticleEditorTab.APPEARANCE),
    SCALE("curve.scale", "section.scale", "Scale", ParticleEditorTab.APPEARANCE),
    LENGTH("curve.length", "section.length", "Length", ParticleEditorTab.APPEARANCE),
    SPIN("appearance.spin", "section.spin", "Spin", ParticleEditorTab.APPEARANCE),
    SCREENSHAKE("effects.screenshake", "section.screenshake", "Screenshake", ParticleEditorTab.EFFECTS);

    final String key;
    final String translationKey;
    final String fallback;
    final ParticleEditorTab tab;

    EditorModule(String key, String translationKey, String fallback, ParticleEditorTab tab) {
        this.key = key;
        this.translationKey = translationKey;
        this.fallback = fallback;
        this.tab = tab;
    }
}
