package team.lodestar.lodestone.systems.particle.editor.ui;

import team.lodestar.lodestone.systems.particle.editor.api.EditorTab;

import java.util.List;
import java.util.function.Consumer;

public record CategoryDefinition(String key, String label, Consumer<List<Entry>> builder, EditorTab tab) {
}
