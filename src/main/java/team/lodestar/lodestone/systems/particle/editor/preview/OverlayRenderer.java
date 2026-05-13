package team.lodestar.lodestone.systems.particle.editor.preview;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import team.lodestar.lodestone.handlers.LodestoneRenderHandler;
import team.lodestar.lodestone.systems.particle.editor.ParticleEditorScreen;
import team.lodestar.lodestone.systems.particle.editor.EditorState;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderType;

import java.util.OptionalDouble;

public final class OverlayRenderer {
    private static final LodestoneRenderType OVERLAY_LINES = new LodestoneRenderType(
            "lodestone_particle_editor_overlay_lines",
            DefaultVertexFormat.POSITION_COLOR_NORMAL,
            VertexFormat.Mode.LINES,
            1536,
            false,
            true,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.RENDERTYPE_LINES_SHADER)
                    .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty()))
                    .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                    .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                    .setCullState(RenderStateShard.NO_CULL)
                    .createCompositeState(false),
            null
    );
    private static final int[] COLORS = {
            0xC0D49CFF,
            0xC0B88CFF,
            0xC0FF9CE8,
            0xC09CA8FF,
            0xC0E6C8FF
    };
    private static ParticleEditorScreen activeScreen;

    private OverlayRenderer() {
    }

    public static void setActiveScreen(ParticleEditorScreen screen) {
        activeScreen = screen;
    }

    public static void clearActiveScreen(ParticleEditorScreen screen) {
        if (activeScreen == screen) {
            activeScreen = null;
        }
    }

    public static void render(Minecraft minecraft, PoseStack poseStack, Camera camera) {
        if (activeScreen == null || minecraft.screen != activeScreen || minecraft.level == null || minecraft.player == null) {
            return;
        }
        activeScreen.renderWorldShapeOverlays(poseStack, camera);
    }

    static int overlayColor(int index) {
        return COLORS[Math.floorMod(index, COLORS.length)];
    }

    static void renderShape(PoseStack poseStack, Camera camera, Vec3 center, EditorState state, int colorIndex) {
        int color = overlayColor(colorIndex);
        int alpha = color >>> 24;
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;
        MultiBufferSource buffer = LodestoneRenderHandler.DEFERRED_RENDER.getTarget();

        poseStack.pushPose();
        Vec3 cameraPos = camera.getPosition();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        drawPoint(buffer, poseStack, center, red, green, blue, Math.min(255, alpha + 40), 0.16);
        switch (state.spawnDistribution) {
            case POINT -> drawPoint(buffer, poseStack, center, red, green, blue, alpha, 0.32);
            case BUILDER_RANDOM -> drawBox(buffer, poseStack, center, state.randomOffsetX, state.randomOffsetY, state.randomOffsetZ, red, green, blue, alpha);
            case CUBE -> drawBox(buffer, poseStack, center, state.shapeSizeX, state.shapeSizeY, state.shapeSizeZ, red, green, blue, alpha);
            case CIRCLE -> {
                drawEllipse(buffer, poseStack, center, state.shapeSizeX, state.shapeSizeZ, Plane.XZ, red, green, blue, alpha);
                drawLine(buffer, poseStack, center.add(-state.shapeSizeX, 0, 0), center.add(state.shapeSizeX, 0, 0), red, green, blue, alpha);
                drawLine(buffer, poseStack, center.add(0, 0, -state.shapeSizeZ), center.add(0, 0, state.shapeSizeZ), red, green, blue, alpha);
            }
            case SPHERE -> {
                drawEllipse(buffer, poseStack, center, state.shapeSizeX, state.shapeSizeZ, Plane.XZ, red, green, blue, alpha);
                drawEllipse(buffer, poseStack, center, state.shapeSizeX, state.shapeSizeY, Plane.XY, red, green, blue, alpha);
                drawEllipse(buffer, poseStack, center, state.shapeSizeZ, state.shapeSizeY, Plane.YZ, red, green, blue, alpha);
            }
        }
        poseStack.popPose();
    }

    private static void drawPoint(MultiBufferSource buffer, PoseStack poseStack, Vec3 center, int red, int green, int blue, int alpha, double size) {
        drawLine(buffer, poseStack, center.add(-size, 0, 0), center.add(size, 0, 0), red, green, blue, alpha);
        drawLine(buffer, poseStack, center.add(0, -size, 0), center.add(0, size, 0), red, green, blue, alpha);
        drawLine(buffer, poseStack, center.add(0, 0, -size), center.add(0, 0, size), red, green, blue, alpha);
    }

    private static void drawBox(MultiBufferSource buffer, PoseStack poseStack, Vec3 center, double x, double y, double z, int red, int green, int blue, int alpha) {
        x = Math.max(0.01, Math.abs(x));
        y = Math.max(0.01, Math.abs(y));
        z = Math.max(0.01, Math.abs(z));
        Vec3 nnn = center.add(-x, -y, -z);
        Vec3 nnp = center.add(-x, -y, z);
        Vec3 npn = center.add(-x, y, -z);
        Vec3 npp = center.add(-x, y, z);
        Vec3 pnn = center.add(x, -y, -z);
        Vec3 pnp = center.add(x, -y, z);
        Vec3 ppn = center.add(x, y, -z);
        Vec3 ppp = center.add(x, y, z);
        drawLine(buffer, poseStack, nnn, pnn, red, green, blue, alpha);
        drawLine(buffer, poseStack, nnp, pnp, red, green, blue, alpha);
        drawLine(buffer, poseStack, npn, ppn, red, green, blue, alpha);
        drawLine(buffer, poseStack, npp, ppp, red, green, blue, alpha);
        drawLine(buffer, poseStack, nnn, npn, red, green, blue, alpha);
        drawLine(buffer, poseStack, nnp, npp, red, green, blue, alpha);
        drawLine(buffer, poseStack, pnn, ppn, red, green, blue, alpha);
        drawLine(buffer, poseStack, pnp, ppp, red, green, blue, alpha);
        drawLine(buffer, poseStack, nnn, nnp, red, green, blue, alpha);
        drawLine(buffer, poseStack, npn, npp, red, green, blue, alpha);
        drawLine(buffer, poseStack, pnn, pnp, red, green, blue, alpha);
        drawLine(buffer, poseStack, ppn, ppp, red, green, blue, alpha);
    }

    private static void drawEllipse(MultiBufferSource buffer, PoseStack poseStack, Vec3 center, double radiusA, double radiusB, Plane plane, int red, int green, int blue, int alpha) {
        radiusA = Math.max(0.01, Math.abs(radiusA));
        radiusB = Math.max(0.01, Math.abs(radiusB));
        Vec3 previous = null;
        int segments = 64;
        for (int i = 0; i <= segments; i++) {
            double angle = i / (double) segments * Math.PI * 2.0;
            double a = Math.cos(angle) * radiusA;
            double b = Math.sin(angle) * radiusB;
            Vec3 point = switch (plane) {
                case XZ -> center.add(a, 0, b);
                case XY -> center.add(a, b, 0);
                case YZ -> center.add(0, b, a);
            };
            if (previous != null) {
                drawLine(buffer, poseStack, previous, point, red, green, blue, alpha);
            }
            previous = point;
        }
    }

    private static void drawLine(MultiBufferSource buffer, PoseStack poseStack, Vec3 start, Vec3 end, int red, int green, int blue, int alpha) {
        VertexConsumer consumer = buffer.getBuffer(OVERLAY_LINES);
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();
        Vec3 normal = end.subtract(start).normalize();
        if (normal.lengthSqr() < 1.0E-7) {
            normal = new Vec3(0, 1, 0);
        }
        consumer.addVertex(matrix, (float) start.x, (float) start.y, (float) start.z)
                .setColor(red, green, blue, alpha)
                .setNormal(pose, (float) normal.x, (float) normal.y, (float) normal.z);
        consumer.addVertex(matrix, (float) end.x, (float) end.y, (float) end.z)
                .setColor(red, green, blue, alpha)
                .setNormal(pose, (float) normal.x, (float) normal.y, (float) normal.z);
    }

    private enum Plane {
        XZ,
        XY,
        YZ
    }
}
