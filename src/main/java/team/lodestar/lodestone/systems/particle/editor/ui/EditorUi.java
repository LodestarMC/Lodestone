package team.lodestar.lodestone.systems.particle.editor.ui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class EditorUi {
    private static final int ACCENT = 0xFF75D6FF;
    private static final int WARNING = 0xFFFFD166;
    private static final int MUTED = 0xFF9BA9BC;

    private EditorUi() {
    }

    public static void drawCentered(Font font, GuiGraphics graphics, String text, int x, int y, int width, int color) {
        graphics.drawString(font, text, x + (width - font.width(text)) / 2, y, color, false);
    }

    public static void drawVerticalDots(GuiGraphics graphics, int x, int y, int width, int height, int color) {
        int dotSize = 2;
        int dotX = x + width / 2 - dotSize / 2;
        int centerY = y + height / 2 - dotSize / 2;
        graphics.fill(dotX, centerY - 4, dotX + dotSize, centerY - 4 + dotSize, color);
        graphics.fill(dotX, centerY, dotX + dotSize, centerY + dotSize, color);
        graphics.fill(dotX, centerY + 4, dotX + dotSize, centerY + 4 + dotSize, color);
    }

    public static void drawMagnetIcon(GuiGraphics graphics, int x, int y, int width, int height, boolean active, int color) {
        int left = x + width / 2 - 5;
        int top = y + height / 2 - 5;
        int right = left + 10;
        int bottom = top + 10;
        int body = active ? ACCENT : color;
        graphics.fill(left, top, left + 2, bottom - 2, body);
        graphics.fill(right - 2, top, right, bottom - 2, body);
        graphics.fill(left + 2, bottom - 4, right - 2, bottom - 2, body);
        graphics.fill(left, top, left + 4, top + 2, active ? WARNING : MUTED);
        graphics.fill(right - 4, top, right, top + 2, active ? WARNING : MUTED);
    }

    public static String trimToWidth(Font font, String text, int maxWidth) {
        if (font.width(text) <= maxWidth) {
            return text;
        }
        String suffix = "...";
        return font.plainSubstrByWidth(text, Math.max(0, maxWidth - font.width(suffix))) + suffix;
    }

    public static boolean inBounds(double mouseX, double mouseY, int x, int y, int w, int h) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    public static void renderColorSwatch(GuiGraphics graphics, String value, int x, int y, int rowHeight) {
        if (!isHexColor(value)) {
            return;
        }
        int color = Integer.parseUnsignedInt(value.substring(1), 16) & 0xFFFFFF;
        int size = Math.min(10, Math.max(6, rowHeight - 7));
        graphics.fill(x - 1, y - 1, x + size + 1, y + size + 1, 0xFF000000);
        graphics.fill(x, y, x + size, y + size, 0xFF000000 | color);
    }

    public static int colorChannelAreaWidth(int rowWidth) {
        return Math.min(210, Math.max(126, rowWidth - 124));
    }

    public static int shapeSizeAreaWidth(int rowWidth, int axisCount) {
        return Math.min(axisCount == 3 ? 270 : 190, Math.max(axisCount == 3 ? 174 : 116, rowWidth - 124));
    }

    public static int colorChannelAt(RenderedEntry rendered, double mouseX) {
        int areaW = colorChannelAreaWidth(rendered.w);
        int segmentW = (areaW - 6) / 3;
        int startX = rendered.x + rendered.w - areaW - 6;
        for (int channel = 0; channel < 3; channel++) {
            int sx = startX + channel * (segmentW + 3);
            if (mouseX >= sx && mouseX <= sx + segmentW) {
                return channel;
            }
        }
        return -1;
    }

    public static int shapeAxisAt(RenderedEntry rendered, double mouseX, int axisCount) {
        int areaW = shapeSizeAreaWidth(rendered.w, axisCount);
        int segmentW = (areaW - (axisCount - 1) * 3) / axisCount;
        int startX = rendered.x + rendered.w - areaW - 6;
        for (int axis = 0; axis < axisCount; axis++) {
            int sx = startX + axis * (segmentW + 3);
            if (mouseX >= sx && mouseX <= sx + segmentW) {
                return axis;
            }
        }
        return -1;
    }

    public static String colorChannelLabel(int channel) {
        return switch (channel) {
            case 0 -> "R";
            case 1 -> "G";
            default -> "B";
        };
    }

    public static int colorChannelText(int channel) {
        return switch (channel) {
            case 0 -> 0xFFFF8A8A;
            case 1 -> 0xFF8AFF9C;
            default -> 0xFF9CC7FF;
        };
    }

    public static int colorChannelBackground(int channel, boolean hovered) {
        int alpha = hovered ? 0xA0 : 0x66;
        int rgb = switch (channel) {
            case 0 -> 0x7A2020;
            case 1 -> 0x207A32;
            default -> 0x244C9A;
        };
        return alpha << 24 | rgb;
    }

    public static int shapeAxisBackground(int axis, boolean hovered) {
        int alpha = hovered ? 0xA0 : 0x66;
        int rgb = switch (axis) {
            case 0 -> 0x356C9A;
            case 1 -> 0x4D8A4D;
            default -> 0x7A5A9A;
        };
        return alpha << 24 | rgb;
    }

    private static boolean isHexColor(String value) {
        if (value.length() != 7 || value.charAt(0) != '#') {
            return false;
        }
        for (int i = 1; i < value.length(); i++) {
            char c = value.charAt(i);
            if (!Character.isDigit(c) && (c < 'a' || c > 'f') && (c < 'A' || c > 'F')) {
                return false;
            }
        }
        return true;
    }
}
