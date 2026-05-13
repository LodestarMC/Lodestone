package team.lodestar.lodestone.systems.particle.editor.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import team.lodestar.lodestone.systems.particle.editor.ParticleEditorScreen;
import team.lodestar.lodestone.systems.particle.editor.project.EditorProject;
import team.lodestar.lodestone.systems.particle.editor.project.ProjectStorage;

import java.util.List;

import static team.lodestar.lodestone.systems.particle.editor.ParticleEditorScreen.*;
import static team.lodestar.lodestone.systems.particle.editor.ui.EditorUi.*;

public final class ProjectRenderer {
    private final ParticleEditorScreen screen;
    private final List<ClickZone> clickZones;

    public ProjectRenderer(ParticleEditorScreen screen, List<ClickZone> clickZones) {
        this.screen = screen;
        this.clickZones = clickZones;
    }

    public void renderProjectBrowser(Font font, GuiGraphics graphics, int x, int y, int w, int h, int mouseX, int mouseY) {
        graphics.fill(x, y, x + w, y + h, 0x66101520);
        graphics.fill(x, y, x + w, y + 21, 0x70202A3A);
        graphics.drawString(font, trimToWidth(font, screen.editorText("section.particles", "Particles"), w - 10), x + 5, y + 7, ACCENT, false);

        int actionY = y + 25;
        int gap = 3;
        int smallW = 22;
        int loadW = Math.max(36, (w - smallW - gap * 2) / 2);
        int saveW = w - smallW - loadW - gap * 2;
        renderAction(font, graphics, x, actionY, smallW, "+", mouseX, mouseY, screen::addProject);
        renderAction(font, graphics, x + smallW + gap, actionY, loadW, screen.editorText("action.load", "Load"), mouseX, mouseY, screen::openProjectFolder);
        renderAction(font, graphics, x + smallW + loadW + gap * 2, actionY, saveW, screen.editorText("action.save", "Save"), mouseX, mouseY, screen::saveAllProjects);

        int listY = y + 46;
        int listH = Math.max(0, h - 46);
        int visibleRows = screen.visibleProjectRows();
        screen.projectListScroll = Mth.clamp(screen.projectListScroll, 0, Math.max(0, screen.projects.size() - visibleRows));

        graphics.enableScissor(x, listY, x + w, y + h);
        int rowY = listY;
        for (int visible = 0; visible < visibleRows; visible++) {
            int index = screen.projectListScroll + visible;
            if (index >= screen.projects.size() || rowY + ROW_HEIGHT > y + h) {
                break;
            }
            EditorProject project = screen.projects.get(index);
            boolean selected = index == screen.activeProjectIndex;
            boolean hovered = inBounds(mouseX, mouseY, x, rowY, w, ROW_HEIGHT);
            graphics.fill(x, rowY, x + w, rowY + ROW_HEIGHT, selected ? 0xB03A5368 : hovered ? ROW_HOVER : ROW);
            if (selected) {
                graphics.fill(x, rowY, x + 3, rowY + ROW_HEIGHT, ACCENT);
            }
            int removeW = screen.projects.size() > 1 ? 14 : 0;
            int visibilityW = 24;
            int visibilityX = x + 5;
            boolean visibilityHovered = inBounds(mouseX, mouseY, visibilityX, rowY + 2, visibilityW, ROW_HEIGHT - 4);
            graphics.fill(visibilityX, rowY + 2, visibilityX + visibilityW, rowY + ROW_HEIGHT - 2, visibilityHovered ? ROW_HOVER : 0x80404A5F);
            drawCentered(font, graphics, project.visible ? "on" : "off", visibilityX, rowY + 6, visibilityW, project.visible ? ACCENT : MUTED);

            int nameX = visibilityX + visibilityW + 5;
            int nameW = Math.max(22, w - (nameX - x) - removeW - 6);
            boolean editingName = screen.editingListProjectIndex == index;
            String rawName = editingName ? screen.editingListProjectName + "_" : (index + 1) + " " + project.name;
            String label = trimToWidth(font, rawName, nameW);
            graphics.drawString(font, label, nameX, rowY + 5, editingName ? WARNING : selected ? ACCENT : project.visible ? TEXT : MUTED, false);
            int capturedIndex = index;
            clickZones.add(new ClickZone(x, rowY, w, ROW_HEIGHT, () -> screen.selectProject(capturedIndex)));
            clickZones.add(new ClickZone(visibilityX, rowY + 2, visibilityW, ROW_HEIGHT - 4, () -> screen.toggleProjectVisibility(capturedIndex)));
            clickZones.add(new ClickZone(nameX, rowY, nameW, ROW_HEIGHT, () -> screen.clickProjectName(capturedIndex)));
            if (screen.projects.size() > 1) {
                int removeX = x + w - removeW - 2;
                boolean removeHovered = inBounds(mouseX, mouseY, removeX, rowY + 2, removeW, ROW_HEIGHT - 4);
                graphics.fill(removeX, rowY + 2, removeX + removeW, rowY + ROW_HEIGHT - 2, removeHovered ? 0xA0643A45 : 0x80404A5F);
                drawCentered(font, graphics, "x", removeX, rowY + 6, removeW, removeHovered ? WARNING : MUTED);
                clickZones.add(new ClickZone(removeX, rowY + 2, removeW, ROW_HEIGHT - 4, () -> screen.removeProject(capturedIndex)));
            }
            rowY += ROW_HEIGHT + ROW_GAP;
        }
        graphics.flush();
        graphics.disableScissor();

        if (screen.projects.size() > visibleRows) {
            int trackX = x + w - 3;
            int trackY = listY;
            int trackH = Math.max(12, listH);
            int thumbH = Math.max(10, trackH * visibleRows / screen.projects.size());
            int maxScroll = Math.max(1, screen.projects.size() - visibleRows);
            int thumbY = trackY + (trackH - thumbH) * screen.projectListScroll / maxScroll;
            graphics.fill(trackX, trackY, trackX + 2, trackY + trackH, 0x50303A4F);
            graphics.fill(trackX, thumbY, trackX + 2, thumbY + thumbH, ACCENT);
        }
    }

    public void renderProjectPicker(Font font, GuiGraphics graphics, int width, int height, int mouseX, int mouseY) {
        graphics.fill(0, 0, width, height, 0x99000000);
        int pickerW = Math.min(420, Math.max(240, width - 32));
        int pickerH = 160;
        int x = (width - pickerW) / 2;
        int y = (height - pickerH) / 2;
        graphics.fill(x, y, x + pickerW, y + pickerH, PANEL);
        graphics.fill(x, y, x + pickerW, y + 30, PANEL_LIGHT);
        graphics.drawString(font, screen.editorText("dialog.project", "Particle Project"), x + 10, y + 11, TEXT, false);

        graphics.drawString(font, screen.editorText("dialog.new_file", "New File"), x + 10, y + 40, ACCENT, false);
        renderTextBox(font, graphics, x + 10, y + 55, pickerW - 122, screen.newProjectName, screen.editingProjectName, mouseX, mouseY, () -> screen.editingProjectName = true);
        renderAction(font, graphics, x + pickerW - 102, y + 55, 92, screen.editorText("action.create", "Create"), mouseX, mouseY, screen::createFreshProject);

        graphics.drawString(font, screen.editorText("dialog.projects_folder", "Projects Folder"), x + 10, y + 84, ACCENT, false);
        renderFolderPath(font, graphics, x + 10, y + 99, pickerW - 20, mouseX, mouseY, screen::openProjectsFolder);
        int thirdW = (pickerW - 28) / 3;
        renderAction(font, graphics, x + 10, y + 121, thirdW, screen.editorText("action.open_folder", "Open Folder"), mouseX, mouseY, screen::openProjectsFolder);
        renderAction(font, graphics, x + 14 + thirdW, y + 121, thirdW, screen.editorText("action.load", "Load"), mouseX, mouseY, screen::openProjectFolder);
        renderAction(font, graphics, x + 18 + thirdW * 2, y + 121, thirdW, screen.editorText("action.continue", "Continue"), mouseX, mouseY, screen::continueCurrentProject);
    }

    public void renderLoadPicker(Font font, GuiGraphics graphics, int width, int height, int mouseX, int mouseY) {
        graphics.fill(0, 0, width, height, 0xAA000000);
        int pickerW = Math.min(520, Math.max(280, width - 32));
        int pickerH = Math.min(300, Math.max(210, height - 32));
        int x = (width - pickerW) / 2;
        int y = (height - pickerH) / 2;
        graphics.fill(x, y, x + pickerW, y + pickerH, PANEL);
        graphics.fill(x, y, x + pickerW, y + 30, PANEL_LIGHT);
        graphics.drawString(font, screen.editorText("dialog.load_particle_files", "Load Particle Files"), x + 10, y + 11, TEXT, false);

        int listX = x + 10;
        int listY = y + 38;
        int listW = pickerW - 20;
        int listH = pickerH - 72;
        int visibleRows = Math.max(1, listH / (ROW_HEIGHT + ROW_GAP));
        screen.loadPickerScroll = Mth.clamp(screen.loadPickerScroll, 0, Math.max(0, screen.loadPickerProjects.size() - visibleRows));

        graphics.enableScissor(listX, listY, listX + listW, listY + listH);
        int rowY = listY;
        for (int visible = 0; visible < visibleRows; visible++) {
            int index = screen.loadPickerScroll + visible;
            if (index >= screen.loadPickerProjects.size() || rowY + ROW_HEIGHT > listY + listH) {
                break;
            }
            ProjectStorage.LoadedProject project = screen.loadPickerProjects.get(index);
            boolean selected = screen.loadPickerSelection.contains(index);
            boolean hovered = inBounds(mouseX, mouseY, listX, rowY, listW, ROW_HEIGHT);
            graphics.fill(listX, rowY, listX + listW, rowY + ROW_HEIGHT, hovered ? ROW_HOVER : ROW);
            int deleteW = 18;
            int deleteX = listX + listW - deleteW - 2;
            int boxX = listX + 5;
            graphics.fill(boxX, rowY + 3, boxX + 12, rowY + 15, selected ? 0xB065533A : 0x80404A5F);
            drawCentered(font, graphics, selected ? "x" : "", boxX, rowY + 6, 12, selected ? WARNING : MUTED);
            String path = ProjectStorage.root(Minecraft.getInstance()).relativize(project.path()).toString();
            String label = trimToWidth(font, project.name() + "  " + path, listW - 50);
            graphics.drawString(font, label, listX + 23, rowY + 5, selected ? ACCENT : TEXT, false);
            int capturedIndex = index;
            boolean deleteHovered = inBounds(mouseX, mouseY, deleteX, rowY + 2, deleteW, ROW_HEIGHT - 4);
            graphics.fill(deleteX, rowY + 2, deleteX + deleteW, rowY + ROW_HEIGHT - 2, deleteHovered ? 0xA0643A45 : 0x80404A5F);
            drawCentered(font, graphics, "x", deleteX, rowY + 6, deleteW, deleteHovered ? WARNING : MUTED);
            clickZones.add(new ClickZone(listX, rowY, listW - deleteW - 4, ROW_HEIGHT, () -> screen.toggleLoadSelection(capturedIndex)));
            clickZones.add(new ClickZone(deleteX, rowY + 2, deleteW, ROW_HEIGHT - 4, () -> screen.deleteLoadedProject(capturedIndex)));
            rowY += ROW_HEIGHT + ROW_GAP;
        }
        graphics.flush();
        graphics.disableScissor();

        int buttonY = y + pickerH - 25;
        int gap = 4;
        int buttonW = (pickerW - 20 - gap * 4) / 5;
        renderAction(font, graphics, x + 10, buttonY, buttonW, screen.editorText("action.load", "Load"), mouseX, mouseY, () -> screen.loadSelectedProjects(false));
        renderAction(font, graphics, x + 10 + (buttonW + gap), buttonY, buttonW, screen.editorText("action.add", "Add"), mouseX, mouseY, () -> screen.loadSelectedProjects(true));
        renderAction(font, graphics, x + 10 + (buttonW + gap) * 2, buttonY, buttonW, screen.editorText("action.all", "All"), mouseX, mouseY, screen::selectAllLoadProjects);
        renderAction(font, graphics, x + 10 + (buttonW + gap) * 3, buttonY, buttonW, screen.editorText("action.none", "None"), mouseX, mouseY, screen.loadPickerSelection::clear);
        renderAction(font, graphics, x + 10 + (buttonW + gap) * 4, buttonY, pickerW - 20 - (buttonW + gap) * 4, screen.editorText("action.cancel", "Cancel"), mouseX, mouseY, screen::closeLoadPicker);
    }

    private void renderTextBox(Font font, GuiGraphics graphics, int x, int y, int w, String value, boolean active, int mouseX, int mouseY, Runnable action) {
        boolean hovered = inBounds(mouseX, mouseY, x, y, w, 17);
        graphics.fill(x, y, x + w, y + 17, active ? 0xB065533A : hovered ? ROW_HOVER : ROW);
        String text = trimToWidth(font, value + (active ? "_" : ""), w - 8);
        graphics.drawString(font, text, x + 5, y + 5, active ? WARNING : TEXT, false);
        clickZones.add(new ClickZone(x, y, w, 17, action));
    }

    private void renderFolderPath(Font font, GuiGraphics graphics, int x, int y, int w, int mouseX, int mouseY, Runnable action) {
        boolean hovered = inBounds(mouseX, mouseY, x, y, w, 17);
        graphics.fill(x, y, x + w, y + 17, hovered ? ROW_HOVER : ROW);
        graphics.drawString(font, trimToWidth(font, ProjectStorage.root(Minecraft.getInstance()).toString(), w - 8), x + 5, y + 5, hovered ? ACCENT : MUTED, false);
        clickZones.add(new ClickZone(x, y, w, 17, action));
    }

    private void renderAction(Font font, GuiGraphics graphics, int x, int y, int w, String label, int mouseX, int mouseY, Runnable action) {
        boolean hovered = inBounds(mouseX, mouseY, x, y, w, 17);
        graphics.fill(x, y, x + w, y + 17, hovered ? ROW_HOVER : ROW);
        drawCentered(font, graphics, trimToWidth(font, label, w - 6), x, y + 5, w, hovered ? ACCENT : TEXT);
        clickZones.add(new ClickZone(x, y, w, 17, action));
    }
}
