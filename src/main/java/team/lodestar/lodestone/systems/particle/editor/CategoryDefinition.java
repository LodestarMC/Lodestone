package team.lodestar.lodestone.systems.particle.editor;

import java.util.List;
import java.util.function.Consumer;

record CategoryDefinition(String key, String label, Consumer<List<Entry>> builder, ParticleEditorTab tab) {}
//imagine being a good programmer and seeing this..