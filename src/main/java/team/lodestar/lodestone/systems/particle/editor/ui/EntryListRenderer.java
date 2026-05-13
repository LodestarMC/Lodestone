package team.lodestar.lodestone.systems.particle.editor.ui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import team.lodestar.lodestone.systems.particle.editor.ParticleEditorScreen;
import team.lodestar.lodestone.systems.particle.editor.EditorState;

import java.util.List;

import static team.lodestar.lodestone.systems.particle.editor.ParticleEditorScreen.*;
import static team.lodestar.lodestone.systems.particle.editor.ui.EditorUi.*;

public final class EntryListRenderer {
    private final ParticleEditorScreen screen;
    private final List<RenderedEntry> renderedEntries;
    private final List<ClickZone> clickZones;

    public EntryListRenderer(ParticleEditorScreen screen, List<RenderedEntry> renderedEntries, List<ClickZone> clickZones) {
        this.screen = screen;
        this.renderedEntries = renderedEntries;
        this.clickZones = clickZones;
    }

    public int renderEntryList(GuiGraphics graphics, Font font, List<Entry> entries, int x, int y, int w, int h, int mouseX, int mouseY, int scrollValue, FloatingTabPanel owner, Entry editingEntry, String editingText) {
        int measuredContentHeight = measureContentHeight(entries);
        scrollValue = screen.clampScroll(scrollValue, measuredContentHeight, h);
        int viewportTop = y;
        int viewportBottom = y + h;
        int rowY = y - scrollValue;

        graphics.enableScissor(x, viewportTop, x + w, viewportBottom);
        for (Entry entry : entries) {
            int entryHeight = entryHeight(entry);
            if (rowY + entryHeight >= viewportTop && rowY <= viewportBottom) {
                renderEntry(graphics, font, entry, x, rowY, w, entryHeight, mouseX, mouseY, owner, viewportTop, viewportBottom, editingEntry, editingText);
            }
            rowY += entryHeight + ROW_GAP;
        }
        graphics.flush();
        graphics.disableScissor();
        return scrollValue;
    }

    public int measureContentHeight(List<Entry> entries) {
        int height = 0;
        for (Entry entry : entries) {
            height += entryHeight(entry) + ROW_GAP;
        }
        return Math.max(0, height);
    }

    private int entryHeight(Entry entry) {
        return entry.header ? 16 : ROW_HEIGHT;
    }

    private void renderEntry(GuiGraphics graphics, Font font, Entry entry, int x, int y, int w, int h, int mouseX, int mouseY, FloatingTabPanel owner, int clipTop, int clipBottom, Entry editingEntry, String editingText) {
        if (entry.header) {
            renderHeader(graphics, font, entry, x, y, w, h, mouseX, mouseY, owner, clipTop, clipBottom);
            return;
        }
        if (entry instanceof ColorChannelsEntry colorChannelsEntry) {
            renderColorChannelsEntry(graphics, font, colorChannelsEntry, x, y, w, h, mouseX, mouseY, owner, clipTop, clipBottom, editingEntry, editingText);
            return;
        }
        if (entry instanceof ShapeSizeEntry shapeSizeEntry) {
            renderShapeSizeEntry(graphics, font, shapeSizeEntry, x, y, w, h, mouseX, mouseY, owner, clipTop, clipBottom, editingEntry, editingText);
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
        String label = trimToWidth(font, entry.label, labelW);
        graphics.drawString(font, label, x + 6, y + 5, TEXT, false);
        String trimmed = trimToWidth(font, value, valueW - 8);
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
            renderStepperControls(graphics, font, x, y, w, h, mouseX, mouseY);
        }
        renderedEntries.add(new RenderedEntry(entry, x, y, w, h, owner, clipTop, clipBottom));
    }

    private void renderHeader(GuiGraphics graphics, Font font, Entry entry, int x, int y, int w, int h, int mouseX, int mouseY, FloatingTabPanel owner, int clipTop, int clipBottom) {
        boolean hovered = inBounds(mouseX, mouseY, x, Math.max(y, clipTop), w, Math.min(y + h, clipBottom) - Math.max(y, clipTop));
        graphics.fill(x, y, x + w, y + h, hovered ? 0x70455A72 : 0x40151B27);
        boolean categoryCanDetach = entry instanceof CategoryEntry category && category.builder != null;
        boolean optionalCanDetach = entry instanceof OptionalCategoryEntry optional && optional.canDetach();
        boolean categoryCanLink = entry instanceof CategoryEntry linkCategory && CurveEntries.isScaleLengthLinkKey(linkCategory.key);
        boolean optionalCanLink = entry instanceof OptionalCategoryEntry optionalLink && CurveEntries.isScaleLengthLinkKey(optionalLink.key());
        boolean canLink = categoryCanLink || optionalCanLink;
        int detachW = categoryCanDetach || optionalCanDetach ? 18 : 0;
        int linkW = canLink ? 18 : 0;
        graphics.drawString(font, entry.value(), x + 6, y + 5, hovered ? WARNING : MUTED, false);
        graphics.drawString(font, trimToWidth(font, entry.label, w - 26 - detachW - linkW), x + 20, y + 5, hovered ? WARNING : ACCENT, false);
        if (canLink) {
            int linkX = x + w - detachW - linkW - 3;
            int linkH = Math.min(14, Math.max(1, Math.min(y + h, clipBottom) - Math.max(y + 1, clipTop)));
            int linkY = Math.max(y + 1, clipTop);
            boolean linkHovered = inBounds(mouseX, mouseY, linkX, linkY, linkW, linkH);
            graphics.fill(linkX, linkY, linkX + linkW, linkY + linkH, linkHovered ? ROW_HOVER : 0x80404A5F);
            drawMagnetIcon(graphics, linkX, linkY, linkW, linkH, screen.state().scaleLengthLinked, linkHovered ? ACCENT : TEXT);
            clickZones.add(new ClickZone(linkX, linkY, linkW, linkH, screen::toggleScaleLengthLink, owner));
        }
        if (categoryCanDetach || optionalCanDetach) {
            int detachX = x + w - detachW - 2;
            int detachH = Math.min(14, Math.max(1, Math.min(y + h, clipBottom) - Math.max(y + 1, clipTop)));
            int detachY = Math.max(y + 1, clipTop);
            boolean detachHovered = inBounds(mouseX, mouseY, detachX, detachY, detachW, detachH);
            graphics.fill(detachX, detachY, detachX + detachW, detachY + detachH, detachHovered ? ROW_HOVER : 0x80404A5F);
            drawVerticalDots(graphics, detachX, detachY, detachW, detachH, detachHovered ? ACCENT : TEXT);
            if (entry instanceof OptionalCategoryEntry optional) {
                clickZones.add(new ClickZone(detachX, detachY, detachW, detachH, () -> screen.detachGroup(optional.group, x + w, y), owner));
            } else if (entry instanceof CategoryEntry category) {
                clickZones.add(new ClickZone(detachX, detachY, detachW, detachH, () -> screen.detachCategory(category.key, category.label, category.builder, x + w, y), owner));
            }
        }
        renderedEntries.add(new RenderedEntry(entry, x, y, w, h, owner, clipTop, clipBottom));
    }

    private void renderStepperControls(GuiGraphics graphics, Font font, int x, int y, int w, int h, int mouseX, int mouseY) {
        int minusX = x + w - CONTROL_SIZE * 2 - CONTROL_GAP * 2;
        int plusX = x + w - CONTROL_SIZE - CONTROL_GAP;
        boolean minusHovered = inBounds(mouseX, mouseY, minusX, y + 1, CONTROL_SIZE, h - 2);
        boolean plusHovered = inBounds(mouseX, mouseY, plusX, y + 1, CONTROL_SIZE, h - 2);
        graphics.fill(minusX, y + 1, minusX + CONTROL_SIZE, y + h - 1, minusHovered ? ROW_HOVER : 0x80404A5F);
        graphics.fill(plusX, y + 1, plusX + CONTROL_SIZE, y + h - 1, plusHovered ? ROW_HOVER : 0x80404A5F);
        drawCentered(font, graphics, "-", minusX, y + 5, CONTROL_SIZE, minusHovered ? ACCENT : TEXT);
        drawCentered(font, graphics, "+", plusX, y + 5, CONTROL_SIZE, plusHovered ? ACCENT : TEXT);
    }

    private void renderColorChannelsEntry(GuiGraphics graphics, Font font, ColorChannelsEntry entry, int x, int y, int w, int h, int mouseX, int mouseY, FloatingTabPanel owner, int clipTop, int clipBottom, Entry editingEntry, String editingText) {
        boolean hovered = inBounds(mouseX, mouseY, x, Math.max(y, clipTop), w, Math.min(y + h, clipBottom) - Math.max(y, clipTop));
        graphics.fill(x, y, x + w, y + h, hovered ? ROW_HOVER : ROW);
        graphics.drawString(font, trimToWidth(font, entry.label, Math.max(40, w - colorChannelAreaWidth(w) - 18)), x + 6, y + 5, TEXT, false);

        int areaW = colorChannelAreaWidth(w);
        int segmentW = (areaW - 6) / 3;
        int startX = x + w - areaW - 6;
        for (int channel = 0; channel < 3; channel++) {
            int sx = startX + channel * (segmentW + 3);
            boolean channelHovered = inBounds(mouseX, mouseY, sx, y + 1, segmentW, h - 2);
            graphics.fill(sx, y + 1, sx + segmentW, y + h - 1, colorChannelBackground(channel, channelHovered));
            String label = entry.channelLabel(channel);
            String value = editingEntry != null && editingEntry.label.equals(label) ? editingText + "_" : String.valueOf(entry.channelValue(channel));
            drawCentered(font, graphics, colorChannelLabel(channel) + " " + trimToWidth(font, value, Math.max(8, segmentW - 14)), sx, y + 5, segmentW, colorChannelText(channel));
        }
        renderedEntries.add(new RenderedEntry(entry, x, y, w, h, owner, clipTop, clipBottom));
    }

    private void renderShapeSizeEntry(GuiGraphics graphics, Font font, ShapeSizeEntry entry, int x, int y, int w, int h, int mouseX, int mouseY, FloatingTabPanel owner, int clipTop, int clipBottom, Entry editingEntry, String editingText) {
        boolean hovered = inBounds(mouseX, mouseY, x, Math.max(y, clipTop), w, Math.min(y + h, clipBottom) - Math.max(y, clipTop));
        graphics.fill(x, y, x + w, y + h, hovered ? ROW_HOVER : ROW);
        graphics.drawString(font, trimToWidth(font, entry.label, Math.max(40, w - shapeSizeAreaWidth(w, entry.axisCount()) - 18)), x + 6, y + 5, TEXT, false);

        int areaW = shapeSizeAreaWidth(w, entry.axisCount());
        int segmentW = (areaW - (entry.axisCount() - 1) * 3) / entry.axisCount();
        int startX = x + w - areaW - 6;
        for (int axis = 0; axis < entry.axisCount(); axis++) {
            int sx = startX + axis * (segmentW + 3);
            boolean axisHovered = inBounds(mouseX, mouseY, sx, y + 1, segmentW, h - 2);
            graphics.fill(sx, y + 1, sx + segmentW, y + h - 1, shapeAxisBackground(axis, axisHovered));
            String label = entry.axisEntryLabel(axis);
            String value = editingEntry != null && editingEntry.label.equals(label) ? editingText + "_" : EditorState.formatNumber(entry.axisValue(axis));
            drawCentered(font, graphics, entry.axisLabel(axis) + " " + trimToWidth(font, value, Math.max(8, segmentW - 14)), sx, y + 5, segmentW, TEXT);
        }
        renderedEntries.add(new RenderedEntry(entry, x, y, w, h, owner, clipTop, clipBottom));
    }
}
