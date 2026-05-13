package team.lodestar.lodestone.systems.particle.editor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;
import team.lodestar.lodestone.systems.particle.editor.data.*;
import team.lodestar.lodestone.systems.particle.editor.api.*;
import team.lodestar.lodestone.systems.particle.editor.preview.OverlayRenderer;
import team.lodestar.lodestone.systems.particle.editor.preview.PreviewController;
import team.lodestar.lodestone.systems.particle.editor.project.*;
import team.lodestar.lodestone.systems.particle.editor.ui.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static team.lodestar.lodestone.systems.particle.editor.ui.EditorUi.*;

public class ParticleEditorScreen extends Screen {
    public static final int PANEL = 0xD014101D;
    public static final int PANEL_LIGHT = 0xE0201830;
    public static final int ROW = 0x7041305A;
    public static final int ROW_HOVER = 0x906B4C9A;
    public static final int TEXT = 0xFFF4EEFF;
    public static final int MUTED = 0xFFB8A9CC;
    public static final int ACCENT = 0xFFD49CFF;
    public static final int WARNING = 0xFFFFD166;
    public static final int ROW_HEIGHT = 18;
    public static final int ROW_GAP = 3;
    public static final int CONTROL_SIZE = 16;
    public static final int CONTROL_GAP = 3;
    private static final int DRAG_STEP_PIXELS = 6;
    private static final int DEFAULT_PANEL_WIDTH = 620;
    private static final int MIN_PANEL_WIDTH = 260;
    private static final int MIN_PANEL_HEIGHT = 260;
    public static final int MIN_FLOATING_PANEL_WIDTH = 240;
    public static final int MIN_FLOATING_PANEL_HEIGHT = 160;
    private static final int RESIZE_HANDLE_SIZE = 14;
    private static final int CONTENT_BOTTOM_PADDING = RESIZE_HANDLE_SIZE + 8;
    private static final int SIDEBAR_WIDTH = 34;
    private static final int PROJECT_BROWSER_MIN_WIDTH = 146;
    private static final int PROJECT_BROWSER_MAX_WIDTH = 210;
    private static final List<EditorProject> SAVED_PROJECTS = new ArrayList<>();
    private static final Set<String> DEFAULT_COLLAPSED_CATEGORIES = defaultCollapsedCategories();
    public static final Set<String> COLLAPSED_CATEGORIES = new HashSet<>(DEFAULT_COLLAPSED_CATEGORIES);
    private static int savedActiveProjectIndex;
    private static int savedLoadProjectIndex = -1;
    private static EditorTab savedTab = EditorTab.EMITTER;
    private static int savedPanelX = 16;
    private static int savedPanelY = 18;
    private static int savedPanelWidth = DEFAULT_PANEL_WIDTH;
    private static int savedPanelHeight;
    private static LivePreviewScope savedLivePreviewScope = LivePreviewScope.ACTIVE;
    private static boolean savedProjectPickerSeen;

    private EditorState state;
    public final List<EditorProject> projects;
    private final PreviewController previewController;
    private final List<RenderedEntry> renderedEntries = new ArrayList<>();
    private final List<ClickZone> clickZones = new ArrayList<>();
    private final EntryListRenderer entryListRenderer = new EntryListRenderer(this, renderedEntries, clickZones);
    private final ProjectRenderer projectRenderer = new ProjectRenderer(this, clickZones);
    private final List<TabButton> renderedTabs = new ArrayList<>();
    public final List<FloatingTabPanel> floatingPanels = new ArrayList<>();
    private final FloatingPanels floatingPanelController = new FloatingPanels(this, floatingPanels);
    public final List<ProjectStorage.LoadedProject> loadPickerProjects = new ArrayList<>();
    public final Set<Integer> loadPickerSelection = new HashSet<>();
    public final Map<String, CategoryDefinition> categoryDefinitions = new HashMap<>();
    private final EntryFactory entryFactory = new EntryFactory();
    private final EditorContext editorContext = new EditorContext(this, entryFactory);
    private final TabEntries tabEntries = new TabEntries(this, editorContext, entryFactory);

    private EditorTab tab = EditorTab.EMITTER;
    public EditorTab buildingTab;
    public LivePreviewScope livePreviewScope = savedLivePreviewScope;
    private int scroll;
    public int projectListScroll;
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
    public boolean projectBrowserVisible;
    public int activeProjectIndex;
    private int loadProjectIndex = -1;
    public boolean projectPickerOpen;
    public boolean loadPickerOpen;
    public boolean editingProjectName;
    public int loadPickerScroll;
    public String newProjectName = "particle_1";
    public int editingListProjectIndex = -1;
    public String editingListProjectName = "";

    private static Set<String> defaultCollapsedCategories() {
        Set<String> categories = new HashSet<>(Set.of(
                "projects",
                "source",
                "render",
                "lifetime",
                "preview",
                "motion.velocity",
                "motion.randomness",
                "motion.spawn_area",
                "motion.physics",
                "motion.behavior_data"
        ));
        categories.addAll(EditorGroups.defaultCollapsedCategories());
        return Set.copyOf(categories);
    }

    public ParticleEditorScreen() {
        super(Component.translatable("screen.lodestone.particle_editor.title"));
        ensureSavedWorkspace();
        projects = SAVED_PROJECTS;
        previewController = new PreviewController(this, projects);
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
        OverlayRenderer.setActiveScreen(this);
        previewController.init();
        scroll = 0;
    }

    @Override
    public void tick() {
        previewController.tickParticles();
        if (projectPickerOpen || loadPickerOpen) {
            return;
        }
        previewController.handleRealtimePreviewChanges();
        previewController.tickLivePreview();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fill(0, 0, width, height, 0x22000000);
        previewController.renderScreenParticles(graphics);
        previewController.renderScreenShapeOverlays(graphics);
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
            projectRenderer.renderProjectBrowser(font, graphics, lastProjectBrowserX, lastProjectBrowserY, lastProjectBrowserW, lastProjectBrowserH, mouseX, mouseY);
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
            projectRenderer.renderProjectPicker(font, graphics, width, height, mouseX, mouseY);
        }
        if (loadPickerOpen) {
            clickZones.clear();
            projectRenderer.renderLoadPicker(font, graphics, width, height, mouseX, mouseY);
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

    private void renderSidebarTabs(GuiGraphics graphics, int x, int y, int w, int h, int mouseX, int mouseY) {
        graphics.fill(x, y, x + w, y + h, 0x80101520);
        List<EditorTab> tabs = EditorRegistry.tabs();
        int tabH = 29;
        int gap = 5;
        int ty = y + 8;
        for (int i = 0; i < tabs.size(); i++) {
            EditorTab targetTab = tabs.get(i);
            int tx = x + 4;
            int tw = w - 8;
            if (ty + tabH > y + h) {
                continue;
            }
            boolean selected = tab.equals(targetTab);
            boolean hovered = inBounds(mouseX, mouseY, tx, ty, tw, tabH);
            boolean floating = findFloatingPanel(targetTab) != null;
            graphics.fill(tx, ty, tx + tw, ty + tabH, selected ? 0xB03A5368 : hovered ? ROW_HOVER : ROW);
            if (floating) {
                graphics.fill(tx + tw - 3, ty, tx + tw, ty + tabH, ACCENT);
            }
            drawCentered(graphics, targetTab.icon, tx, ty + 10, tw, selected ? ACCENT : TEXT);
            renderedTabs.add(new TabButton(targetTab, tx, ty, tw, tabH));
            ty += tabH + gap;
        }
    }

    private void renderFloatingPanels(GuiGraphics graphics, int mouseX, int mouseY) {
        for (FloatingTabPanel panel : floatingPanels) {
            clampFloatingPanel(panel);
            graphics.fill(panel.x, panel.y, panel.x + panel.w, panel.y + panel.h, PANEL);
            graphics.fill(panel.x, panel.y, panel.x + panel.w, panel.y + FloatingTabPanel.HEADER_HEIGHT, PANEL_LIGHT);
            String title = panel.group != null ? groupLabel(panel.group) : panel.categoryLabel != null ? panel.categoryLabel : panel.tab.label;
            graphics.drawString(font, trimToWidth(title, Math.max(24, panel.w - FloatingTabPanel.DOCK_WIDTH - 22)), panel.x + 8, panel.y + 8, TEXT, false);

            boolean dockHovered = panel.dockContains(mouseX, mouseY);
            graphics.fill(panel.dockX(), panel.y + 5, panel.dockX() + FloatingTabPanel.DOCK_WIDTH, panel.y + 22, dockHovered ? ROW_HOVER : ROW);
            drawCentered(graphics, editorText("action.dock", "Dock"), panel.dockX(), panel.y + 10, FloatingTabPanel.DOCK_WIDTH, dockHovered ? ACCENT : TEXT);

            int bodyX = panel.x + 8;
            int bodyY = panel.y + FloatingTabPanel.HEADER_HEIGHT + 8;
            int bodyW = panel.w - 16;
            int bodyH = panel.h - FloatingTabPanel.HEADER_HEIGHT - CONTENT_BOTTOM_PADDING;
            if (panel.group != null) {
                List<Entry> entries = buildGroupEntries(panel.group);
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

    private int renderEntries(GuiGraphics graphics, EditorTab targetTab, int x, int y, int w, int h, int mouseX, int mouseY, int scrollValue, FloatingTabPanel owner) {
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
        return entryListRenderer.renderEntryList(graphics, font, entries, x, y, w, h, mouseX, mouseY, scrollValue, owner, editingEntry, editingText);
    }

    private int measureContentHeight(List<Entry> entries) {
        return entryListRenderer.measureContentHeight(entries);
    }

    private List<Entry> buildEntries(EditorTab targetTab) {
        return tabEntries.buildEntries(targetTab);
    }

    private void refreshCategoryDefinitions() {
        tabEntries.refreshCategoryDefinitions();
    }

    public void toggleScaleLengthLink() {
        state.scaleLengthLinked = !state.scaleLengthLinked;
        if (state.scaleLengthLinked) {
            editorContext.curves().setScaleLengthLinkedValue(state.scale.curve.start);
        }
        onStateEdited();
    }

    private List<Entry> buildGroupEntries(EditorDataGroup<?> group) {
        return tabEntries.buildGroupEntries(group);
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
                editingListProjectName += ProjectNames.sanitizeInput(minecraft.keyboardHandler.getClipboard());
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
                    newProjectName += ProjectNames.sanitizeInput(minecraft.keyboardHandler.getClipboard());
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
        if (editingListProjectIndex >= 0 && ProjectNames.isAllowedNameChar(codePoint)) {
            editingListProjectName += codePoint;
            return true;
        }
        if (projectPickerOpen && editingProjectName && ProjectNames.isAllowedNameChar(codePoint)) {
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

    public EditorProject activeProject() {
        if (projects.isEmpty()) {
            state = new EditorState();
            projects.add(new EditorProject("particle_1", state));
            activeProjectIndex = 0;
        }
        activeProjectIndex = Mth.clamp(activeProjectIndex, 0, projects.size() - 1);
        return projects.get(activeProjectIndex);
    }

    private static void ensureSavedWorkspace() {
        if (SAVED_PROJECTS.isEmpty()) {
            EditorState initial = new EditorState();
            SAVED_PROJECTS.add(new EditorProject("particle_1", initial));
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

    public void continueCurrentProject() {
        projectPickerOpen = false;
        editingProjectName = false;
        savedProjectPickerSeen = true;
        previewController.captureSnapshot();
        refreshLivePreview();
        saveWorkspaceMemory();
    }

    public void createFreshProject() {
        cancelProjectNameEditing();
        previewController.clearPreviewParticles();
        EditorState newState = new EditorState();
        previewController.initializeFixedPreviewPosition(newState);
        String name = ProjectNames.sanitize(newProjectName);
        projects.clear();
        projects.add(new EditorProject(name, newState));
        activeProjectIndex = 0;
        loadProjectIndex = -1;
        state = newState;
        tab = EditorTab.EMITTER;
        scroll = 0;
        floatingPanels.clear();
        projectPickerOpen = false;
        editingProjectName = false;
        savedProjectPickerSeen = true;
        newProjectName = ProjectNames.unique("particle_2", projects);
        previewController.captureSnapshot();
        saveWorkspaceMemory();
        saveAllProjects();
        refreshLivePreview();
        message("New particle file created: " + name);
    }

    public void openProjectFolder() {
        try {
            cancelProjectNameEditing();
            List<Path> paths = ProjectStorage.listProjects(Minecraft.getInstance());
            if (paths.isEmpty()) {
                openProjectsFolder();
                message("No particle projects yet. Folder opened");
                return;
            }
            loadPickerProjects.clear();
            loadPickerSelection.clear();
            for (Path path : paths) {
                loadPickerProjects.add(ProjectStorage.loadProject(path));
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

    public void loadSelectedProjects(boolean append) {
        if (loadPickerSelection.isEmpty()) {
            message("Select at least one particle file");
            return;
        }
        int selectedCount = loadPickerSelection.size();
        previewController.clearPreviewParticles();
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
                ProjectStorage.LoadedProject loaded = loadPickerProjects.get(i);
                EditorState loadedState = loaded.state().copy();
                previewController.initializeFixedPreviewPosition(loadedState);
                projects.add(new EditorProject(ProjectNames.unique(loaded.name(), projects), loadedState));
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

    public void toggleLoadSelection(int index) {
        if (loadPickerSelection.contains(index)) {
            loadPickerSelection.remove(index);
        } else {
            loadPickerSelection.add(index);
        }
    }

    public void deleteLoadedProject(int index) {
        if (index < 0 || index >= loadPickerProjects.size()) {
            return;
        }
        ProjectStorage.LoadedProject project = loadPickerProjects.get(index);
        try {
            ProjectStorage.deleteProject(project.path());
            loadPickerProjects.remove(index);
            Set<Integer> shiftedSelection = ProjectNames.shiftSelectionAfterRemoval(loadPickerSelection, index);
            loadPickerSelection.clear();
            loadPickerSelection.addAll(shiftedSelection);
            loadPickerScroll = Mth.clamp(loadPickerScroll, 0, Math.max(0, loadPickerProjects.size() - 1));
            message("Deleted saved particle project: " + project.name());
        } catch (IOException | RuntimeException exception) {
            message("Failed to delete particle project: " + exception.getMessage());
        }
    }

    public void selectAllLoadProjects() {
        loadPickerSelection.clear();
        for (int i = 0; i < loadPickerProjects.size(); i++) {
            loadPickerSelection.add(i);
        }
    }

    public void closeLoadPicker() {
        loadPickerOpen = false;
        loadPickerProjects.clear();
        loadPickerSelection.clear();
        loadPickerScroll = 0;
    }

    public void addProject() {
        commitProjectNameEditing();
        EditorState newState = new EditorState();
        previewController.initializeFixedPreviewPosition(newState);
        EditorProject project = new EditorProject(ProjectNames.unique("particle_" + (projects.size() + 1), projects), newState);
        projects.add(project);
        selectProject(projects.size() - 1);
        projectListScroll = maxProjectListScroll();
        message("Added particle project: " + project.name);
    }

    public void removeProject(int index) {
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

    public void clickProjectName(int index) {
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
            EditorProject project = projects.get(editingListProjectIndex);
            String oldName = project.name;
            project.name = ProjectNames.unique(ProjectNames.sanitize(editingListProjectName), projects, editingListProjectIndex);
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

    public void toggleProjectVisibility(int index) {
        if (index < 0 || index >= projects.size()) {
            return;
        }
        commitProjectNameEditing();
        EditorProject project = projects.get(index);
        project.visible = !project.visible;
        previewController.clearPreviewParticles();
        refreshLivePreview();
        message((project.visible ? "Shown particle: " : "Hidden particle: ") + project.name);
    }

    public void openProjectsFolder() {
        try {
            ProjectStorage.openRootFolder(Minecraft.getInstance());
            message("Opened projects folder");
        } catch (IOException | RuntimeException exception) {
            message("Failed to open projects folder: " + exception.getMessage());
        }
    }

    public void selectProject(int index) {
        if (projects.isEmpty()) {
            return;
        }
        if (editingListProjectIndex >= 0 && Math.floorMod(index, projects.size()) != editingListProjectIndex) {
            commitProjectNameEditing();
        }
        activeProjectIndex = Math.floorMod(index, projects.size());
        state = activeProject().state;
        scroll = 0;
        previewController.captureSnapshot();
        keepActiveProjectVisible();
        saveWorkspaceMemory();
        refreshLivePreview();
    }

    public int visibleProjectRows() {
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

    public void saveAllProjects() {
        int saved = 0;
        try {
            for (EditorProject project : projects) {
                ProjectStorage.saveProject(Minecraft.getInstance(), project);
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

    public void cancelEditing() {
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

    public void spawnPreviewWithEffects() {
        previewController.spawnPreviewWithEffects();
    }

    public void spawnAllPreviews() {
        previewController.spawnAllPreviews();
    }

    public void playScreenshakePreview() {
        previewController.playScreenshakePreview();
    }

    public void refreshLivePreview() {
        previewController.refreshLivePreview();
    }

    public void onStateEdited() {
        previewController.handleRealtimePreviewChanges();
    }

    public void renderWorldShapeOverlays(com.mojang.blaze3d.vertex.PoseStack poseStack, net.minecraft.client.Camera camera) {
        previewController.renderWorldShapeOverlays(poseStack, camera);
    }

    public Vec3 calculateCameraPreviewPosition() {
        return previewController.calculateCameraPreviewPosition();
    }

    @Override
    public void onClose() {
        OverlayRenderer.clearActiveScreen(this);
        saveWorkspaceMemory();
        previewController.onClose(!projectPickerOpen && minecraft != null && minecraft.level != null && minecraft.player != null);
        super.onClose();
    }

    public void message(String text) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            client.player.displayClientMessage(Component.literal(text), true);
        }
    }

    public EditorState state() {
        return state;
    }

    public String editorText(String key, String fallback) {
        String translationKey = "screen.lodestone.particle_editor." + key;
        String translated = Component.translatable(translationKey).getString();
        return translated.equals(translationKey) ? fallback : translated;
    }

    private void drawCentered(GuiGraphics graphics, String text, int x, int y, int width, int color) {
        EditorUi.drawCentered(font, graphics, text, x, y, width, color);
    }

    private String trimToWidth(String text, int maxWidth) {
        return EditorUi.trimToWidth(font, text, maxWidth);
    }

    private String groupLabel(EditorDataGroup<?> group) {
        return editorText(group.translationKey(), group.fallback());
    }

    private FloatingTabPanel detachTab(EditorTab targetTab, int mouseX, int mouseY) {
        return floatingPanelController.detachTab(targetTab, mouseX, mouseY);
    }

    public FloatingTabPanel detachGroup(EditorDataGroup<?> group, int mouseX, int mouseY) {
        return floatingPanelController.detachGroup(group, mouseX, mouseY);
    }

    public FloatingTabPanel detachCategory(String key, String label, Consumer<List<Entry>> builder, int mouseX, int mouseY) {
        return floatingPanelController.detachCategory(key, label, builder, mouseX, mouseY);
    }

    private FloatingTabPanel findFloatingPanel(EditorTab targetTab) {
        return floatingPanelController.find(targetTab);
    }

    public FloatingTabPanel findFloatingPanel(String categoryKey) {
        return floatingPanelController.find(categoryKey);
    }

    public FloatingTabPanel findFloatingPanel(EditorDataGroup<?> group) {
        return floatingPanelController.find(group);
    }

    private FloatingTabPanel floatingPanelAt(double mouseX, double mouseY) {
        return floatingPanelController.at(mouseX, mouseY);
    }

    private void bringFloatingPanelToFront(FloatingTabPanel panel) {
        floatingPanelController.bringToFront(panel);
    }

    private void clampFloatingPanel(FloatingTabPanel panel) {
        floatingPanelController.clamp(panel);
    }

    public int clampScroll(int scrollValue, int totalContentHeight, int viewportHeight) {
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

}
