package team.lodestar.lodestone.systems.particle.editor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.helpers.RenderHelper;
import team.lodestar.lodestone.modules.core.easing.Easing;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import static team.lodestar.lodestone.systems.particle.editor.ParticleEditorUi.*;

public class ParticleEditorScreen extends Screen {
    private static final int PANEL = 0xD012141D;
    private static final int PANEL_LIGHT = 0xE01B1F2B;
    private static final int ROW = 0x70303A4F;
    private static final int ROW_HOVER = 0x9050627F;
    private static final int TEXT = 0xFFE8EDF5;
    private static final int MUTED = 0xFF9BA9BC;
    private static final int ACCENT = 0xFF75D6FF;
    private static final int WARNING = 0xFFFFD166;
    private static final int ROW_HEIGHT = 18;
    private static final int ROW_GAP = 3;
    private static final int CONTROL_SIZE = 16;
    private static final int CONTROL_GAP = 3;
    private static final int DRAG_STEP_PIXELS = 6;
    private static final int DEFAULT_PANEL_WIDTH = 620;
    private static final int MIN_PANEL_WIDTH = 260;
    private static final int MIN_PANEL_HEIGHT = 260;
    private static final int MIN_FLOATING_PANEL_WIDTH = 240;
    private static final int MIN_FLOATING_PANEL_HEIGHT = 160;
    private static final int RESIZE_HANDLE_SIZE = 14;
    private static final int CONTENT_BOTTOM_PADDING = RESIZE_HANDLE_SIZE + 8;
    private static final int SIDEBAR_WIDTH = 34;
    private static final int PROJECT_BROWSER_MIN_WIDTH = 146;
    private static final int PROJECT_BROWSER_MAX_WIDTH = 210;
    private static final int PROJECT_PICKER_WIDTH = 420;
    private static final int PROJECT_PICKER_HEIGHT = 160;
    private static final String funny_name_here = "Particles";

    private static final List<Easing> EASINGS = Easing.EASINGS.values().stream()
            .sorted(Comparator.comparing(easing -> easing.name))
            .toList();
    private static final List<ParticleEditorProject> SAVED_PROJECTS = new ArrayList<>();
    private static final Set<String> DEFAULT_COLLAPSED_CATEGORIES = Set.of(
            "projects",
            "source",
            "render",
            "lifetime",
            "preview",
            "motion.velocity",
            "motion.randomness",
            "motion.spawn_area",
            "motion.physics",
            "motion.behavior_data",
            "appearance.color",
            "curve.transparency",
            "curve.scale",
            "curve.length",
            "appearance.spin",
            "effects.screenshake"
    );
    private static final Set<String> COLLAPSED_CATEGORIES = new HashSet<>(DEFAULT_COLLAPSED_CATEGORIES);
    private static int savedActiveProjectIndex;
    private static int savedLoadProjectIndex = -1;
    private static ParticleEditorTab savedTab = ParticleEditorTab.EMITTER;
    private static int savedPanelX = 16;
    private static int savedPanelY = 18;
    private static int savedPanelWidth = DEFAULT_PANEL_WIDTH;
    private static int savedPanelHeight;
    private static LivePreviewScope savedLivePreviewScope = LivePreviewScope.ACTIVE;
    private static boolean savedProjectPickerSeen;

    private ParticleEditorState state;
    private final List<ParticleEditorProject> projects;
    private final Set<LodestoneWorldParticle> previewParticles = Collections.newSetFromMap(new IdentityHashMap<>());
    private final ScreenParticleHolder screenPreviewParticles = new ScreenParticleHolder();
    private final List<RenderedEntry> renderedEntries = new ArrayList<>();
    private final List<ClickZone> clickZones = new ArrayList<>();
    private final List<TabButton> renderedTabs = new ArrayList<>();
    private final List<FloatingTabPanel> floatingPanels = new ArrayList<>();
    private final List<ParticleEditorStorage.LoadedProject> loadPickerProjects = new ArrayList<>();
    private final Set<Integer> loadPickerSelection = new HashSet<>();
    private final Map<String, CategoryDefinition> categoryDefinitions = new HashMap<>();

    private ParticleEditorTab tab = ParticleEditorTab.EMITTER;
    private ParticleEditorTab buildingTab;
    private LivePreviewScope livePreviewScope = savedLivePreviewScope;
    private int scroll;
    private int projectListScroll;
    private int contentHeight;
    private int mainEntriesViewportHeight;
    private Entry editingEntry;
    private String editingText = "";
    private Entry draggingEntry;
    private double draggingMouseX;
    private boolean draggingMoved;
    private TabButton pressedTab;
    private double tabDragStartX;
    private double tabDragStartY;
    private FloatingTabPanel movingFloatingPanel;
    private FloatingTabPanel resizingFloatingPanel;
    private double movingPanelOffsetX;
    private double movingPanelOffsetY;
    private boolean resizingPanel;
    private double resizeStartMouseX;
    private double resizeStartMouseY;
    private int resizeStartWidth;
    private int resizeStartHeight;
    private boolean movingPanel;
    private double movingScreenOffsetX;
    private double movingScreenOffsetY;
    private int panelX = 16;
    private int panelY = 18;
    private int panelWidth = DEFAULT_PANEL_WIDTH;
    private int panelHeight;
    private int lastPanelX;
    private int lastPanelY;
    private int lastPanelW;
    private int lastPanelH;
    private int lastProjectBrowserX;
    private int lastProjectBrowserY;
    private int lastProjectBrowserW;
    private int lastProjectBrowserH;
    private boolean projectBrowserVisible;
    private final Map<ParticleEditorProject, Integer> livePreviewTicks = new IdentityHashMap<>();
    private final Set<ParticleEditorProject> livePreviewOneShots = Collections.newSetFromMap(new IdentityHashMap<>());
    private int activeProjectIndex;
    private int loadProjectIndex = -1;
    private boolean projectPickerOpen;
    private boolean loadPickerOpen;
    private boolean editingProjectName;
    private int loadPickerScroll;
    private String newProjectName = "particle_1";
    private int editingListProjectIndex = -1;
    private String editingListProjectName = "";
    private String lastPreviewSnapshot;

    public ParticleEditorScreen() {
        super(Component.translatable("screen.lodestone.particle_editor.title"));
        ensureSavedWorkspace();
        projects = SAVED_PROJECTS;
        activeProjectIndex = Mth.clamp(savedActiveProjectIndex, 0, projects.size() - 1);
        loadProjectIndex = savedLoadProjectIndex;
        tab = savedTab;
        panelX = savedPanelX;
        panelY = savedPanelY;
        panelWidth = savedPanelWidth;
        panelHeight = savedPanelHeight;
        state = activeProject().state;
        projectPickerOpen = !savedProjectPickerSeen;
        editingProjectName = projectPickerOpen;
    }

    @Override
    protected void init() {
        ParticleEditorOverlayRenderer.setActiveScreen(this);
        ParticleEditorPreviewSession.stop(false);
        initializeFixedPreviewPositions();
        scroll = 0;
        resetLivePreviewTimers();
        lastPreviewSnapshot = livePreviewSnapshot();
    }

    @Override
    public void tick() {
        prunePreviewParticles();
        screenPreviewParticles.tick();
        if (projectPickerOpen || loadPickerOpen) {
            return;
        }
        handleRealtimePreviewChanges();
        tickLivePreview();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fill(0, 0, width, height, 0x22000000);
        screenPreviewParticles.render(graphics);
        renderScreenShapeOverlays(graphics);
        renderedEntries.clear();
        clickZones.clear();
        renderedTabs.clear();

        int margin = 8;
        panelX = Mth.clamp(panelX, 0, Math.max(0, width - Math.min(MIN_PANEL_WIDTH, width)));
        panelY = Mth.clamp(panelY, 0, Math.max(0, height - Math.min(MIN_PANEL_HEIGHT, height)));
        int panelX = this.panelX;
        int panelY = this.panelY;
        int availablePanelW = Math.max(1, width - panelX - margin);
        int availablePanelH = Math.max(1, height - panelY - margin);
        if (panelHeight == 0) {
            panelHeight = availablePanelH;
        }
        int panelW = Mth.clamp(panelWidth, Math.min(MIN_PANEL_WIDTH, availablePanelW), availablePanelW);
        int panelH = Mth.clamp(panelHeight, Math.min(MIN_PANEL_HEIGHT, availablePanelH), availablePanelH);
        panelWidth = panelW;
        panelHeight = panelH;
        lastPanelX = panelX;
        lastPanelY = panelY;
        lastPanelW = panelW;
        lastPanelH = panelH;
        int headerH = 30;
        int bodyY = panelY + headerH;
        int sidebarH = panelH - headerH - CONTENT_BOTTOM_PADDING;
        int bodyContentX = panelX + SIDEBAR_WIDTH + 8;
        int entriesY = bodyY + 8;
        int entriesH = panelH - headerH - CONTENT_BOTTOM_PADDING - 8;
        int projectBrowserW = panelW >= 430 ? Mth.clamp(panelW / 4, PROJECT_BROWSER_MIN_WIDTH, PROJECT_BROWSER_MAX_WIDTH) : 0;
        projectBrowserVisible = projectBrowserW > 0;
        lastProjectBrowserX = bodyContentX;
        lastProjectBrowserY = entriesY;
        lastProjectBrowserW = projectBrowserW;
        lastProjectBrowserH = entriesH;
        int entriesX = bodyContentX + (projectBrowserVisible ? projectBrowserW + 8 : 0);
        int entriesW = panelW - SIDEBAR_WIDTH - 16 - (projectBrowserVisible ? projectBrowserW + 8 : 0);

        graphics.fill(panelX, panelY, panelX + panelW, panelY + panelH, PANEL);
        graphics.fill(panelX, panelY, panelX + panelW, panelY + headerH, PANEL_LIGHT);

        graphics.drawString(font, trimToWidth(title.getString(), Math.max(24, panelW - 20)), panelX + 10, panelY + 10, TEXT, false);
        renderSidebarTabs(graphics, panelX, bodyY, SIDEBAR_WIDTH, Math.max(40, sidebarH), mouseX, mouseY);
        if (projectBrowserVisible) {
            renderProjectBrowser(graphics, lastProjectBrowserX, lastProjectBrowserY, lastProjectBrowserW, lastProjectBrowserH, mouseX, mouseY);
        }
        refreshCategoryDefinitions();
        renderEntries(graphics, entriesX, entriesY, Math.max(40, entriesW), Math.max(40, entriesH), mouseX, mouseY);
        renderResizeHandle(graphics, panelX, panelY, panelW, panelH, resizeHandleContains(mouseX, mouseY), resizingPanel);
        renderFloatingPanels(graphics, mouseX, mouseY);

        if (editingEntry != null) {
            graphics.drawString(font, "Editing: " + editingEntry.label + " = " + editingText, panelX + 10, panelY + panelH - 13, WARNING, false);
        }
        if (projectPickerOpen) {
            clickZones.clear();
            renderProjectPicker(graphics, mouseX, mouseY);
        }
        if (loadPickerOpen) {
            clickZones.clear();
            renderLoadPicker(graphics, mouseX, mouseY);
        }
    }

    private void renderResizeHandle(GuiGraphics graphics, int x, int y, int w, int h, boolean hovered, boolean active) {
        int handleX = x + w - RESIZE_HANDLE_SIZE;
        int handleY = y + h - RESIZE_HANDLE_SIZE;
        int color = hovered || active ? ACCENT : MUTED;
        graphics.fill(handleX + 9, handleY + 3, handleX + 11, handleY + 11, color);
        graphics.fill(handleX + 5, handleY + 7, handleX + 7, handleY + 11, color);
        graphics.fill(handleX + 1, handleY + 11, handleX + 11, handleY + 13, color);
    }

    private void renderAction(GuiGraphics graphics, int x, int y, int w, String label, int mouseX, int mouseY, Runnable action) {
        boolean hovered = inBounds(mouseX, mouseY, x, y, w, 17);
        graphics.fill(x, y, x + w, y + 17, hovered ? ROW_HOVER : ROW);
        drawCentered(graphics, trimToWidth(label, w - 6), x, y + 5, w, hovered ? ACCENT : TEXT);
        clickZones.add(new ClickZone(x, y, w, 17, action));
    }

    private void renderProjectBrowser(GuiGraphics graphics, int x, int y, int w, int h, int mouseX, int mouseY) {
        graphics.fill(x, y, x + w, y + h, 0x66101520);
        graphics.fill(x, y, x + w, y + 21, 0x70202A3A);
        graphics.drawString(font, trimToWidth(editorText("section.particles", funny_name_here), w - 10), x + 5, y + 7, ACCENT, false);

        int actionY = y + 25;
        int gap = 3;
        int smallW = 22;
        int loadW = Math.max(36, (w - smallW - gap * 2) / 2);
        int saveW = w - smallW - loadW - gap * 2;
        renderAction(graphics, x, actionY, smallW, "+", mouseX, mouseY, this::addProject);
        renderAction(graphics, x + smallW + gap, actionY, loadW, editorText("action.load", "Load"), mouseX, mouseY, this::openProjectFolder);
        renderAction(graphics, x + smallW + loadW + gap * 2, actionY, saveW, editorText("action.save", "Save"), mouseX, mouseY, this::saveAllProjects);

        int listY = y + 46;
        int listH = Math.max(0, h - 46);
        int visibleRows = visibleProjectRows();
        projectListScroll = Mth.clamp(projectListScroll, 0, Math.max(0, projects.size() - visibleRows));

        graphics.enableScissor(x, listY, x + w, y + h);
        int rowY = listY;
        for (int visible = 0; visible < visibleRows; visible++) {
            int index = projectListScroll + visible;
            if (index >= projects.size() || rowY + ROW_HEIGHT > y + h) {
                break;
            }
            ParticleEditorProject project = projects.get(index);
            boolean selected = index == activeProjectIndex;
            boolean hovered = inBounds(mouseX, mouseY, x, rowY, w, ROW_HEIGHT);
            graphics.fill(x, rowY, x + w, rowY + ROW_HEIGHT, selected ? 0xB03A5368 : hovered ? ROW_HOVER : ROW);
            if (selected) {
                graphics.fill(x, rowY, x + 3, rowY + ROW_HEIGHT, ACCENT);
            }
            int removeW = projects.size() > 1 ? 14 : 0;
            int visibilityW = 24;
            int visibilityX = x + 5;
            boolean visibilityHovered = inBounds(mouseX, mouseY, visibilityX, rowY + 2, visibilityW, ROW_HEIGHT - 4);
            graphics.fill(visibilityX, rowY + 2, visibilityX + visibilityW, rowY + ROW_HEIGHT - 2, visibilityHovered ? ROW_HOVER : 0x80404A5F);
            drawCentered(graphics, project.visible ? "on" : "off", visibilityX, rowY + 6, visibilityW, project.visible ? ACCENT : MUTED);

            int nameX = visibilityX + visibilityW + 5;
            int nameW = Math.max(22, w - (nameX - x) - removeW - 6);
            boolean editingName = editingListProjectIndex == index;
            String rawName = editingName ? editingListProjectName + "_" : (index + 1) + " " + project.name;
            String label = trimToWidth(rawName, nameW);
            graphics.drawString(font, label, nameX, rowY + 5, editingName ? WARNING : selected ? ACCENT : project.visible ? TEXT : MUTED, false);
            int capturedIndex = index;
            clickZones.add(new ClickZone(x, rowY, w, ROW_HEIGHT, () -> selectProject(capturedIndex)));
            clickZones.add(new ClickZone(visibilityX, rowY + 2, visibilityW, ROW_HEIGHT - 4, () -> toggleProjectVisibility(capturedIndex)));
            clickZones.add(new ClickZone(nameX, rowY, nameW, ROW_HEIGHT, () -> clickProjectName(capturedIndex)));
            if (projects.size() > 1) {
                int removeX = x + w - removeW - 2;
                boolean removeHovered = inBounds(mouseX, mouseY, removeX, rowY + 2, removeW, ROW_HEIGHT - 4);
                graphics.fill(removeX, rowY + 2, removeX + removeW, rowY + ROW_HEIGHT - 2, removeHovered ? 0xA0643A45 : 0x80404A5F);
                drawCentered(graphics, "x", removeX, rowY + 6, removeW, removeHovered ? WARNING : MUTED);
                clickZones.add(new ClickZone(removeX, rowY + 2, removeW, ROW_HEIGHT - 4, () -> removeProject(capturedIndex)));
            }
            rowY += ROW_HEIGHT + ROW_GAP;
        }
        graphics.flush();
        graphics.disableScissor();

        if (projects.size() > visibleRows) {
            int trackX = x + w - 3;
            int trackY = listY;
            int trackH = Math.max(12, listH);
            int thumbH = Math.max(10, trackH * visibleRows / projects.size());
            int maxScroll = Math.max(1, projects.size() - visibleRows);
            int thumbY = trackY + (trackH - thumbH) * projectListScroll / maxScroll;
            graphics.fill(trackX, trackY, trackX + 2, trackY + trackH, 0x50303A4F);
            graphics.fill(trackX, thumbY, trackX + 2, thumbY + thumbH, ACCENT);
        }
    }

    private void renderSidebarTabs(GuiGraphics graphics, int x, int y, int w, int h, int mouseX, int mouseY) {
        graphics.fill(x, y, x + w, y + h, 0x80101520);
        ParticleEditorTab[] tabs = ParticleEditorTab.values();
        int tabH = 29;
        int gap = 5;
        int ty = y + 8;
        for (int i = 0; i < tabs.length; i++) {
            int tx = x + 4;
            int tw = w - 8;
            if (ty + tabH > y + h) {
                continue;
            }
            boolean selected = tab == tabs[i];
            boolean hovered = inBounds(mouseX, mouseY, tx, ty, tw, tabH);
            boolean floating = findFloatingPanel(tabs[i]) != null;
            graphics.fill(tx, ty, tx + tw, ty + tabH, selected ? 0xB03A5368 : hovered ? ROW_HOVER : ROW);
            if (floating) {
                graphics.fill(tx + tw - 3, ty, tx + tw, ty + tabH, ACCENT);
            }
            drawCentered(graphics, tabIcon(tabs[i]), tx, ty + 10, tw, selected ? ACCENT : TEXT);
            renderedTabs.add(new TabButton(tabs[i], tx, ty, tw, tabH));
            ty += tabH + gap;
        }
    }

    private String tabIcon(ParticleEditorTab targetTab) {
        return switch (targetTab) {
            case EMITTER -> "S";
            case MOTION -> "M";
            case APPEARANCE -> "V";
            case EFFECTS -> "Fx";
        };
    }

    private void renderProjectPicker(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.fill(0, 0, width, height, 0x99000000);
        int pickerW = Math.min(PROJECT_PICKER_WIDTH, Math.max(240, width - 32));
        int pickerH = PROJECT_PICKER_HEIGHT;
        int x = (width - pickerW) / 2;
        int y = (height - pickerH) / 2;
        graphics.fill(x, y, x + pickerW, y + pickerH, PANEL);
        graphics.fill(x, y, x + pickerW, y + 30, PANEL_LIGHT);
        graphics.drawString(font, editorText("dialog.project", "Particle Project"), x + 10, y + 11, TEXT, false);

        graphics.drawString(font, editorText("dialog.new_file", "New File"), x + 10, y + 40, ACCENT, false);
        renderTextBox(graphics, x + 10, y + 55, pickerW - 122, newProjectName, editingProjectName, mouseX, mouseY, () -> editingProjectName = true);
        renderAction(graphics, x + pickerW - 102, y + 55, 92, editorText("action.create", "Create"), mouseX, mouseY, this::createFreshProject);

        graphics.drawString(font, editorText("dialog.projects_folder", "Projects Folder"), x + 10, y + 84, ACCENT, false);
        renderFolderPath(graphics, x + 10, y + 99, pickerW - 20, mouseX, mouseY, this::openProjectsFolder);
        int thirdW = (pickerW - 28) / 3;
        renderAction(graphics, x + 10, y + 121, thirdW, editorText("action.open_folder", "Open Folder"), mouseX, mouseY, this::openProjectsFolder);
        renderAction(graphics, x + 14 + thirdW, y + 121, thirdW, editorText("action.load", "Load"), mouseX, mouseY, this::openProjectFolder);
        renderAction(graphics, x + 18 + thirdW * 2, y + 121, thirdW, editorText("action.continue", "Continue"), mouseX, mouseY, this::continueCurrentProject);
    }

    private void renderLoadPicker(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.fill(0, 0, width, height, 0xAA000000);
        int pickerW = Math.min(520, Math.max(280, width - 32));
        int pickerH = Math.min(300, Math.max(210, height - 32));
        int x = (width - pickerW) / 2;
        int y = (height - pickerH) / 2;
        graphics.fill(x, y, x + pickerW, y + pickerH, PANEL);
        graphics.fill(x, y, x + pickerW, y + 30, PANEL_LIGHT);
        graphics.drawString(font, editorText("dialog.load_particle_files", "Load Particle Files"), x + 10, y + 11, TEXT, false);

        int listX = x + 10;
        int listY = y + 38;
        int listW = pickerW - 20;
        int listH = pickerH - 72;
        int visibleRows = Math.max(1, listH / (ROW_HEIGHT + ROW_GAP));
        loadPickerScroll = Mth.clamp(loadPickerScroll, 0, Math.max(0, loadPickerProjects.size() - visibleRows));

        graphics.enableScissor(listX, listY, listX + listW, listY + listH);
        int rowY = listY;
        for (int visible = 0; visible < visibleRows; visible++) {
            int index = loadPickerScroll + visible;
            if (index >= loadPickerProjects.size() || rowY + ROW_HEIGHT > listY + listH) {
                break;
            }
            ParticleEditorStorage.LoadedProject project = loadPickerProjects.get(index);
            boolean selected = loadPickerSelection.contains(index);
            boolean hovered = inBounds(mouseX, mouseY, listX, rowY, listW, ROW_HEIGHT);
            graphics.fill(listX, rowY, listX + listW, rowY + ROW_HEIGHT, hovered ? ROW_HOVER : ROW);
            int deleteW = 18;
            int deleteX = listX + listW - deleteW - 2;
            int boxX = listX + 5;
            graphics.fill(boxX, rowY + 3, boxX + 12, rowY + 15, selected ? 0xB065533A : 0x80404A5F);
            drawCentered(graphics, selected ? "x" : "", boxX, rowY + 6, 12, selected ? WARNING : MUTED);
            String path = ParticleEditorStorage.root(Minecraft.getInstance()).relativize(project.path()).toString();
            String label = trimToWidth(project.name() + "  " + path, listW - 50);
            graphics.drawString(font, label, listX + 23, rowY + 5, selected ? ACCENT : TEXT, false);
            int capturedIndex = index;
            boolean deleteHovered = inBounds(mouseX, mouseY, deleteX, rowY + 2, deleteW, ROW_HEIGHT - 4);
            graphics.fill(deleteX, rowY + 2, deleteX + deleteW, rowY + ROW_HEIGHT - 2, deleteHovered ? 0xA0643A45 : 0x80404A5F);
            drawCentered(graphics, "x", deleteX, rowY + 6, deleteW, deleteHovered ? WARNING : MUTED);
            clickZones.add(new ClickZone(listX, rowY, listW - deleteW - 4, ROW_HEIGHT, () -> toggleLoadSelection(capturedIndex)));
            clickZones.add(new ClickZone(deleteX, rowY + 2, deleteW, ROW_HEIGHT - 4, () -> deleteLoadedProject(capturedIndex)));
            rowY += ROW_HEIGHT + ROW_GAP;
        }
        graphics.flush();
        graphics.disableScissor();

        int buttonY = y + pickerH - 25;
        int gap = 4;
        int buttonW = (pickerW - 20 - gap * 4) / 5;
        renderAction(graphics, x + 10, buttonY, buttonW, editorText("action.load", "Load"), mouseX, mouseY, () -> loadSelectedProjects(false));
        renderAction(graphics, x + 10 + (buttonW + gap), buttonY, buttonW, editorText("action.add", "Add"), mouseX, mouseY, () -> loadSelectedProjects(true));
        renderAction(graphics, x + 10 + (buttonW + gap) * 2, buttonY, buttonW, editorText("action.all", "All"), mouseX, mouseY, this::selectAllLoadProjects);
        renderAction(graphics, x + 10 + (buttonW + gap) * 3, buttonY, buttonW, editorText("action.none", "None"), mouseX, mouseY, loadPickerSelection::clear);
        renderAction(graphics, x + 10 + (buttonW + gap) * 4, buttonY, pickerW - 20 - (buttonW + gap) * 4, editorText("action.cancel", "Cancel"), mouseX, mouseY, this::closeLoadPicker);
    }

    private void renderTextBox(GuiGraphics graphics, int x, int y, int w, String value, boolean active, int mouseX, int mouseY, Runnable action) {
        boolean hovered = inBounds(mouseX, mouseY, x, y, w, 17);
        graphics.fill(x, y, x + w, y + 17, active ? 0xB065533A : hovered ? ROW_HOVER : ROW);
        String text = trimToWidth(value + (active ? "_" : ""), w - 8);
        graphics.drawString(font, text, x + 5, y + 5, active ? WARNING : TEXT, false);
        clickZones.add(new ClickZone(x, y, w, 17, action));
    }

    private void renderFolderPath(GuiGraphics graphics, int x, int y, int w, int mouseX, int mouseY, Runnable action) {
        boolean hovered = inBounds(mouseX, mouseY, x, y, w, 17);
        graphics.fill(x, y, x + w, y + 17, hovered ? ROW_HOVER : ROW);
        graphics.drawString(font, trimToWidth(ParticleEditorStorage.root(Minecraft.getInstance()).toString(), w - 8), x + 5, y + 5, hovered ? ACCENT : MUTED, false);
        clickZones.add(new ClickZone(x, y, w, 17, action));
    }

    private void renderFloatingPanels(GuiGraphics graphics, int mouseX, int mouseY) {
        for (FloatingTabPanel panel : floatingPanels) {
            clampFloatingPanel(panel);
            graphics.fill(panel.x, panel.y, panel.x + panel.w, panel.y + panel.h, PANEL);
            graphics.fill(panel.x, panel.y, panel.x + panel.w, panel.y + FloatingTabPanel.HEADER_HEIGHT, PANEL_LIGHT);
            String title = panel.module != null ? moduleLabel(panel.module) : panel.categoryLabel != null ? panel.categoryLabel : panel.tab.label;
            graphics.drawString(font, trimToWidth(title, Math.max(24, panel.w - FloatingTabPanel.DOCK_WIDTH - 22)), panel.x + 8, panel.y + 8, TEXT, false);

            boolean dockHovered = panel.dockContains(mouseX, mouseY);
            graphics.fill(panel.dockX(), panel.y + 5, panel.dockX() + FloatingTabPanel.DOCK_WIDTH, panel.y + 22, dockHovered ? ROW_HOVER : ROW);
            drawCentered(graphics, editorText("action.dock", "Dock"), panel.dockX(), panel.y + 10, FloatingTabPanel.DOCK_WIDTH, dockHovered ? ACCENT : TEXT);

            int bodyX = panel.x + 8;
            int bodyY = panel.y + FloatingTabPanel.HEADER_HEIGHT + 8;
            int bodyW = panel.w - 16;
            int bodyH = panel.h - FloatingTabPanel.HEADER_HEIGHT - CONTENT_BOTTOM_PADDING;
            if (panel.module != null) {
                List<Entry> entries = buildModuleEntries(panel.module);
                panel.scroll = renderEntryList(graphics, entries, bodyX, bodyY, bodyW, Math.max(40, bodyH), mouseX, mouseY, panel.scroll, panel);
                panel.contentHeight = measureContentHeight(entries);
            } else if (panel.categoryBuilder != null) {
                List<Entry> entries = new ArrayList<>();
                panel.categoryBuilder.accept(entries);
                panel.scroll = renderEntryList(graphics, entries, bodyX, bodyY, bodyW, Math.max(40, bodyH), mouseX, mouseY, panel.scroll, panel);
                panel.contentHeight = measureContentHeight(entries);
            } else {
                panel.scroll = renderEntries(graphics, panel.tab, bodyX, bodyY, bodyW, Math.max(40, bodyH), mouseX, mouseY, panel.scroll, panel);
            }
            renderResizeHandle(graphics, panel.x, panel.y, panel.w, panel.h, panel.resizeHandleContains(mouseX, mouseY), resizingFloatingPanel == panel);
        }
    }

    private void renderEntries(GuiGraphics graphics, int x, int y, int w, int h, int mouseX, int mouseY) {
        scroll = renderEntries(graphics, tab, x, y, w, h, mouseX, mouseY, scroll, null);
    }

    private int renderEntries(GuiGraphics graphics, ParticleEditorTab targetTab, int x, int y, int w, int h, int mouseX, int mouseY, int scrollValue, FloatingTabPanel owner) {
        List<Entry> entries = buildEntries(targetTab);
        scrollValue = renderEntryList(graphics, entries, x, y, w, h, mouseX, mouseY, scrollValue, owner);
        if (owner == null) {
            contentHeight = measureContentHeight(entries);
            mainEntriesViewportHeight = h;
        } else {
            owner.contentHeight = measureContentHeight(entries);
        }
        return scrollValue;
    }

    private int renderEntryList(GuiGraphics graphics, List<Entry> entries, int x, int y, int w, int h, int mouseX, int mouseY, int scrollValue, FloatingTabPanel owner) {
        int measuredContentHeight = measureContentHeight(entries);
        scrollValue = clampScroll(scrollValue, measuredContentHeight, h);
        int viewportTop = y;
        int viewportBottom = y + h;
        int rowY = y - scrollValue;

        graphics.enableScissor(x, viewportTop, x + w, viewportBottom);
        for (Entry entry : entries) {
            int entryHeight = entryHeight(entry);
            if (rowY + entryHeight >= viewportTop && rowY <= viewportBottom) {
                renderEntry(graphics, entry, x, rowY, w, entryHeight, mouseX, mouseY, owner, viewportTop, viewportBottom);
            }
            rowY += entryHeight + ROW_GAP;
        }
        graphics.flush();
        graphics.disableScissor();
        return scrollValue;
    }

    private int measureContentHeight(List<Entry> entries) {
        int height = 0;
        for (Entry entry : entries) {
            height += entryHeight(entry) + ROW_GAP;
        }
        return Math.max(0, height);
    }

    private int entryHeight(Entry entry) {
        return entry.header ? 16 : ROW_HEIGHT;
    }

    private void renderEntry(GuiGraphics graphics, Entry entry, int x, int y, int w, int h, int mouseX, int mouseY, FloatingTabPanel owner, int clipTop, int clipBottom) {
        if (entry.header) {
            boolean hovered = inBounds(mouseX, mouseY, x, Math.max(y, clipTop), w, Math.min(y + h, clipBottom) - Math.max(y, clipTop));
            graphics.fill(x, y, x + w, y + h, hovered ? 0x70455A72 : 0x40151B27);
            boolean categoryCanDetach = entry instanceof CategoryEntry category && category.builder != null;
            boolean optionalCanDetach = entry instanceof OptionalCategoryEntry optional && optional.canDetach();
            boolean categoryCanLink = entry instanceof CategoryEntry linkCategory && isScaleLengthLinkKey(linkCategory.key);
            int detachW = categoryCanDetach || optionalCanDetach ? 18 : 0;
            int linkW = categoryCanLink ? 18 : 0;
            graphics.drawString(font, entry.value(), x + 6, y + 5, hovered ? WARNING : MUTED, false);
            graphics.drawString(font, trimToWidth(entry.label, w - 26 - detachW - linkW), x + 20, y + 5, hovered ? WARNING : ACCENT, false);
            if (categoryCanLink) {
                int linkX = x + w - detachW - linkW - 3;
                int linkH = Math.min(14, Math.max(1, Math.min(y + h, clipBottom) - Math.max(y + 1, clipTop)));
                int linkY = Math.max(y + 1, clipTop);
                boolean linkHovered = inBounds(mouseX, mouseY, linkX, linkY, linkW, linkH);
                graphics.fill(linkX, linkY, linkX + linkW, linkY + linkH, linkHovered ? ROW_HOVER : 0x80404A5F);
                drawMagnetIcon(graphics, linkX, linkY, linkW, linkH, state.scaleLengthLinked, linkHovered ? ACCENT : TEXT);
                clickZones.add(new ClickZone(linkX, linkY, linkW, linkH, this::toggleScaleLengthLink, owner));
            }
            if (categoryCanDetach || optionalCanDetach) {
                int detachX = x + w - detachW - 2;
                int detachH = Math.min(14, Math.max(1, Math.min(y + h, clipBottom) - Math.max(y + 1, clipTop)));
                int detachY = Math.max(y + 1, clipTop);
                boolean detachHovered = inBounds(mouseX, mouseY, detachX, detachY, detachW, detachH);
                graphics.fill(detachX, detachY, detachX + detachW, detachY + detachH, detachHovered ? ROW_HOVER : 0x80404A5F);
                drawVerticalDots(graphics, detachX, detachY, detachW, detachH, detachHovered ? ACCENT : TEXT);
                if (entry instanceof OptionalCategoryEntry optional) {
                    clickZones.add(new ClickZone(detachX, detachY, detachW, detachH, () -> detachModule(optional.module, x + w, y), owner));
                } else if (entry instanceof CategoryEntry category) {
                    clickZones.add(new ClickZone(detachX, detachY, detachW, detachH, () -> detachCategory(category.key, category.label, category.builder, x + w, y), owner));
                }
            }
            renderedEntries.add(new RenderedEntry(entry, x, y, w, h, owner, clipTop, clipBottom));
            return;
        }
        if (entry instanceof ColorChannelsEntry colorChannelsEntry) {
            renderColorChannelsEntry(graphics, colorChannelsEntry, x, y, w, h, mouseX, mouseY, owner, clipTop, clipBottom);
            return;
        }
        if (entry instanceof ShapeSizeEntry shapeSizeEntry) {
            renderShapeSizeEntry(graphics, shapeSizeEntry, x, y, w, h, mouseX, mouseY, owner, clipTop, clipBottom);
            return;
        }

        boolean hovered = inBounds(mouseX, mouseY, x, Math.max(y, clipTop), w, Math.min(y + h, clipBottom) - Math.max(y, clipTop));
        boolean editing = editingEntry != null && editingEntry.label.equals(entry.label);
        graphics.fill(x, y, x + w, y + h, editing ? 0xB065533A : hovered ? ROW_HOVER : ROW);

        String value = editing ? editingText + "_" : entry.value();
        int rightPadding = entry.adjustable() ? CONTROL_SIZE * 2 + CONTROL_GAP * 3 + 6 : 6;
        int availableTextW = Math.max(24, w - rightPadding - 12);
        int valueW = Math.max(28, Math.min(availableTextW / 2, font.width(value) + 12));
        int labelW = Math.max(28, availableTextW - valueW - 8);
        String label = trimToWidth(entry.label, labelW);
        graphics.drawString(font, label, x + 6, y + 5, TEXT, false);
        String trimmed = trimToWidth(value, valueW - 8);
        int valueRight = x + w - rightPadding;
        int valueX = valueRight - font.width(trimmed);
        if (w < 390) {
            int compactX = x + 6 + font.width(label) + 8;
            valueX = Math.min(valueX, compactX);
            valueX = Math.min(valueX, valueRight - font.width(trimmed));
        }
        graphics.drawString(font, trimmed, valueX, y + 5, entry.editable ? WARNING : MUTED, false);
        renderColorSwatch(graphics, value, valueX - 14, y + 4, h);
        if (entry.adjustable()) {
            int minusX = x + w - CONTROL_SIZE * 2 - CONTROL_GAP * 2;
            int plusX = x + w - CONTROL_SIZE - CONTROL_GAP;
            boolean minusHovered = inBounds(mouseX, mouseY, minusX, y + 1, CONTROL_SIZE, h - 2);
            boolean plusHovered = inBounds(mouseX, mouseY, plusX, y + 1, CONTROL_SIZE, h - 2);
            graphics.fill(minusX, y + 1, minusX + CONTROL_SIZE, y + h - 1, minusHovered ? ROW_HOVER : 0x80404A5F);
            graphics.fill(plusX, y + 1, plusX + CONTROL_SIZE, y + h - 1, plusHovered ? ROW_HOVER : 0x80404A5F);
            drawCentered(graphics, "-", minusX, y + 5, CONTROL_SIZE, minusHovered ? ACCENT : TEXT);
            drawCentered(graphics, "+", plusX, y + 5, CONTROL_SIZE, plusHovered ? ACCENT : TEXT);
        }
        renderedEntries.add(new RenderedEntry(entry, x, y, w, h, owner, clipTop, clipBottom));
    }

    private void renderColorChannelsEntry(GuiGraphics graphics, ColorChannelsEntry entry, int x, int y, int w, int h, int mouseX, int mouseY, FloatingTabPanel owner, int clipTop, int clipBottom) {
        boolean hovered = inBounds(mouseX, mouseY, x, Math.max(y, clipTop), w, Math.min(y + h, clipBottom) - Math.max(y, clipTop));
        graphics.fill(x, y, x + w, y + h, hovered ? ROW_HOVER : ROW);
        graphics.drawString(font, trimToWidth(entry.label, Math.max(40, w - colorChannelAreaWidth(w) - 18)), x + 6, y + 5, TEXT, false);

        int areaW = colorChannelAreaWidth(w);
        int segmentW = (areaW - 6) / 3;
        int startX = x + w - areaW - 6;
        for (int channel = 0; channel < 3; channel++) {
            int sx = startX + channel * (segmentW + 3);
            boolean channelHovered = inBounds(mouseX, mouseY, sx, y + 1, segmentW, h - 2);
            int background = colorChannelBackground(channel, channelHovered);
            graphics.fill(sx, y + 1, sx + segmentW, y + h - 1, background);
            String label = entry.channelLabel(channel);
            String value = editingEntry != null && editingEntry.label.equals(label) ? editingText + "_" : String.valueOf(entry.channelValue(channel));
            drawCentered(graphics, colorChannelLabel(channel) + " " + trimToWidth(value, Math.max(8, segmentW - 14)), sx, y + 5, segmentW, colorChannelText(channel));
        }
        renderedEntries.add(new RenderedEntry(entry, x, y, w, h, owner, clipTop, clipBottom));
    }

    private void renderShapeSizeEntry(GuiGraphics graphics, ShapeSizeEntry entry, int x, int y, int w, int h, int mouseX, int mouseY, FloatingTabPanel owner, int clipTop, int clipBottom) {
        boolean hovered = inBounds(mouseX, mouseY, x, Math.max(y, clipTop), w, Math.min(y + h, clipBottom) - Math.max(y, clipTop));
        graphics.fill(x, y, x + w, y + h, hovered ? ROW_HOVER : ROW);
        graphics.drawString(font, trimToWidth(entry.label, Math.max(40, w - shapeSizeAreaWidth(w, entry.axisCount()) - 18)), x + 6, y + 5, TEXT, false);

        int areaW = shapeSizeAreaWidth(w, entry.axisCount());
        int segmentW = (areaW - (entry.axisCount() - 1) * 3) / entry.axisCount();
        int startX = x + w - areaW - 6;
        for (int axis = 0; axis < entry.axisCount(); axis++) {
            int sx = startX + axis * (segmentW + 3);
            boolean axisHovered = inBounds(mouseX, mouseY, sx, y + 1, segmentW, h - 2);
            graphics.fill(sx, y + 1, sx + segmentW, y + h - 1, shapeAxisBackground(axis, axisHovered));
            String label = entry.axisEntryLabel(axis);
            String value = editingEntry != null && editingEntry.label.equals(label) ? editingText + "_" : format(entry.axisValue(axis));
            drawCentered(graphics, entry.axisLabel(axis) + " " + trimToWidth(value, Math.max(8, segmentW - 14)), sx, y + 5, segmentW, axisHovered ? WARNING : TEXT);
        }
        renderedEntries.add(new RenderedEntry(entry, x, y, w, h, owner, clipTop, clipBottom));
    }

    private List<Entry> buildEntries(ParticleEditorTab targetTab) {
        List<Entry> entries = new ArrayList<>();
        ParticleEditorTab previousBuildingTab = buildingTab;
        buildingTab = targetTab;
        try {
            switch (targetTab) {
                case EMITTER -> {
                    if (!projectBrowserVisible) {
                        addProjectEntries(entries);
                    }
                    addEmitterEntries(entries);
                }
                case MOTION -> addMotionEntries(entries);
                case APPEARANCE -> addAppearanceEntries(entries);
                case EFFECTS -> addEffectsEntries(entries);
            }
        } finally {
            buildingTab = previousBuildingTab;
        }
        return entries;
    }

    private void refreshCategoryDefinitions() {
        categoryDefinitions.clear();
        for (ParticleEditorTab targetTab : ParticleEditorTab.values()) {
            buildEntries(targetTab);
        }
    }

    private void addProjectEntries(List<Entry> entries) {
        section(entries, "projects", "section.projects", "Projects", section -> {
            section.add(displayEntry(editorText("entry.active_project", "Active Project"), (activeProjectIndex + 1) + "/" + projects.size() + " " + activeProject().name));
            section.add(actionEntry(editorText("action.previous_project", "Previous Project"), () -> selectProject(activeProjectIndex - 1)));
            section.add(actionEntry(editorText("action.next_project", "Next Project"), () -> selectProject(activeProjectIndex + 1)));
            section.add(actionEntry(editorText("action.add_project", "Add Project"), this::addProject));
            section.add(actionEntry(editorText("action.save_projects", "Save Projects"), this::saveAllProjects));
        });
    }

    private void addEmitterEntries(List<Entry> entries) {
        section(entries, "source", "section.source", "Source", section -> {
            section.add(enumEntry(editorText("entry.output_mode", "Output Mode"), () -> state.outputMode, value -> state.outputMode = value, ParticleOutputMode.values(), value -> value.label));
            if (state.outputMode == ParticleOutputMode.SCREEN) {
                section.add(enumEntry(editorText("entry.screen_particle", "Screen Particle"), () -> state.screenParticle, value -> state.screenParticle = value, ScreenParticlePreset.values(), value -> value.label));
            } else {
                section.add(enumEntry(editorText("entry.particle", "Particle"), () -> state.particle, value -> state.particle = value, ParticlePreset.values(), value -> value.label));
            }
        });

        section(entries, "render", "section.render", "Render", section -> {
            if (state.outputMode == ParticleOutputMode.SCREEN) {
                section.add(enumEntry(editorText("entry.screen_render_type", "Screen Render Type"), () -> state.screenRenderMode, value -> state.screenRenderMode = value, ScreenRenderMode.values(), value -> value.label));
                section.add(doubleEntry(editorText("entry.screen_x", "Screen X"), () -> state.screenX, value -> state.screenX = value, -4096.0, 4096.0, 1.0));
                section.add(doubleEntry(editorText("entry.screen_y", "Screen Y"), () -> state.screenY, value -> state.screenY = value, -4096.0, 4096.0, 1.0));
                section.add(boolEntry(editorText("entry.track_stack", "Track Stack"), () -> state.screenTrackStack, value -> state.screenTrackStack = value));
                if (state.screenTrackStack) {
                    section.add(doubleEntry(editorText("entry.stack_x_offset", "Stack X Offset"), () -> state.stackTrackXOffset, value -> state.stackTrackXOffset = value, -512.0, 512.0, 1.0));
                    section.add(doubleEntry(editorText("entry.stack_y_offset", "Stack Y Offset"), () -> state.stackTrackYOffset, value -> state.stackTrackYOffset = value, -512.0, 512.0, 1.0));
                }
            } else {
                section.add(enumEntry(editorText("entry.render_type", "Render Type"), () -> state.renderMode, value -> state.renderMode = value, RenderMode.values(), value -> value.label));
                section.add(boolEntry(editorText("entry.depth_fade", "Depth Fade"), () -> state.depthFade, value -> state.depthFade = value));
                section.add(enumEntry(editorText("entry.render_layer", "Render Layer"), () -> state.renderLayer, value -> state.renderLayer = value, RenderLayerMode.values(), value -> value.label));
                section.add(enumEntry(editorText("entry.behavior", "Behavior"), () -> state.behavior, value -> state.behavior = value, BehaviorMode.values(), value -> value.label));
            }
            section.add(enumEntry(editorText("entry.sprite_picker", "Sprite Picker"), () -> state.spritePicker, value -> state.spritePicker = value, SimpleParticleOptions.ParticleSpritePicker.values(), Enum::name));
            if (state.outputMode == ParticleOutputMode.WORLD) {
                section.add(enumEntry(editorText("entry.lighting", "Lighting"), () -> state.lightMode, value -> state.lightMode = value, LightMode.values(), value -> value.label));
            }
            if (state.outputMode == ParticleOutputMode.WORLD && state.lightMode == LightMode.CUSTOM) {
                section.add(intEntry(editorText("entry.light_value", "Light Value"), () -> state.lightLevel, value -> state.lightLevel = value, -1, RenderHelper.FULL_BRIGHT, 16));
            }
        });

        section(entries, "lifetime", "section.lifetime", "Lifetime", section -> {
            if (state.outputMode == ParticleOutputMode.WORLD) {
                section.add(boolEntry(editorText("entry.force_spawn", "Force Spawn"), () -> state.forceSpawn, value -> state.forceSpawn = value));
                section.add(boolEntry(editorText("entry.no_clip", "No Clip"), () -> state.noClip, value -> state.noClip = value));
            }
            section.add(intEntry(editorText("entry.lifetime", "Lifetime"), () -> state.lifetime, value -> state.lifetime = value, 1, 6000, 1));
            section.add(intEntry(editorText("entry.life_delay", "Life Delay"), () -> state.lifeDelay, value -> state.lifeDelay = value, 0, 6000, 1));
            section.add(floatEntry(editorText("entry.lifetime_modifier", "Lifetime Modifier"), () -> state.lifetimeModifier, value -> state.lifetimeModifier = value, 0.0f, 8.0f, 0.05f));
            section.add(floatEntry(editorText("entry.life_delay_modifier", "Life Delay Modifier"), () -> state.lifeDelayModifier, value -> state.lifeDelayModifier = value, 0.0f, 8.0f, 0.05f));
        });

        section(entries, "preview", "section.preview", "Preview", section -> section.addAll(buildPreviewEntries()));
    }

    private List<Entry> buildPreviewEntries() {
        List<Entry> entries = new ArrayList<>();
        entries.add(boolEntry(editorText("entry.live_preview", "Live Preview"), () -> state.autoPreview, value -> state.autoPreview = value));
        entries.add(enumEntry(editorText("entry.live_preview_scope", "Live Scope"), () -> livePreviewScope, value -> livePreviewScope = value, LivePreviewScope.values(), this::livePreviewScopeLabel));
        entries.add(enumEntry(editorText("entry.preview_playback", "Playback"), () -> state.previewPlayback, value -> state.previewPlayback = value, PreviewPlaybackMode.values(), this::previewPlaybackLabel));
        entries.add(intEntry(editorText("entry.spawn_count", "Spawn Count"), () -> state.spawnCount, value -> state.spawnCount = value, 1, 512, 1));
        if (state.previewPlayback == PreviewPlaybackMode.LOOPING) {
            entries.add(intEntry(editorText("entry.emit_interval", "Emit Interval"), () -> state.previewInterval, value -> state.previewInterval = value, 1, 200, 1));
        }
        entries.add(enumEntry(editorText("entry.preview_mode", "Preview Mode"), () -> state.followCamera ? PreviewPositionMode.CAMERA : PreviewPositionMode.FIXED, value -> {
            state.followCamera = value == PreviewPositionMode.CAMERA;
            if (!state.followCamera) {
                state.setFixedPreviewPosition(calculateCameraPreviewPosition());
            }
        }, PreviewPositionMode.values(), this::previewPositionLabel));
        if (!state.followCamera) {
            entries.add(actionEntry(editorText("entry.move_fixed_point", "Move Fixed Point"), () -> {
                state.setFixedPreviewPosition(calculateCameraPreviewPosition());
                refreshLivePreview();
            }));
            entries.add(doubleEntry(editorText("entry.fixed_x", "Fixed X"), () -> state.fixedPreviewX, value -> state.fixedPreviewX = value, -30000000.0, 30000000.0, 0.05));
            entries.add(doubleEntry(editorText("entry.fixed_y", "Fixed Y"), () -> state.fixedPreviewY, value -> state.fixedPreviewY = value, -2048.0, 2048.0, 0.05));
            entries.add(doubleEntry(editorText("entry.fixed_z", "Fixed Z"), () -> state.fixedPreviewZ, value -> state.fixedPreviewZ = value, -30000000.0, 30000000.0, 0.05));
        }
        entries.add(doubleEntry(editorText("entry.preview_distance", "Preview Distance"), () -> state.previewDistance, value -> state.previewDistance = value, 0.25, 32.0, 0.25));
        entries.add(doubleEntry(editorText("entry.preview_y_offset", "Preview Y Offset"), () -> state.previewYOffset, value -> state.previewYOffset = value, -16.0, 16.0, 0.1));
        entries.add(actionEntry(editorText("action.spawn_burst", "Spawn Burst"), this::spawnPreviewWithEffects));
        entries.add(actionEntry(editorText("action.spawn_all_projects", "Spawn All Projects"), this::spawnAllPreviews));
        return entries;
    }

    private void addMotionEntries(List<Entry> entries) {
        section(entries, "motion.velocity", "section.velocity", "Velocity", section -> {
            section.add(doubleEntry(editorText("entry.motion_x", "Motion X"), () -> state.motionX, value -> state.motionX = value, -8.0, 8.0, 0.01));
            section.add(doubleEntry(editorText("entry.motion_y", "Motion Y"), () -> state.motionY, value -> state.motionY = value, -8.0, 8.0, 0.01));
            if (state.outputMode == ParticleOutputMode.WORLD) {
                section.add(doubleEntry(editorText("entry.motion_z", "Motion Z"), () -> state.motionZ, value -> state.motionZ = value, -8.0, 8.0, 0.01));
            }
        });

        section(entries, "motion.randomness", "section.randomness", "Randomness", section -> {
            section.add(doubleEntry(editorText("entry.random_motion_x", "Random Motion X"), () -> state.randomMotionX, value -> state.randomMotionX = value, 0.0, 8.0, 0.01));
            section.add(doubleEntry(editorText("entry.random_motion_y", "Random Motion Y"), () -> state.randomMotionY, value -> state.randomMotionY = value, 0.0, 8.0, 0.01));
            if (state.outputMode == ParticleOutputMode.WORLD) {
                section.add(doubleEntry(editorText("entry.random_motion_z", "Random Motion Z"), () -> state.randomMotionZ, value -> state.randomMotionZ = value, 0.0, 8.0, 0.01));
            }
        });

        section(entries, "motion.spawn_area", "section.spawn_area", "Spawn Area", section -> {
            section.add(enumEntry(editorText("entry.spawn_shape", "Spawn Shape"), () -> state.spawnDistribution, value -> state.spawnDistribution = value, SpawnDistributionMode.values(), this::spawnDistributionLabel));
            section.add(boolEntry(editorText("entry.shape_overlay", "Shape Overlay"), () -> state.shapeOverlay, value -> state.shapeOverlay = value));
            if (state.spawnDistribution == SpawnDistributionMode.BUILDER_RANDOM) {
                section.add(doubleEntry(editorText("entry.builder_offset_x", "Builder Offset X"), () -> state.randomOffsetX, value -> state.randomOffsetX = value, 0.0, 16.0, 0.01));
                section.add(doubleEntry(editorText("entry.builder_offset_y", "Builder Offset Y"), () -> state.randomOffsetY, value -> state.randomOffsetY = value, 0.0, 16.0, 0.01));
                if (state.outputMode == ParticleOutputMode.WORLD) {
                    section.add(doubleEntry(editorText("entry.builder_offset_z", "Builder Offset Z"), () -> state.randomOffsetZ, value -> state.randomOffsetZ = value, 0.0, 16.0, 0.01));
                }
            } else if (state.spawnDistribution != SpawnDistributionMode.POINT) {
                section.add(shapeSizeEntry(editorText("entry.shape_size", "Shape Size")));
                section.add(doubleEntry(editorText("entry.shape_uniform", "Shape Uniform"), this::shapeUniformSize, this::setShapeUniformSize, 0.0, 64.0, 0.05));
                section.add(doubleEntry(editorText("entry.shape_x", "Shape X"), () -> state.shapeSizeX, value -> state.shapeSizeX = value, 0.0, 64.0, 0.05));
                if (shapeUsesY()) {
                    section.add(doubleEntry(editorText("entry.shape_y", "Shape Y"), () -> state.shapeSizeY, value -> state.shapeSizeY = value, 0.0, 64.0, 0.05));
                }
                if (shapeUsesZ()) {
                    section.add(doubleEntry(editorText("entry.shape_z", "Shape Z"), () -> state.shapeSizeZ, value -> state.shapeSizeZ = value, 0.0, 64.0, 0.05));
                }
            }
        });

        section(entries, "motion.physics", "section.physics", "Physics", section -> {
            section.add(floatEntry(editorText("entry.gravity", "Gravity"), () -> state.gravity, value -> state.gravity = value, -4.0f, 4.0f, 0.01f));
            section.add(floatEntry(editorText("entry.friction", "Friction"), () -> state.friction, value -> state.friction = value, 0.0f, 2.0f, 0.01f));
            section.add(floatEntry(editorText("entry.gravity_modifier", "Gravity Modifier"), () -> state.gravityModifier, value -> state.gravityModifier = value, 0.0f, 8.0f, 0.05f));
            section.add(floatEntry(editorText("entry.friction_modifier", "Friction Modifier"), () -> state.frictionModifier, value -> state.frictionModifier = value, 0.0f, 8.0f, 0.05f));
        });

        if (state.outputMode == ParticleOutputMode.SCREEN) {
            return;
        }
        section(entries, "motion.behavior_data", "section.behavior_data", "Behavior Data", section -> {
            section.add(boolEntry(editorText("entry.forced_direction", "Forced Direction"), () -> state.forcedDirection, value -> state.forcedDirection = value));
            if (state.forcedDirection) {
                section.add(doubleEntry(editorText("entry.direction_x", "Direction X"), () -> state.directionX, value -> state.directionX = value, -1.0, 1.0, 0.05));
                section.add(doubleEntry(editorText("entry.direction_y", "Direction Y"), () -> state.directionY, value -> state.directionY = value, -1.0, 1.0, 0.05));
                section.add(doubleEntry(editorText("entry.direction_z", "Direction Z"), () -> state.directionZ, value -> state.directionZ = value, -1.0, 1.0, 0.05));
            }
            if (state.behavior == BehaviorMode.SPARK) {
                section.add(floatEntry(editorText("entry.spark_length_center", "Spark Length Center"), () -> state.sparkLengthCenter, value -> state.sparkLengthCenter = value, -2.0f, 2.0f, 0.05f));
            }
        });
    }

    private void addAppearanceEntries(List<Entry> entries) {
        section(entries, "appearance.color", "section.color", "Color", this::addColorSectionEntries);
        section(entries, "curve.transparency", "section.transparency", "Transparency", section -> addCurveSectionEntries(section, "transparency", "Transparency", state.transparency, value -> state.transparencyDataEnabled = value, 0.0f, 1.0f, 0.025f));
        section(entries, "curve.scale", "section.scale", "Scale", section -> addLinkedCurveSectionEntries(section, "scale", "Scale", state.scale, value -> state.scaleDataEnabled = value, 0.0f, 16.0f, 0.025f));
        section(entries, "curve.length", "section.length", "Length", section -> addLinkedCurveSectionEntries(section, "length", "Length", state.length, value -> state.lengthDataEnabled = value, 0.0f, 32.0f, 0.025f));
        section(entries, "appearance.spin", "section.spin", "Spin", this::addSpinSectionEntries);
    }

    private void addEffectsEntries(List<Entry> entries) {
        optionalModuleSection(entries, EditorModule.SCREENSHAKE, this::addScreenshakeModuleEntries);
    }

    private void addColorSectionEntries(List<Entry> entries) {
        state.colorDataEnabled = true;
        addColorModuleEntries(entries);
    }

    private void addCurveSectionEntries(List<Entry> entries, String key, String label, ParticleCurveData curve, Consumer<Boolean> setter, float min, float max, float step) {
        setter.accept(true);
        addCurveFields(entries, key, label, curve, min, max, step);
    }

    private void addLinkedCurveSectionEntries(List<Entry> entries, String key, String label, ParticleCurveData curve, Consumer<Boolean> setter, float min, float max, float step) {
        setter.accept(true);
        if (state.scaleLengthLinked && isScaleLengthLinkKey(key)) {
            state.scaleDataEnabled = true;
            state.lengthDataEnabled = true;
            entries.add(floatEntry(editorText("entry.scale_length_value", "Scale / Length"), () -> state.scale.start, this::setScaleLengthLinkedValue, 0.0f, 32.0f, step));
            return;
        }
        entries.add(enumEntry(editorText("entry." + key + "_mode", label + " Mode"), () -> curve.linked ? CurveEditMode.SINGLE : CurveEditMode.CURVE, value -> {
            curve.linked = value == CurveEditMode.SINGLE;
            if (curve.linked) {
                curve.setLinkedValue(curve.start);
            }
        }, CurveEditMode.values(), this::curveEditModeLabel));
        if (curve.linked) {
            entries.add(floatEntry(editorText("entry." + key + "_value", label), () -> curve.start, value -> curve.setLinkedValue(value), min, max, step));
        } else {
            addCurveFields(entries, key, label, curve, min, max, step);
        }
    }

    private void setScaleLengthLinkedValue(float value) {
        state.scale.linked = true;
        state.length.linked = true;
        state.scale.setLinkedValue(value);
        state.length.setLinkedValue(value);
    }

    private boolean isScaleLengthLinkKey(String key) {
        return "curve.scale".equals(key) || "curve.length".equals(key) || "scale".equals(key) || "length".equals(key);
    }

    private void addSpinSectionEntries(List<Entry> entries) {
        state.spinDataEnabled = true;
        addSpinModuleEntries(entries);
    }

    private void addColorModuleEntries(List<Entry> entries) {
        entries.add(hexEntry(editorText("entry.start_hex", "Start Hex"), () -> state.startColor, value -> state.startColor = value));
        entries.add(colorChannelsEntry(editorText("entry.start_rgb", "Start RGB"), () -> state.startColor, value -> state.startColor = value));
        entries.add(hexEntry(editorText("entry.end_hex", "End Hex"), () -> state.endColor, value -> state.endColor = value));
        entries.add(colorChannelsEntry(editorText("entry.end_rgb", "End RGB"), () -> state.endColor, value -> state.endColor = value));
        entries.add(actionEntry(editorText("action.swap_colors", "Swap Colors"), () -> {
            int color = state.startColor;
            state.startColor = state.endColor;
            state.endColor = color;
        }));
        entries.add(actionEntry(editorText("action.copy_start_to_end", "Copy Start To End"), () -> state.endColor = state.startColor));
        entries.add(floatEntry(editorText("entry.color_coefficient", "Color Coefficient"), () -> state.colorCoefficient, value -> state.colorCoefficient = value, 0.0f, 8.0f, 0.05f));
        entries.add(easingEntry(editorText("entry.color_easing", "Color Easing"), () -> state.colorEasing, value -> state.colorEasing = value));
    }

    private void addSpinModuleEntries(List<Entry> entries) {
        entries.add(floatEntry(editorText("entry.spin_offset", "Spin Offset"), () -> state.spinOffset, value -> state.spinOffset = value, -Mth.TWO_PI, Mth.TWO_PI, 0.05f));
        addCurveFields(entries, "spin_speed", "Spin Speed", state.spin, -Mth.TWO_PI, Mth.TWO_PI, 0.025f);
    }

    private void addScreenshakeModuleEntries(List<Entry> entries) {
        entries.add(boolEntry(editorText("entry.shake_on_burst", "Shake On Burst"), () -> state.screenshakeOnBurst, value -> state.screenshakeOnBurst = value));
        entries.add(intEntry(editorText("entry.shake_duration", "Shake Duration"), () -> state.screenshakeDuration, value -> state.screenshakeDuration = value, 1, 1200, 1));
        entries.add(floatEntry(editorText("entry.shake_start", "Shake Start"), () -> state.screenshakeStartStrength, value -> state.screenshakeStartStrength = value, 0.0f, 10.0f, 0.05f));
        entries.add(floatEntry(editorText("entry.shake_middle", "Shake Middle"), () -> state.screenshakeMiddleStrength, value -> state.screenshakeMiddleStrength = value, 0.0f, 10.0f, 0.05f));
        entries.add(floatEntry(editorText("entry.shake_end", "Shake End"), () -> state.screenshakeEndStrength, value -> state.screenshakeEndStrength = value, 0.0f, 10.0f, 0.05f));
        entries.add(easingEntry(editorText("entry.shake_start_easing", "Shake Start Easing"), () -> state.screenshakeStartEasing, value -> state.screenshakeStartEasing = value));
        entries.add(easingEntry(editorText("entry.shake_end_easing", "Shake End Easing"), () -> state.screenshakeEndEasing, value -> state.screenshakeEndEasing = value));
        entries.add(floatEntry(editorText("entry.shake_coefficient", "Shake Coefficient"), () -> state.screenshakeCoefficient, value -> state.screenshakeCoefficient = value, 0.05f, 8.0f, 0.05f));
        if (state.outputMode == ParticleOutputMode.WORLD) {
            entries.add(boolEntry(editorText("entry.position_falloff", "Position Falloff"), () -> state.screenshakePositioned, value -> state.screenshakePositioned = value));
        }
        if (state.outputMode == ParticleOutputMode.WORLD && state.screenshakePositioned) {
            entries.add(floatEntry(editorText("entry.falloff_distance", "Falloff Distance"), () -> state.screenshakeFalloffDistance, value -> state.screenshakeFalloffDistance = value, 0.0f, 256.0f, 0.5f));
            entries.add(easingEntry(editorText("entry.falloff_easing", "Falloff Easing"), () -> state.screenshakeFalloffEasing, value -> state.screenshakeFalloffEasing = value));
        }
        entries.add(actionEntry(editorText("action.preview_screenshake", "Preview Screenshake"), this::playScreenshakePreview));
    }

    private void addCurveFields(List<Entry> entries, String key, String label, ParticleCurveData curve, float min, float max, float step) {
        entries.add(boolEntry(editorText("entry." + key + "_sync_endpoints", "Sync Start/End"), () -> curve.endpointsLinked, curve::setEndpointsLinked));
        entries.add(floatEntry(editorText("entry." + key + "_start", label + " Start"), () -> curve.start, value -> curve.setStart(value), min, max, step));
        entries.add(floatEntry(editorText("entry." + key + (curve.trinary ? "_middle" : "_end"), label + (curve.trinary ? " Middle" : " End")), () -> curve.middle, value -> {
            if (curve.trinary) {
                curve.middle = value;
            } else {
                curve.setFinal(value);
            }
        }, min, max, step));
        entries.add(boolEntry(editorText("entry." + key + "_trinary", label + " Trinary"), () -> curve.trinary, curve::setTrinary));
        if (curve.trinary) {
            entries.add(floatEntry(editorText("entry." + key + "_end", label + " End"), curve::finalValue, value -> curve.setFinal(value), min, max, step));
            entries.add(easingEntry(editorText("entry." + key + "_end_easing", label + " End Easing"), () -> curve.endEasing, value -> curve.endEasing = value));
        }
        entries.add(floatEntry(editorText("entry." + key + "_coefficient", label + " Coefficient"), () -> curve.coefficient, value -> curve.coefficient = value, 0.0f, 8.0f, 0.05f));
        entries.add(easingEntry(editorText("entry." + key + "_easing", label + " Easing"), () -> curve.startEasing, value -> curve.startEasing = value));
    }

    private Entry colorChannelsEntry(String label, IntSupplier getter, IntConsumer setter) {
        return new ColorChannelsEntry(label, getter, setter);
    }

    private Entry shapeSizeEntry(String label) {
        return new ShapeSizeEntry(label, state, this::format);
    }

    private boolean shapeUsesY() {
        return state.outputMode == ParticleOutputMode.SCREEN || state.spawnDistribution != SpawnDistributionMode.CIRCLE;
    }

    private boolean shapeUsesZ() {
        return state.outputMode == ParticleOutputMode.WORLD;
    }

    private double shapeUniformSize() {
        if (state.outputMode == ParticleOutputMode.WORLD && state.spawnDistribution == SpawnDistributionMode.CIRCLE) {
            return (state.shapeSizeX + state.shapeSizeZ) * 0.5;
        }
        if (state.outputMode == ParticleOutputMode.SCREEN) {
            return (state.shapeSizeX + state.shapeSizeY) * 0.5;
        }
        return (state.shapeSizeX + state.shapeSizeY + state.shapeSizeZ) / 3.0;
    }

    private void setShapeUniformSize(double value) {
        state.shapeSizeX = value;
        if (shapeUsesY()) {
            state.shapeSizeY = value;
        }
        if (shapeUsesZ()) {
            state.shapeSizeZ = value;
        }
    }

    private String previewPlaybackLabel(PreviewPlaybackMode value) {
        return editorText("preview_playback." + value.label, value.label);
    }

    private String curveEditModeLabel(CurveEditMode value) {
        return editorText("curve_mode." + value.label, value.label);
    }

    private void toggleScaleLengthLink() {
        state.scaleLengthLinked = !state.scaleLengthLinked;
        if (state.scaleLengthLinked) {
            setScaleLengthLinkedValue(state.scale.start);
        }
        onStateEdited();
    }

    private String livePreviewScopeLabel(LivePreviewScope value) {
        return editorText("preview_scope." + value.label, value.label);
    }

    private List<Entry> buildModuleEntries(EditorModule module) {
        List<Entry> entries = new ArrayList<>();
        addModuleEntries(module, entries);
        return entries;
    }

    private void addModuleEntries(EditorModule module, List<Entry> entries) {
        switch (module) {
            case COLOR -> addColorModuleEntries(entries);
            case TRANSPARENCY -> addCurveFields(entries, "transparency", "Transparency", state.transparency, 0.0f, 1.0f, 0.025f);
            case SCALE -> addLinkedCurveSectionEntries(entries, "scale", "Scale", state.scale, value -> state.scaleDataEnabled = value, 0.0f, 16.0f, 0.025f);
            case LENGTH -> addLinkedCurveSectionEntries(entries, "length", "Length", state.length, value -> state.lengthDataEnabled = value, 0.0f, 32.0f, 0.025f);
            case SPIN -> addSpinModuleEntries(entries);
            case SCREENSHAKE -> addScreenshakeModuleEntries(entries);
        }
    }

    private void optionalModuleSection(List<Entry> entries, EditorModule module, Consumer<List<Entry>> builder) {
        if (findFloatingPanel(module) != null) {
            return;
        }
        optionalSection(entries, module.key, module.translationKey, module.fallback, module, () -> isModuleEnabled(module), value -> setModuleEnabled(module, value), builder);
    }

    private void section(List<Entry> entries, String key, String translationKey, String fallback, Consumer<List<Entry>> builder) {
        String label = editorText(translationKey, fallback);
        categoryDefinitions.put(key, new CategoryDefinition(key, label, builder, buildingTab));
        if (findFloatingPanel(key) != null) {
            return;
        }
        entries.add(header(key, label, builder));
        if (!COLLAPSED_CATEGORIES.contains(key)) {
            builder.accept(entries);
        }
    }

    private void optionalSection(List<Entry> entries, String key, String translationKey, String fallback, EditorModule module, BooleanSupplier enabled, Consumer<Boolean> setter, Consumer<List<Entry>> builder) {
        entries.add(new OptionalCategoryEntry(key, editorText(translationKey, fallback), module, enabled, setter, COLLAPSED_CATEGORIES, this::onStateEdited));
        if (enabled.getAsBoolean() && !COLLAPSED_CATEGORIES.contains(key)) {
            builder.accept(entries);
        }
    }
    private Entry header(String key, String label, Consumer<List<Entry>> builder) {
        return new CategoryEntry(key, label, builder, COLLAPSED_CATEGORIES);
    }

    private Entry displayEntry(String label, String value) {
        return new Entry(label, false, false) {
            @Override
            String value() {
                return value;
            }
        };
    }

    private Entry actionEntry(String label, Runnable action) {
        return new Entry(label, false, false) {
            @Override
            String value() {
                return ">";
            }

            @Override
            void click(int button) {
                action.run();
            }
        };
    }

    private Entry boolEntry(String label, BooleanSupplier getter, Consumer<Boolean> setter) {
        return new Entry(label, false, false) {
            @Override
            String value() {
                return getter.getAsBoolean() ? "true" : "false";
            }

            @Override
            void click(int button) {
                setter.accept(!getter.getAsBoolean());
            }
        };
    }

    private <T extends Enum<T>> Entry enumEntry(String label, Supplier<T> getter, Consumer<T> setter, T[] values, java.util.function.Function<T, String> namer) {
        return new Entry(label, false, false) {
            @Override
            String value() {
                return namer.apply(getter.get());
            }

            @Override
            void click(int button) {
                int index = indexOf(values, getter.get());
                int direction = button == GLFW.GLFW_MOUSE_BUTTON_RIGHT ? -1 : 1;
                setter.accept(values[Math.floorMod(index + direction, values.length)]);
            }

            @Override
            void scroll(double amount) {
                int index = indexOf(values, getter.get());
                int direction = amount > 0 ? 1 : -1;
                setter.accept(values[Math.floorMod(index + direction, values.length)]);
            }

            @Override
            boolean adjustable() {
                return true;
            }
        };
    }

    private Entry easingEntry(String label, Supplier<Easing> getter, Consumer<Easing> setter) {
        return new Entry(label, false, false) {
            @Override
            String value() {
                return getter.get().name;
            }

            @Override
            void click(int button) {
                int index = EASINGS.indexOf(getter.get());
                int direction = button == GLFW.GLFW_MOUSE_BUTTON_RIGHT ? -1 : 1;
                setter.accept(EASINGS.get(Math.floorMod(index + direction, EASINGS.size())));
            }

            @Override
            void scroll(double amount) {
                int index = EASINGS.indexOf(getter.get());
                int direction = amount > 0 ? 1 : -1;
                setter.accept(EASINGS.get(Math.floorMod(index + direction, EASINGS.size())));
            }

            @Override
            boolean adjustable() {
                return true;
            }
        };
    }

    private Entry intEntry(String label, IntSupplier getter, IntConsumer setter, int min, int max, int step) {
        return new Entry(label, true, false) {
            @Override
            String value() {
                return String.valueOf(getter.getAsInt());
            }

            @Override
            void commit(String value) {
                setter.accept(Mth.clamp(Integer.parseInt(value), min, max));
            }

            @Override
            void scroll(double amount) {
                int multiplier = Screen.hasShiftDown() ? 10 : 1;
                int direction = amount > 0 ? 1 : -1;
                setter.accept(Mth.clamp(getter.getAsInt() + step * multiplier * direction, min, max));
            }

            @Override
            boolean adjustable() {
                return true;
            }
        };
    }

    private Entry floatEntry(String label, Supplier<Float> getter, Consumer<Float> setter, float min, float max, float step) {
        return new Entry(label, true, false) {
            @Override
            String value() {
                return format(getter.get());
            }

            @Override
            void commit(String value) {
                setter.accept(Mth.clamp(Float.parseFloat(value), min, max));
            }

            @Override
            void scroll(double amount) {
                float multiplier = Screen.hasShiftDown() ? 10.0f : Screen.hasControlDown() ? 0.1f : 1.0f;
                float direction = amount > 0 ? 1.0f : -1.0f;
                setter.accept(Mth.clamp(getter.get() + step * multiplier * direction, min, max));
            }

            @Override
            boolean adjustable() {
                return true;
            }
        };
    }

    private Entry doubleEntry(String label, DoubleSupplier getter, DoubleConsumer setter, double min, double max, double step) {
        return new Entry(label, true, false) {
            @Override
            String value() {
                return format(getter.getAsDouble());
            }

            @Override
            void commit(String value) {
                setter.accept(Mth.clamp(Double.parseDouble(value), min, max));
            }

            @Override
            void scroll(double amount) {
                double multiplier = Screen.hasShiftDown() ? 10.0 : Screen.hasControlDown() ? 0.1 : 1.0;
                double direction = amount > 0 ? 1.0 : -1.0;
                setter.accept(Mth.clamp(getter.getAsDouble() + step * multiplier * direction, min, max));
            }

            @Override
            boolean adjustable() {
                return true;
            }
        };
    }

    private Entry hexEntry(String label, IntSupplier getter, IntConsumer setter) {
        return new Entry(label, true, false) {
            @Override
            String value() {
                return "#" + String.format(Locale.ROOT, "%06X", getter.getAsInt() & 0xFFFFFF);
            }

            @Override
            void commit(String value) {
                String clean = value.trim();
                if (clean.startsWith("#")) {
                    clean = clean.substring(1);
                }
                setter.accept(Integer.parseUnsignedInt(clean, 16) & 0xFFFFFF);
            }
        };
    }

    private boolean handleRenderedEntryClick(double mouseX, double mouseY, int button) {
        return handleRenderedEntryClick(mouseX, mouseY, button, null);
    }

    private boolean handleRenderedEntryClick(double mouseX, double mouseY, int button, FloatingTabPanel owner) {
        for (int i = renderedEntries.size() - 1; i >= 0; i--) {
            RenderedEntry rendered = renderedEntries.get(i);
            if (rendered.owner == owner && rendered.contains(mouseX, mouseY)) {
                Entry entry = rendered.entry;
                if (entry.header) {
                    cancelEditing();
                    entry.click(button);
                    return true;
                }
                if (entry instanceof ColorChannelsEntry channels) {
                    int channel = colorChannelAt(rendered, mouseX);
                    if (channel >= 0) {
                        channels.nudgeChannel(channel, button == GLFW.GLFW_MOUSE_BUTTON_RIGHT ? -1 : 1);
                        onStateEdited();
                        return true;
                    }
                    cancelEditing();
                    return true;
                }
                if (entry instanceof ShapeSizeEntry shapeSize) {
                    int axis = shapeAxisAt(rendered, mouseX, shapeSize.axisCount());
                    if (axis >= 0) {
                        shapeSize.nudgeAxis(axis, button == GLFW.GLFW_MOUSE_BUTTON_RIGHT ? -1 : 1);
                        onStateEdited();
                        return true;
                    }
                    cancelEditing();
                    return true;
                }
                if (entry.adjustable() && rendered.minusContains(mouseX, mouseY)) {
                    cancelEditing();
                    entry.nudge(-1);
                    onStateEdited();
                    return true;
                }
                if (entry.adjustable() && rendered.plusContains(mouseX, mouseY)) {
                    cancelEditing();
                    entry.nudge(1);
                    onStateEdited();
                    return true;
                }
                if (entry.adjustable() && entry.editable && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                    cancelEditing();
                    draggingEntry = entry;
                    draggingMouseX = mouseX;
                    draggingMoved = false;
                    return true;
                }
                if (entry.editable) {
                    startEditing(entry);
                    return true;
                }
                cancelEditing();
                entry.click(button);
                onStateEdited();
                return true;
            }
        }
        return false;
    }

    private boolean handleClickZoneClick(double mouseX, double mouseY) {
        return handleClickZoneClick(mouseX, mouseY, null);
    }

    private boolean handleClickZoneClick(double mouseX, double mouseY, FloatingTabPanel owner) {
        for (int i = clickZones.size() - 1; i >= 0; i--) {
            ClickZone zone = clickZones.get(i);
            if (zone.ownedBy(owner) && zone.contains(mouseX, mouseY)) {
                if (editingListProjectIndex >= 0 && !projectBrowserContains(mouseX, mouseY)) {
                    commitProjectNameEditing();
                }
                zone.action.run();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (loadPickerOpen) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                handleClickZoneClick(mouseX, mouseY);
            }
            return true;
        }
        if (projectPickerOpen) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && handleClickZoneClick(mouseX, mouseY)) {
                return true;
            }
            cancelEditing();
            editingProjectName = false;
            return true;
        }
        FloatingTabPanel floatingPanel = floatingPanelAt(mouseX, mouseY);
        if (floatingPanel != null) {
            bringFloatingPanelToFront(floatingPanel);
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && floatingPanel.resizeHandleContains(mouseX, mouseY)) {
                cancelEditing();
                resizingFloatingPanel = floatingPanel;
                resizeStartMouseX = mouseX;
                resizeStartMouseY = mouseY;
                resizeStartWidth = floatingPanel.w;
                resizeStartHeight = floatingPanel.h;
                return true;
            }
            if (floatingPanel.dockContains(mouseX, mouseY)) {
                floatingPanels.remove(floatingPanel);
                if (floatingPanel.tab != null) {
                    tab = floatingPanel.tab;
                    scroll = 0;
                }
                cancelEditing();
                return true;
            }
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && floatingPanel.headerContains(mouseX, mouseY)) {
                cancelEditing();
                movingFloatingPanel = floatingPanel;
                movingPanelOffsetX = mouseX - floatingPanel.x;
                movingPanelOffsetY = mouseY - floatingPanel.y;
                return true;
            }
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && handleClickZoneClick(mouseX, mouseY, floatingPanel)) {
                return true;
            }
            if (handleRenderedEntryClick(mouseX, mouseY, button, floatingPanel)) {
                return true;
            }
            cancelEditing();
            return true;
        }
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && handleClickZoneClick(mouseX, mouseY)) {
            return true;
        }
        if (editingListProjectIndex >= 0 && !projectBrowserContains(mouseX, mouseY)) {
            commitProjectNameEditing();
        }
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && panelHeaderContains(mouseX, mouseY)) {
            cancelEditing();
            movingPanel = true;
            movingScreenOffsetX = mouseX - panelX;
            movingScreenOffsetY = mouseY - panelY;
            return true;
        }
        if (handleRenderedEntryClick(mouseX, mouseY, button)) {
            return true;
        }
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && resizeHandleContains(mouseX, mouseY)) {
            cancelEditing();
            resizingPanel = true;
            resizeStartMouseX = mouseX;
            resizeStartMouseY = mouseY;
            resizeStartWidth = panelWidth;
            resizeStartHeight = panelHeight;
            return true;
        }
        for (TabButton tabButton : renderedTabs) {
            if (tabButton.contains(mouseX, mouseY)) {
                if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                    detachTab(tabButton.tab, (int) mouseX, (int) mouseY);
                    return true;
                }
                if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                    pressedTab = tabButton;
                    tabDragStartX = mouseX;
                    tabDragStartY = mouseY;
                    return true;
                }
            }
        }
        cancelEditing();
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (movingPanel && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            panelX = (int) (mouseX - movingScreenOffsetX);
            panelY = (int) (mouseY - movingScreenOffsetY);
            clampPanelPosition();
            return true;
        }
        if (resizingFloatingPanel != null && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            resizingFloatingPanel.w = (int) (resizeStartWidth + mouseX - resizeStartMouseX);
            resizingFloatingPanel.h = (int) (resizeStartHeight + mouseY - resizeStartMouseY);
            clampFloatingPanel(resizingFloatingPanel);
            return true;
        }
        if (pressedTab != null && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            double distance = Math.hypot(mouseX - tabDragStartX, mouseY - tabDragStartY);
            if (distance > 8.0) {
                FloatingTabPanel panel = detachTab(pressedTab.tab, (int) mouseX, (int) mouseY);
                movingFloatingPanel = panel;
                movingPanelOffsetX = panel.w / 2.0;
                movingPanelOffsetY = 10.0;
                pressedTab = null;
            }
            return true;
        }
        if (movingFloatingPanel != null && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            movingFloatingPanel.x = (int) (mouseX - movingPanelOffsetX);
            movingFloatingPanel.y = (int) (mouseY - movingPanelOffsetY);
            clampFloatingPanel(movingFloatingPanel);
            return true;
        }
        if (resizingPanel && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            int margin = 8;
            int availablePanelW = Math.max(1, width - panelX - margin);
            int availablePanelH = Math.max(1, height - panelY - margin);
            panelWidth = Mth.clamp((int) (resizeStartWidth + mouseX - resizeStartMouseX), Math.min(MIN_PANEL_WIDTH, availablePanelW), availablePanelW);
            panelHeight = Mth.clamp((int) (resizeStartHeight + mouseY - resizeStartMouseY), Math.min(MIN_PANEL_HEIGHT, availablePanelH), availablePanelH);
            return true;
        }
        if (draggingEntry != null && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            int steps = (int) ((mouseX - draggingMouseX) / DRAG_STEP_PIXELS);
            if (steps != 0) {
                int direction = steps > 0 ? 1 : -1;
                for (int i = 0; i < Math.abs(steps); i++) {
                    draggingEntry.nudge(direction);
                }
                draggingMouseX += steps * DRAG_STEP_PIXELS;
                draggingMoved = true;
                onStateEdited();
            }
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (movingPanel && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            movingPanel = false;
            saveWorkspaceMemory();
            return true;
        }
        if (resizingFloatingPanel != null && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            resizingFloatingPanel = null;
            return true;
        }
        if (pressedTab != null && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            tab = pressedTab.tab;
            scroll = 0;
            pressedTab = null;
            cancelEditing();
            saveWorkspaceMemory();
            return true;
        }
        if (movingFloatingPanel != null && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            movingFloatingPanel = null;
            return true;
        }
        if (resizingPanel && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            resizingPanel = false;
            saveWorkspaceMemory();
            return true;
        }
        if (draggingEntry != null && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            Entry entry = draggingEntry;
            boolean moved = draggingMoved;
            draggingEntry = null;
            draggingMoved = false;
            if (!moved && entry.editable) {
                startEditing(entry);
            }
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (loadPickerOpen) {
            int visibleRows = Math.max(1, (Math.min(300, Math.max(210, height - 32)) - 72) / (ROW_HEIGHT + ROW_GAP));
            int direction = scrollY > 0 ? -1 : 1;
            loadPickerScroll = Mth.clamp(loadPickerScroll + direction, 0, Math.max(0, loadPickerProjects.size() - visibleRows));
            return true;
        }
        if (projectPickerOpen) {
            return true;
        }
        if (projectBrowserVisible && inBounds(mouseX, mouseY, lastProjectBrowserX, lastProjectBrowserY, lastProjectBrowserW, lastProjectBrowserH)) {
            int direction = scrollY > 0 ? -1 : 1;
            projectListScroll = Mth.clamp(projectListScroll + direction, 0, maxProjectListScroll());
            return true;
        }
        for (int i = renderedEntries.size() - 1; i >= 0; i--) {
            RenderedEntry rendered = renderedEntries.get(i);
            if (rendered.contains(mouseX, mouseY)) {
                if (rendered.entry instanceof ColorChannelsEntry channels) {
                    int channel = colorChannelAt(rendered, mouseX);
                    if (channel >= 0) {
                        channels.nudgeChannel(channel, scrollY > 0 ? 1 : -1);
                        onStateEdited();
                    }
                    return true;
                }
                if (rendered.entry instanceof ShapeSizeEntry shapeSize) {
                    int axis = shapeAxisAt(rendered, mouseX, shapeSize.axisCount());
                    if (axis >= 0) {
                        shapeSize.nudgeAxis(axis, scrollY > 0 ? 1 : -1);
                        onStateEdited();
                    }
                    return true;
                }
                rendered.entry.scroll(scrollY);
                onStateEdited();
                return true;
            }
        }
        FloatingTabPanel floatingPanel = floatingPanelAt(mouseX, mouseY);
        if (floatingPanel != null) {
            floatingPanel.scroll = clampScroll(floatingPanel.scroll - (int) (scrollY * ROW_HEIGHT * 2), floatingPanel.contentHeight, floatingPanel.bodyHeight());
            return true;
        }
        scroll = clampScroll(scroll - (int) (scrollY * ROW_HEIGHT * 2), contentHeight, mainEntriesViewportHeight);
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (loadPickerOpen) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                closeLoadPicker();
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
                loadSelectedProjects(false);
                return true;
            }
            if (Screen.hasControlDown() && keyCode == GLFW.GLFW_KEY_A) {
                selectAllLoadProjects();
                return true;
            }
            return true;
        }
        if (editingListProjectIndex >= 0) {
            if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
                commitProjectNameEditing();
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                cancelProjectNameEditing();
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE && !editingListProjectName.isEmpty()) {
                editingListProjectName = editingListProjectName.substring(0, editingListProjectName.length() - 1);
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_DELETE) {
                editingListProjectName = "";
                return true;
            }
            if (Screen.hasControlDown() && keyCode == GLFW.GLFW_KEY_V && minecraft != null) {
                editingListProjectName += sanitizeProjectNameInput(minecraft.keyboardHandler.getClipboard());
                return true;
            }
            return true;
        }
        if (editingEntry != null) {
            if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
                commitEditing();
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                cancelEditing();
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE && !editingText.isEmpty()) {
                editingText = editingText.substring(0, editingText.length() - 1);
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_DELETE) {
                editingText = "";
                return true;
            }
            if (Screen.hasControlDown() && keyCode == GLFW.GLFW_KEY_V && minecraft != null) {
                editingText += minecraft.keyboardHandler.getClipboard();
                return true;
            }
            return true;
        }
        if (projectPickerOpen) {
            if (editingProjectName) {
                if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
                    createFreshProject();
                    return true;
                }
                if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                    editingProjectName = false;
                    return true;
                }
                if (keyCode == GLFW.GLFW_KEY_BACKSPACE && !newProjectName.isEmpty()) {
                    newProjectName = newProjectName.substring(0, newProjectName.length() - 1);
                    return true;
                }
                if (keyCode == GLFW.GLFW_KEY_DELETE) {
                    newProjectName = "";
                    return true;
                }
                if (Screen.hasControlDown() && keyCode == GLFW.GLFW_KEY_V && minecraft != null) {
                    newProjectName += sanitizeProjectNameInput(minecraft.keyboardHandler.getClipboard());
                    return true;
                }
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
                continueCurrentProject();
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_N) {
                createFreshProject();
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_O) {
                openProjectFolder();
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_ESCAPE && minecraft != null) {
                minecraft.setScreen(null);
                return true;
            }
            return true;
        }
        if (Screen.hasControlDown() && keyCode == GLFW.GLFW_KEY_S) {
            saveAllProjects();
            return true;
        }
        if (Screen.hasControlDown() && keyCode == GLFW.GLFW_KEY_O) {
            openProjectFolder();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_SPACE) {
            spawnPreviewWithEffects();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (loadPickerOpen) {
            return true;
        }
        if (editingListProjectIndex >= 0 && isAllowedProjectNameChar(codePoint)) {
            editingListProjectName += codePoint;
            return true;
        }
        if (projectPickerOpen && editingProjectName && isAllowedProjectNameChar(codePoint)) {
            newProjectName += codePoint;
            return true;
        }
        if (editingEntry != null && isAllowedEntryChar(editingEntry, codePoint)) {
            editingText += codePoint;
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private ParticleEditorProject activeProject() {
        if (projects.isEmpty()) {
            state = new ParticleEditorState();
            projects.add(new ParticleEditorProject("particle_1", state));
            activeProjectIndex = 0;
        }
        activeProjectIndex = Mth.clamp(activeProjectIndex, 0, projects.size() - 1);
        return projects.get(activeProjectIndex);
    }

    private static void ensureSavedWorkspace() {
        if (SAVED_PROJECTS.isEmpty()) {
            ParticleEditorState initial = new ParticleEditorState();
            SAVED_PROJECTS.add(new ParticleEditorProject("particle_1", initial));
            savedActiveProjectIndex = 0;
        }
    }

    private void saveWorkspaceMemory() {
        if (projects.isEmpty()) {
            return;
        }
        savedActiveProjectIndex = Mth.clamp(activeProjectIndex, 0, projects.size() - 1);
        savedLoadProjectIndex = loadProjectIndex;
        savedTab = tab;
        savedPanelX = panelX;
        savedPanelY = panelY;
        savedPanelWidth = panelWidth;
        savedPanelHeight = panelHeight;
        savedLivePreviewScope = livePreviewScope;
    }

    private void continueCurrentProject() {
        projectPickerOpen = false;
        editingProjectName = false;
        savedProjectPickerSeen = true;
        lastPreviewSnapshot = livePreviewSnapshot();
        refreshLivePreview();
        saveWorkspaceMemory();
    }

    private void createFreshProject() {
        cancelProjectNameEditing();
        clearPreviewParticles();
        ParticleEditorState newState = new ParticleEditorState();
        initializeFixedPreviewPosition(newState);
        String name = sanitizeProjectName(newProjectName);
        projects.clear();
        projects.add(new ParticleEditorProject(name, newState));
        activeProjectIndex = 0;
        loadProjectIndex = -1;
        state = newState;
        tab = ParticleEditorTab.EMITTER;
        scroll = 0;
        floatingPanels.clear();
        projectPickerOpen = false;
        editingProjectName = false;
        savedProjectPickerSeen = true;
        newProjectName = uniqueProjectName("particle_2");
        lastPreviewSnapshot = livePreviewSnapshot();
        saveWorkspaceMemory();
        saveAllProjects();
        refreshLivePreview();
        message("New particle file created: " + name);
    }

    private void openProjectFolder() {
        try {
            cancelProjectNameEditing();
            List<Path> paths = ParticleEditorStorage.listProjects(Minecraft.getInstance());
            if (paths.isEmpty()) {
                openProjectsFolder();
                message("No particle projects yet. Folder opened");
                return;
            }
            loadPickerProjects.clear();
            loadPickerSelection.clear();
            for (Path path : paths) {
                loadPickerProjects.add(ParticleEditorStorage.loadProject(path));
            }
            selectAllLoadProjects();
            loadPickerScroll = 0;
            loadPickerOpen = true;
            editingProjectName = false;
            message("Found particle project files: " + paths.size());
        } catch (IOException | RuntimeException exception) {
            message("Failed to open particle project: " + exception.getMessage());
        }
    }

    private void loadSelectedProjects(boolean append) {
        if (loadPickerSelection.isEmpty()) {
            message("Select at least one particle file");
            return;
        }
        int selectedCount = loadPickerSelection.size();
        clearPreviewParticles();
        int firstAddedIndex = append ? projects.size() : 0;
        if (!append) {
            projects.clear();
            activeProjectIndex = 0;
            loadProjectIndex = -1;
            floatingPanels.clear();
            scroll = 0;
        }
        for (int i = 0; i < loadPickerProjects.size(); i++) {
            if (loadPickerSelection.contains(i)) {
                ParticleEditorStorage.LoadedProject loaded = loadPickerProjects.get(i);
                ParticleEditorState loadedState = loaded.state().copy();
                initializeFixedPreviewPosition(loadedState);
                projects.add(new ParticleEditorProject(uniqueProjectName(loaded.name()), loadedState));
            }
        }
        projectListScroll = 0;
        closeLoadPicker();
        projectPickerOpen = false;
        editingProjectName = false;
        savedProjectPickerSeen = true;
        selectProject(Math.min(firstAddedIndex, projects.size() - 1));
        message((append ? "Added particle project files: " : "Loaded particle project files: ") + selectedCount);
    }

    private void toggleLoadSelection(int index) {
        if (loadPickerSelection.contains(index)) {
            loadPickerSelection.remove(index);
        } else {
            loadPickerSelection.add(index);
        }
    }

    private void deleteLoadedProject(int index) {
        if (index < 0 || index >= loadPickerProjects.size()) {
            return;
        }
        ParticleEditorStorage.LoadedProject project = loadPickerProjects.get(index);
        try {
            ParticleEditorStorage.deleteProject(project.path());
            loadPickerProjects.remove(index);
            Set<Integer> shiftedSelection = new HashSet<>();
            for (Integer selected : loadPickerSelection) {
                if (selected < index) {
                    shiftedSelection.add(selected);
                } else if (selected > index) {
                    shiftedSelection.add(selected - 1);
                }
            }
            loadPickerSelection.clear();
            loadPickerSelection.addAll(shiftedSelection);
            loadPickerScroll = Mth.clamp(loadPickerScroll, 0, Math.max(0, loadPickerProjects.size() - 1));
            message("Deleted saved particle project: " + project.name());
        } catch (IOException | RuntimeException exception) {
            message("Failed to delete particle project: " + exception.getMessage());
        }
    }

    private void selectAllLoadProjects() {
        loadPickerSelection.clear();
        for (int i = 0; i < loadPickerProjects.size(); i++) {
            loadPickerSelection.add(i);
        }
    }

    private void closeLoadPicker() {
        loadPickerOpen = false;
        loadPickerProjects.clear();
        loadPickerSelection.clear();
        loadPickerScroll = 0;
    }

    private void addProject() {
        commitProjectNameEditing();
        ParticleEditorState newState = new ParticleEditorState();
        initializeFixedPreviewPosition(newState);
        ParticleEditorProject project = new ParticleEditorProject(uniqueProjectName("particle_" + (projects.size() + 1)), newState);
        projects.add(project);
        selectProject(projects.size() - 1);
        projectListScroll = maxProjectListScroll();
        message("Added particle project: " + project.name);
    }

    private void removeProject(int index) {
        if (projects.size() <= 1 || index < 0 || index >= projects.size()) {
            return;
        }
        if (editingListProjectIndex == index) {
            cancelProjectNameEditing();
        } else if (editingListProjectIndex > index) {
            editingListProjectIndex--;
        }
        String removedName = projects.get(index).name;
        projects.remove(index);
        if (activeProjectIndex >= projects.size()) {
            activeProjectIndex = projects.size() - 1;
        }
        if (index < activeProjectIndex) {
            activeProjectIndex--;
        }
        projectListScroll = Mth.clamp(projectListScroll, 0, maxProjectListScroll());
        selectProject(activeProjectIndex);
        message("Removed from workspace: " + removedName);
    }

    private void clickProjectName(int index) {
        if (index < 0 || index >= projects.size()) {
            return;
        }
        if (editingListProjectIndex == index) {
            return;
        }
        if (editingListProjectIndex >= 0) {
            commitProjectNameEditing();
        }
        if (activeProjectIndex != index) {
            selectProject(index);
            return;
        }
        startProjectNameEditing(index);
    }

    private void startProjectNameEditing(int index) {
        editingListProjectIndex = index;
        editingListProjectName = projects.get(index).name;
        cancelEditing();
    }

    private void commitProjectNameEditing() {
        if (editingListProjectIndex < 0) {
            return;
        }
        if (editingListProjectIndex < projects.size()) {
            ParticleEditorProject project = projects.get(editingListProjectIndex);
            String oldName = project.name;
            project.name = uniqueProjectName(sanitizeProjectName(editingListProjectName), editingListProjectIndex);
            if (!oldName.equals(project.name)) {
                message("Renamed particle: " + project.name);
            }
        }
        cancelProjectNameEditing();
        saveWorkspaceMemory();
    }

    private void cancelProjectNameEditing() {
        editingListProjectIndex = -1;
        editingListProjectName = "";
    }

    private void toggleProjectVisibility(int index) {
        if (index < 0 || index >= projects.size()) {
            return;
        }
        commitProjectNameEditing();
        ParticleEditorProject project = projects.get(index);
        project.visible = !project.visible;
        clearPreviewParticles();
        refreshLivePreview();
        message((project.visible ? "Shown particle: " : "Hidden particle: ") + project.name);
    }

    private void openProjectsFolder() {
        try {
            ParticleEditorStorage.openRootFolder(Minecraft.getInstance());
            message("Opened projects folder");
        } catch (IOException | RuntimeException exception) {
            message("Failed to open projects folder: " + exception.getMessage());
        }
    }

    private void selectProject(int index) {
        if (projects.isEmpty()) {
            return;
        }
        if (editingListProjectIndex >= 0 && Math.floorMod(index, projects.size()) != editingListProjectIndex) {
            commitProjectNameEditing();
        }
        activeProjectIndex = Math.floorMod(index, projects.size());
        state = activeProject().state;
        scroll = 0;
        lastPreviewSnapshot = livePreviewSnapshot();
        keepActiveProjectVisible();
        saveWorkspaceMemory();
        refreshLivePreview();
    }

    private int visibleProjectRows() {
        int listHeight = Math.max(ROW_HEIGHT, lastProjectBrowserH - 46);
        return Math.max(1, listHeight / (ROW_HEIGHT + ROW_GAP));
    }

    private int maxProjectListScroll() {
        return Math.max(0, projects.size() - visibleProjectRows());
    }

    private void keepActiveProjectVisible() {
        int visibleRows = visibleProjectRows();
        if (activeProjectIndex < projectListScroll) {
            projectListScroll = activeProjectIndex;
        } else if (activeProjectIndex >= projectListScroll + visibleRows) {
            projectListScroll = activeProjectIndex - visibleRows + 1;
        }
        projectListScroll = Mth.clamp(projectListScroll, 0, maxProjectListScroll());
    }

    private String uniqueProjectName(String baseName) {
        return uniqueProjectName(baseName, -1);
    }

    private String uniqueProjectName(String baseName, int ignoredIndex) {
        String clean = baseName == null || baseName.isBlank() ? "particle" : baseName;
        String candidate = clean;
        int suffix = 2;
        while (hasProjectNamed(candidate, ignoredIndex)) {
            candidate = clean + "_" + suffix++;
        }
        return candidate;
    }

    private boolean hasProjectNamed(String name, int ignoredIndex) {
        int index = 0;
        for (ParticleEditorProject project : projects) {
            if (index != ignoredIndex && project.name.equalsIgnoreCase(name)) {
                return true;
            }
            index++;
        }
        return false;
    }

    private void saveAllProjects() {
        int saved = 0;
        try {
            for (ParticleEditorProject project : projects) {
                ParticleEditorStorage.saveProject(Minecraft.getInstance(), project);
                saved++;
            }
            message("Saved particle projects: " + saved);
        } catch (IOException | RuntimeException exception) {
            message("Failed to save particle projects: " + exception.getMessage());
        }
    }

    private void commitEditing() {
        if (editingEntry == null) {
            return;
        }
        try {
            editingEntry.commit(editingText);
            onStateEdited();
        } catch (RuntimeException ignored) {
            if (minecraft != null && minecraft.player != null) {
                minecraft.player.displayClientMessage(Component.literal("Invalid value: " + editingText), true);
            }
        }
        cancelEditing();
    }

    private void cancelEditing() {
        editingEntry = null;
        editingText = "";
    }

    private void startEditing(Entry entry) {
        editingEntry = entry;
        editingText = entry.value().replace("#", "");
    }

    private boolean isAllowedEditChar(char c) {
        return Character.isDigit(c) || c == '-' || c == '.' || c == '#' ||
                (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    private boolean isAllowedEntryChar(Entry entry, char c) {
        return isAllowedEditChar(c);
    }

    private boolean isAllowedProjectNameChar(char c) {
        return !Character.isISOControl(c) && "\\/:*?\"<>|".indexOf(c) < 0;
    }

    private String sanitizeProjectNameInput(String value) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (isAllowedProjectNameChar(c)) {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    private String sanitizeProjectName(String value) {
        String clean = sanitizeProjectNameInput(value).trim();
        return clean.isBlank() ? "particle_1" : clean;
    }

    private void spawnPreviewWithEffects() {
        spawnPreview(activeProject(), true);
    }

    private void spawnAllPreviews() {
        int spawned = 0;
        for (ParticleEditorProject project : projects) {
            if (project.visible) {
                spawnPreview(project, true);
                spawned++;
            }
        }
        message("Spawned visible particle projects: " + spawned);
    }

    private void spawnPreview(ParticleEditorProject project) {
        spawnPreview(project, false);
    }

    private void spawnPreview(ParticleEditorProject project, boolean includeEffects) {
        if (!project.visible) {
            return;
        }
        spawnPreview(project.state);
        if (includeEffects && project.state.screenshakeOnBurst) {
            playScreenshake(project.state);
        }
    }

    private void spawnPreview(ParticleEditorState previewState) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null || client.player == null) {
            return;
        }
        if (previewState.outputMode == ParticleOutputMode.SCREEN) {
            var builder = previewState.createScreenBuilder(screenPreviewParticles);
            if (previewState.spawnDistribution == SpawnDistributionMode.BUILDER_RANDOM) {
                if (previewState.screenTrackStack) {
                    builder.repeatOnStack(previewState.stackTrackXOffset, previewState.stackTrackYOffset, previewState.spawnCount);
                } else {
                    builder.repeat(previewState.screenX, previewState.screenY, previewState.spawnCount);
                }
                return;
            }
            for (int i = 0; i < previewState.spawnCount; i++) {
                Vec3 offset = previewState.createScreenDistributionOffset(client.level.random);
                if (previewState.screenTrackStack) {
                    builder.spawnOnStack(previewState.stackTrackXOffset + offset.x, previewState.stackTrackYOffset + offset.y);
                } else {
                    builder.spawn(previewState.screenX + offset.x, previewState.screenY + offset.y);
                }
            }
            return;
        }
        Vec3 pos = getPreviewPosition(previewState);
        for (int i = 0; i < previewState.spawnCount; i++) {
            previewState.createBuilder().addSpawnActor(this::trackPreviewParticle).spawn(client.level, pos.add(previewState.createWorldDistributionOffset(client.level.random)));
        }
    }

    private void tickLivePreview() {
        if (minecraft == null || minecraft.level == null || minecraft.player == null) {
            return;
        }
        Set<ParticleEditorProject> previewedProjects = Collections.newSetFromMap(new IdentityHashMap<>());
        for (ParticleEditorProject project : projects) {
            if (!shouldLivePreview(project)) {
                continue;
            }
            previewedProjects.add(project);
            ParticleEditorState previewState = project.state;
            if (previewState.previewPlayback != PreviewPlaybackMode.LOOPING) {
                if (!livePreviewOneShots.contains(project)) {
                    spawnPreview(project);
                    livePreviewOneShots.add(project);
                }
                livePreviewTicks.remove(project);
                continue;
            }
            int ticks = livePreviewTicks.getOrDefault(project, 0) - 1;
            if (ticks <= 0) {
                spawnPreview(project);
                ticks = Math.max(1, previewState.previewInterval);
            }
            livePreviewTicks.put(project, ticks);
            livePreviewOneShots.remove(project);
        }
        livePreviewTicks.keySet().removeIf(project -> !previewedProjects.contains(project));
        livePreviewOneShots.removeIf(project -> !previewedProjects.contains(project));
    }

    private boolean shouldLivePreview(ParticleEditorProject project) {
        if (!project.visible || !project.state.autoPreview) {
            return false;
        }
        return livePreviewScope == LivePreviewScope.VISIBLE_PROJECTS || project == activeProject();
    }

    private void resetLivePreviewTimers() {
        livePreviewTicks.clear();
        livePreviewOneShots.clear();
    }

    private void playScreenshakePreview() {
        playScreenshake(state);
    }

    private void playScreenshake(ParticleEditorState targetState) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null || client.player == null || !targetState.screenshakeEnabled) {
            return;
        }
        ScreenshakeHandler.addScreenshake(targetState.createScreenshake(getPreviewPosition(targetState)));
    }

    private void refreshLivePreview() {
        clearPreviewParticles();
        resetLivePreviewTimers();
        lastPreviewSnapshot = livePreviewSnapshot();
        if (minecraft == null || minecraft.level == null || minecraft.player == null) {
            return;
        }
        for (ParticleEditorProject project : projects) {
            if (shouldLivePreview(project)) {
                spawnPreview(project);
                if (project.state.previewPlayback == PreviewPlaybackMode.LOOPING) {
                    livePreviewTicks.put(project, Math.max(1, project.state.previewInterval));
                } else {
                    livePreviewOneShots.add(project);
                }
            }
        }
    }

    private String livePreviewSnapshot() {
        StringBuilder builder = new StringBuilder("scope=").append(livePreviewScope.label);
        if (livePreviewScope == LivePreviewScope.VISIBLE_PROJECTS) {
            for (int i = 0; i < projects.size(); i++) {
                appendProjectPreviewSnapshot(builder, projects.get(i), i);
            }
        } else {
            appendProjectPreviewSnapshot(builder, activeProject(), activeProjectIndex);
        }
        return builder.toString();
    }

    private void appendProjectPreviewSnapshot(StringBuilder builder, ParticleEditorProject project, int index) {
        builder.append('|')
                .append(index)
                .append(":visible=").append(project.visible)
                .append(":auto=").append(project.state.autoPreview)
                .append(":").append(project.state.previewSnapshot());
    }

    private void handleRealtimePreviewChanges() {
        String snapshot = livePreviewSnapshot();
        if (!snapshot.equals(lastPreviewSnapshot)) {
            lastPreviewSnapshot = snapshot;
            refreshLivePreview();
        }
    }

    private void onStateEdited() {
        handleRealtimePreviewChanges();
    }

    private void trackPreviewParticle(LodestoneWorldParticle particle) {
        previewParticles.add(particle);
    }

    private void prunePreviewParticles() {
        previewParticles.removeIf(particle -> !particle.isAlive());
    }

    private void clearPreviewParticles() {
        previewParticles.forEach(LodestoneWorldParticle::remove);
        previewParticles.clear();
        ScreenParticleHandler.clearParticles(screenPreviewParticles);
    }

    private void renderScreenShapeOverlays(GuiGraphics graphics) {
        if (projectPickerOpen || loadPickerOpen) {
            return;
        }
        ParticleEditorScreenShapeOverlay.render(graphics, projects);
    }

    private Vec3 getPreviewPosition(ParticleEditorState previewState) {
        if (previewState.followCamera) {
            return calculateCameraPreviewPosition(previewState);
        }
        return previewState.fixedPreviewPosition();
    }

    void renderWorldShapeOverlays(com.mojang.blaze3d.vertex.PoseStack poseStack, net.minecraft.client.Camera camera) {
        if (projectPickerOpen || loadPickerOpen) {
            return;
        }
        int index = 0;
        for (ParticleEditorProject project : projects) {
            ParticleEditorState overlayState = project.state;
            if (project.visible && overlayState.shapeOverlay && overlayState.outputMode == ParticleOutputMode.WORLD) {
                ParticleEditorOverlayRenderer.renderShape(poseStack, camera, getPreviewPosition(overlayState), overlayState, index);
                index++;
            }
        }
    }

    private Vec3 calculateCameraPreviewPosition() {
        return calculateCameraPreviewPosition(state);
    }

    private Vec3 calculateCameraPreviewPosition(ParticleEditorState previewState) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) {
            return Vec3.ZERO;
        }
        Vec3 look = client.player.getLookAngle();
        return client.player.getEyePosition().add(look.scale(previewState.previewDistance)).add(0, previewState.previewYOffset, 0);
    }

    private void initializeFixedPreviewPositions() {
        for (ParticleEditorProject project : projects) {
            initializeFixedPreviewPosition(project.state);
        }
    }

    private void initializeFixedPreviewPosition(ParticleEditorState targetState) {
        if (!targetState.followCamera && isUnsetFixedPreviewPosition(targetState)) {
            targetState.setFixedPreviewPosition(calculateCameraPreviewPosition(targetState));
        }
    }

    private boolean isUnsetFixedPreviewPosition(ParticleEditorState targetState) {
        return targetState.fixedPreviewX == 0.0 && targetState.fixedPreviewY == 0.0 && targetState.fixedPreviewZ == 0.0;
    }

    @Override
    public void onClose() {
        ParticleEditorOverlayRenderer.clearActiveScreen(this);
        saveWorkspaceMemory();
        if (!projectPickerOpen && minecraft != null && minecraft.level != null && minecraft.player != null) {
            ParticleEditorPreviewSession.start(projects);
            previewParticles.clear();
            if (ParticleEditorPreviewSession.isActive()) {
                message("Background particle preview started");
            }
        } else {
            clearPreviewParticles();
        }
        super.onClose();
    }

    private void message(String text) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            client.player.displayClientMessage(Component.literal(text), true);
        }
    }

    private String editorText(String key, String fallback) {
        String translationKey = "screen.lodestone.particle_editor." + key;
        String translated = Component.translatable(translationKey).getString();
        return translated.equals(translationKey) ? fallback : translated;
    }

    private String spawnDistributionLabel(SpawnDistributionMode value) {
        return editorText("spawn_shape." + value.label, value.label);
    }

    private String previewPositionLabel(PreviewPositionMode value) {
        return editorText("preview_mode." + value.label, value.label);
    }

    private void drawCentered(GuiGraphics graphics, String text, int x, int y, int width, int color) {
        ParticleEditorUi.drawCentered(font, graphics, text, x, y, width, color);
    }

    private String trimToWidth(String text, int maxWidth) {
        return ParticleEditorUi.trimToWidth(font, text, maxWidth);
    }

    private String moduleLabel(EditorModule module) {
        return editorText(module.translationKey, module.fallback);
    }

    private boolean isModuleEnabled(EditorModule module) {
        return switch (module) {
            case COLOR -> state.colorDataEnabled;
            case TRANSPARENCY -> state.transparencyDataEnabled;
            case SCALE -> state.scaleDataEnabled;
            case LENGTH -> state.lengthDataEnabled;
            case SPIN -> state.spinDataEnabled;
            case SCREENSHAKE -> state.screenshakeEnabled;
        };
    }

    private void setModuleEnabled(EditorModule module, boolean enabled) {
        switch (module) {
            case COLOR -> state.colorDataEnabled = enabled;
            case TRANSPARENCY -> state.transparencyDataEnabled = enabled;
            case SCALE -> state.scaleDataEnabled = enabled;
            case LENGTH -> state.lengthDataEnabled = enabled;
            case SPIN -> state.spinDataEnabled = enabled;
            case SCREENSHAKE -> state.screenshakeEnabled = enabled;
        }
        if (!enabled) {
            FloatingTabPanel panel = findFloatingPanel(module);
            if (panel != null) {
                floatingPanels.remove(panel);
            }
        }
    }

    private FloatingTabPanel detachTab(ParticleEditorTab targetTab, int mouseX, int mouseY) {
        FloatingTabPanel panel = findFloatingPanel(targetTab);
        if (panel == null) {
            int panelW = Math.min(430, Math.max(260, width - 32));
            int panelH = Math.min(360, Math.max(220, height - 32));
            panel = new FloatingTabPanel(targetTab, mouseX - panelW / 2, mouseY - 12, panelW, panelH);
            floatingPanels.add(panel);
        }
        panel.x = mouseX - panel.w / 2;
        panel.y = mouseY - 12;
        clampFloatingPanel(panel);
        bringFloatingPanelToFront(panel);
        cancelEditing();
        return panel;
    }

    private FloatingTabPanel detachModule(EditorModule module, int mouseX, int mouseY) {
        FloatingTabPanel panel = findFloatingPanel(module);
        if (panel == null) {
            int panelW = Math.min(360, Math.max(260, width - 32));
            int panelH = Math.min(320, Math.max(200, height - 32));
            panel = new FloatingTabPanel(module, mouseX - panelW / 2, mouseY - 12, panelW, panelH);
            floatingPanels.add(panel);
        }
        panel.x = mouseX - panel.w / 2;
        panel.y = mouseY - 12;
        clampFloatingPanel(panel);
        bringFloatingPanelToFront(panel);
        cancelEditing();
        return panel;
    }

    private FloatingTabPanel detachCategory(String key, String label, Consumer<List<Entry>> builder, int mouseX, int mouseY) {
        if (builder == null) {
            CategoryDefinition definition = categoryDefinitions.get(key);
            if (definition != null) {
                label = definition.label();
                builder = definition.builder();
            }
        }
        if (builder == null) {
            return null;
        }
        FloatingTabPanel panel = findFloatingPanel(key);
        if (panel == null) {
            int panelW = Math.min(360, Math.max(260, width - 32));
            int panelH = Math.min(320, Math.max(200, height - 32));
            panel = new FloatingTabPanel(key, label, builder, mouseX - panelW / 2, mouseY - 12, panelW, panelH);
            floatingPanels.add(panel);
        }
        panel.x = mouseX - panel.w / 2;
        panel.y = mouseY - 12;
        clampFloatingPanel(panel);
        bringFloatingPanelToFront(panel);
        cancelEditing();
        return panel;
    }

    private FloatingTabPanel findFloatingPanel(ParticleEditorTab targetTab) {
        for (FloatingTabPanel panel : floatingPanels) {
            if (panel.tab == targetTab) {
                return panel;
            }
        }
        return null;
    }

    private FloatingTabPanel findFloatingPanel(String categoryKey) {
        for (FloatingTabPanel panel : floatingPanels) {
            if (categoryKey.equals(panel.categoryKey)) {
                return panel;
            }
        }
        return null;
    }

    private FloatingTabPanel findFloatingPanel(EditorModule module) {
        for (FloatingTabPanel panel : floatingPanels) {
            if (panel.module == module) {
                return panel;
            }
        }
        return null;
    }

    private FloatingTabPanel floatingPanelAt(double mouseX, double mouseY) {
        for (int i = floatingPanels.size() - 1; i >= 0; i--) {
            FloatingTabPanel panel = floatingPanels.get(i);
            if (panel.contains(mouseX, mouseY)) {
                return panel;
            }
        }
        return null;
    }

    private void bringFloatingPanelToFront(FloatingTabPanel panel) {
        if (floatingPanels.remove(panel)) {
            floatingPanels.add(panel);
        }
    }

    private void clampFloatingPanel(FloatingTabPanel panel) {
        int maxW = Math.max(1, width);
        int maxH = Math.max(1, height);
        panel.w = Mth.clamp(panel.w, Math.min(MIN_FLOATING_PANEL_WIDTH, maxW), maxW);
        panel.h = Mth.clamp(panel.h, Math.min(MIN_FLOATING_PANEL_HEIGHT, maxH), maxH);
        panel.x = Mth.clamp(panel.x, 0, Math.max(0, width - panel.w));
        panel.y = Mth.clamp(panel.y, 0, Math.max(0, height - panel.h));
        panel.scroll = clampScroll(panel.scroll, panel.contentHeight, panel.bodyHeight());
    }

    private int clampScroll(int scrollValue, int totalContentHeight, int viewportHeight) {
        return Mth.clamp(scrollValue, 0, Math.max(0, totalContentHeight - viewportHeight));
    }

    private void clampPanelPosition() {
        panelX = Mth.clamp(panelX, 0, Math.max(0, width - Math.min(MIN_PANEL_WIDTH, width)));
        panelY = Mth.clamp(panelY, 0, Math.max(0, height - Math.min(MIN_PANEL_HEIGHT, height)));
    }

    private boolean panelHeaderContains(double mouseX, double mouseY) {
        return inBounds(mouseX, mouseY, lastPanelX, lastPanelY, lastPanelW, 30);
    }

    private boolean resizeHandleContains(double mouseX, double mouseY) {
        return mouseX >= lastPanelX + lastPanelW - RESIZE_HANDLE_SIZE &&
                mouseX <= lastPanelX + lastPanelW &&
                mouseY >= lastPanelY + lastPanelH - RESIZE_HANDLE_SIZE &&
                mouseY <= lastPanelY + lastPanelH;
    }

    private boolean projectBrowserContains(double mouseX, double mouseY) {
        return projectBrowserVisible && inBounds(mouseX, mouseY, lastProjectBrowserX, lastProjectBrowserY, lastProjectBrowserW, lastProjectBrowserH);
    }

    private String format(double value) {
        return String.format(Locale.ROOT, "%.4f", value).replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    private <T> int indexOf(T[] values, T value) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] == value) {
                return i;
            }
        }
        return 0;
    }

}
