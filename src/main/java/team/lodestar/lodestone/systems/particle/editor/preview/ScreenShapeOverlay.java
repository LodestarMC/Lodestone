package team.lodestar.lodestone.systems.particle.editor.preview;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.systems.particle.editor.EditorState;
import team.lodestar.lodestone.systems.particle.editor.data.ParticleOutputMode;
import team.lodestar.lodestone.systems.particle.editor.data.SpawnDistributionMode;
import team.lodestar.lodestone.systems.particle.editor.project.EditorProject;

import java.util.List;

class ScreenShapeOverlay {
    private ScreenShapeOverlay() {
    }

    static void render(GuiGraphics graphics, List<EditorProject> projects) {
        int index = 0;
        for (EditorProject project : projects) {
            EditorState state = project.state;
            if (project.visible && state.shapeOverlay && state.outputMode == ParticleOutputMode.SCREEN) {
                renderShape(graphics, state, OverlayRenderer.overlayColor(index));
                index++;
            }
        }
    }

    private static void renderShape(GuiGraphics graphics, EditorState state, int color) {
        double x = state.screenTrackStack ? ScreenParticleHandler.currentItemX + state.stackTrackXOffset : state.screenX;
        double y = state.screenTrackStack ? ScreenParticleHandler.currentItemY + state.stackTrackYOffset : state.screenY;
        double rx = state.spawnDistribution == SpawnDistributionMode.BUILDER_RANDOM ? state.randomOffsetX : state.shapeSizeX;
        double ry = state.spawnDistribution == SpawnDistributionMode.BUILDER_RANDOM ? state.randomOffsetY : state.shapeSizeY;
        rx = Math.max(2.0, rx);
        ry = Math.max(2.0, ry);

        if (state.spawnDistribution == SpawnDistributionMode.POINT) {
            drawLine(graphics, x - 5, y, x + 5, y, color);
            drawLine(graphics, x, y - 5, x, y + 5, color);
            return;
        }
        if (state.spawnDistribution == SpawnDistributionMode.CUBE || state.spawnDistribution == SpawnDistributionMode.BUILDER_RANDOM) {
            drawLine(graphics, x - rx, y - ry, x + rx, y - ry, color);
            drawLine(graphics, x + rx, y - ry, x + rx, y + ry, color);
            drawLine(graphics, x + rx, y + ry, x - rx, y + ry, color);
            drawLine(graphics, x - rx, y + ry, x - rx, y - ry, color);
            return;
        }

        double previousX = 0.0;
        double previousY = 0.0;
        boolean hasPrevious = false;
        int segments = 48;
        for (int i = 0; i <= segments; i++) {
            double angle = i / (double) segments * Math.PI * 2.0;
            double pointX = x + Math.cos(angle) * rx;
            double pointY = y + Math.sin(angle) * ry;
            if (hasPrevious) {
                drawLine(graphics, previousX, previousY, pointX, pointY, color);
            }
            previousX = pointX;
            previousY = pointY;
            hasPrevious = true;
        }
    }

    private static void drawLine(GuiGraphics graphics, double x1, double y1, double x2, double y2, int color) {
        int steps = Math.max(1, (int) Math.ceil(Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1))));
        for (int i = 0; i <= steps; i++) {
            double progress = i / (double) steps;
            int x = Mth.floor(Mth.lerp(progress, x1, x2));
            int y = Mth.floor(Mth.lerp(progress, y1, y2));
            graphics.fill(x, y, x + 1, y + 1, color);
        }
    }
}
