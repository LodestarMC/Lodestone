package team.lodestar.lodestone.systems.particle.editor.ui;

import net.minecraft.util.Mth;
import team.lodestar.lodestone.systems.particle.editor.ParticleEditorScreen;
import team.lodestar.lodestone.systems.particle.editor.api.EditorDataGroup;
import team.lodestar.lodestone.systems.particle.editor.api.EditorTab;

import java.util.List;
import java.util.function.Consumer;

public final class FloatingPanels {
    private final ParticleEditorScreen screen;
    private final List<FloatingTabPanel> panels;

    public FloatingPanels(ParticleEditorScreen screen, List<FloatingTabPanel> panels) {
        this.screen = screen;
        this.panels = panels;
    }

    public FloatingTabPanel detachTab(EditorTab targetTab, int mouseX, int mouseY) {
        FloatingTabPanel panel = find(targetTab);
        if (panel == null) {
            int panelW = Math.min(430, Math.max(260, screen.width - 32));
            int panelH = Math.min(360, Math.max(220, screen.height - 32));
            panel = new FloatingTabPanel(targetTab, mouseX - panelW / 2, mouseY - 12, panelW, panelH);
            panels.add(panel);
        }
        place(panel, mouseX, mouseY);
        return panel;
    }

    public FloatingTabPanel detachGroup(EditorDataGroup<?> group, int mouseX, int mouseY) {
        FloatingTabPanel panel = find(group);
        if (panel == null) {
            int panelW = Math.min(360, Math.max(260, screen.width - 32));
            int panelH = Math.min(320, Math.max(200, screen.height - 32));
            panel = new FloatingTabPanel(group, mouseX - panelW / 2, mouseY - 12, panelW, panelH);
            panels.add(panel);
        }
        place(panel, mouseX, mouseY);
        return panel;
    }

    public FloatingTabPanel detachCategory(String key, String label, Consumer<List<Entry>> builder, int mouseX, int mouseY) {
        if (builder == null) {
            CategoryDefinition definition = screen.categoryDefinitions.get(key);
            if (definition != null) {
                label = definition.label();
                builder = definition.builder();
            }
        }
        if (builder == null) {
            return null;
        }
        FloatingTabPanel panel = find(key);
        if (panel == null) {
            int panelW = Math.min(360, Math.max(260, screen.width - 32));
            int panelH = Math.min(320, Math.max(200, screen.height - 32));
            panel = new FloatingTabPanel(key, label, builder, mouseX - panelW / 2, mouseY - 12, panelW, panelH);
            panels.add(panel);
        }
        place(panel, mouseX, mouseY);
        return panel;
    }

    public FloatingTabPanel find(EditorTab targetTab) {
        for (FloatingTabPanel panel : panels) {
            if (targetTab.equals(panel.tab)) {
                return panel;
            }
        }
        return null;
    }

    public FloatingTabPanel find(String categoryKey) {
        for (FloatingTabPanel panel : panels) {
            if (categoryKey.equals(panel.categoryKey)) {
                return panel;
            }
        }
        return null;
    }

    public FloatingTabPanel find(EditorDataGroup<?> group) {
        for (FloatingTabPanel panel : panels) {
            if (panel.group == group) {
                return panel;
            }
        }
        return null;
    }

    public FloatingTabPanel at(double mouseX, double mouseY) {
        for (int i = panels.size() - 1; i >= 0; i--) {
            FloatingTabPanel panel = panels.get(i);
            if (panel.contains(mouseX, mouseY)) {
                return panel;
            }
        }
        return null;
    }

    public void bringToFront(FloatingTabPanel panel) {
        if (panels.remove(panel)) {
            panels.add(panel);
        }
    }

    public void clamp(FloatingTabPanel panel) {
        int maxW = Math.max(1, screen.width);
        int maxH = Math.max(1, screen.height);
        panel.w = Mth.clamp(panel.w, Math.min(ParticleEditorScreen.MIN_FLOATING_PANEL_WIDTH, maxW), maxW);
        panel.h = Mth.clamp(panel.h, Math.min(ParticleEditorScreen.MIN_FLOATING_PANEL_HEIGHT, maxH), maxH);
        panel.x = Mth.clamp(panel.x, 0, Math.max(0, screen.width - panel.w));
        panel.y = Mth.clamp(panel.y, 0, Math.max(0, screen.height - panel.h));
        panel.scroll = screen.clampScroll(panel.scroll, panel.contentHeight, panel.bodyHeight());
    }

    private void place(FloatingTabPanel panel, int mouseX, int mouseY) {
        panel.x = mouseX - panel.w / 2;
        panel.y = mouseY - 12;
        clamp(panel);
        bringToFront(panel);
        screen.cancelEditing();
    }
}
