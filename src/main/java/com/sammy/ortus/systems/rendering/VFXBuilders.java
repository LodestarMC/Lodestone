package com.sammy.ortus.systems.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import com.sammy.ortus.helpers.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class VFXBuilders {
    public static ScreenVFXBuilder createScreen() {
        return new ScreenVFXBuilder().setPosTexDefaultFormat();
    }

    public static class ScreenVFXBuilder {
        float r = 1, g = 1, b = 1, a = 1;
        int light = -1;
        float u0 = 0, v0 = 0, u1 = 1, v1 = 1;
        float x0 = 0, y0 = 0, x1 = 1, y1 = 1;

        VertexFormat format;
        Supplier<ShaderInstance> shader = GameRenderer::getPositionTexShader;
        VertexPlacementSupplier supplier; //TODO: this is actually a pretty cool way of allowing X vertex format on a per-builder basis. Would be nice to port it to the WorldVFXBuilder too
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();

        public ScreenVFXBuilder setPosTexDefaultFormat() {
            supplier = (b, l, x, y, u, v) -> b.vertex(l, x, y, 0).uv(u, v).endVertex();
            format = DefaultVertexFormat.POSITION_TEX;
            return this;
        }

        public ScreenVFXBuilder setPosColorTexDefaultFormat() {
            supplier = (b, l, x, y, u, v) -> b.vertex(l, x, y, 0).color(this.r, this.g, this.b, this.a).uv(u, v).endVertex();
            format = DefaultVertexFormat.POSITION_COLOR_TEX;
            return this;
        }

        public ScreenVFXBuilder setPosColorTexLightmapDefaultFormat() {
            supplier = (b, l, x, y, u, v) -> b.vertex(l, x, y, 0).color(this.r, this.g, this.b, this.a).uv(u, v).uv2(this.light).endVertex();
            format = DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP;
            return this;
        }

        public ScreenVFXBuilder setFormat(VertexFormat format) {
            this.format = format;
            return this;
        }

        public ScreenVFXBuilder setShader(Supplier<ShaderInstance> shader) {
            this.shader = shader;
            return this;
        }

        public ScreenVFXBuilder setVertexSupplier(VertexPlacementSupplier supplier) {
            this.supplier = supplier;
            return this;
        }

        public ScreenVFXBuilder setLight(int light) {
            this.light = light;
            return this;
        }

        public ScreenVFXBuilder setColor(Color color) {
            return setColor(color.getRed(), color.getGreen(), color.getBlue());
        }

        public ScreenVFXBuilder setColor(Color color, float a) {
            return setColor(color).setAlpha(a);
        }

        public ScreenVFXBuilder setColor(float r, float g, float b, float a) {
            return setColor(r, g, b).setAlpha(a);
        }

        public ScreenVFXBuilder setColor(float r, float g, float b) {
            this.r = r / 255f;
            this.g = g / 255f;
            this.b = b / 255f;
            return this;
        }

        public ScreenVFXBuilder setAlpha(float a) {
            this.a = a;
            return this;
        }

        public ScreenVFXBuilder setPositionWithWidth(float x, float y, float width, float height) {
            return setPosition(x, y, x + width, y + height);
        }

        public ScreenVFXBuilder setPosition(float x0, float y0, float x1, float y1) {
            this.x0 = x0;
            this.y0 = y0;
            this.x1 = x1;
            this.y1 = y1;
            return this;
        }

        public ScreenVFXBuilder setUVWithWidth(float u, float v, float width, float height, float canvasSize) {
            return setUVWithWidth(u, v, width, height, canvasSize, canvasSize);
        }

        public ScreenVFXBuilder setUVWithWidth(float u, float v, float width, float height, float canvasSizeX, float canvasSizeY) {
            return setUVWithWidth(u / canvasSizeX, v / canvasSizeY, width / canvasSizeX, height / canvasSizeY);
        }

        public ScreenVFXBuilder setUVWithWidth(float u, float v, float width, float height) {
            this.u0 = u;
            this.v0 = v;
            this.u1 = (u + width);
            this.v1 = (v + height);
            return this;
        }

        public ScreenVFXBuilder setUV(float u0, float v0, float u1, float v1, float canvasSize) {
            return setUV(u0, v0, u1, v1, canvasSize, canvasSize);
        }

        public ScreenVFXBuilder setUV(float u0, float v0, float u1, float v1, float canvasSizeX, float canvasSizeY) {
            return setUV(u0 / canvasSizeX, v0 / canvasSizeY, u1 / canvasSizeX, v1 / canvasSizeY);
        }

        public ScreenVFXBuilder setUV(float u0, float v0, float u1, float v1) {
            this.u0 = u0;
            this.v0 = v0;
            this.u1 = u1;
            this.v1 = v1;
            return this;
        }

        public ScreenVFXBuilder blit(PoseStack stack) {
            Matrix4f last = stack.last().pose();
            RenderSystem.setShader(shader);
            supplier.placeVertex(bufferbuilder, last, x0, y1, u0, v1);
            supplier.placeVertex(bufferbuilder, last, x1, y1, u1, v1);
            supplier.placeVertex(bufferbuilder, last, x1, v0, u1, v0);
            supplier.placeVertex(bufferbuilder, last, x0, v0, u0, v0);
            return this;
        }

        public ScreenVFXBuilder begin() {
            bufferbuilder.begin(VertexFormat.Mode.QUADS, format);
            return this;
        }

        public ScreenVFXBuilder end() {
            bufferbuilder.end();
            BufferUploader.end(bufferbuilder);
            return this;
        }

        private interface VertexPlacementSupplier {
            void placeVertex(BufferBuilder bufferBuilder, Matrix4f last, float x, float y, float u, float v);
        }
    }

    public static WorldVFXBuilder create() {
        return new WorldVFXBuilder();
    }

    public static class WorldVFXBuilder {
        float r = 1, g = 1, b = 1, a = 1;
        float xOffset = 0, yOffset = 0, zOffset = 0;
        int light = RenderHelper.FULL_BRIGHT;
        float u0 = 0, v0 = 0, u1 = 1, v1 = 1;


        public WorldVFXBuilder setColor(Color color) {
            return setColor(color.getRed(), color.getGreen(), color.getBlue());
        }

        public WorldVFXBuilder setColor(Color color, float a) {
            return setColor(color).setAlpha(a);
        }

        public WorldVFXBuilder setColor(float r, float g, float b, float a) {
            return setColor(r, g, b).setAlpha(a);
        }

        public WorldVFXBuilder setColor(float r, float g, float b) {
            this.r = r / 255f;
            this.g = g / 255f;
            this.b = b / 255f;
            return this;
        }

        public WorldVFXBuilder setAlpha(float a) {
            this.a = a;
            return this;
        }

        public WorldVFXBuilder setOffset(float xOffset, float yOffset, float zOffset) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.zOffset = zOffset;
            return this;
        }

        public WorldVFXBuilder setLight(int light) {
            this.light = light;
            return this;
        }

        public WorldVFXBuilder setUV(float u0, float v0, float u1, float v1) {
            this.u0 = u0;
            this.v0 = v0;
            this.u1 = u1;
            this.v1 = v1;
            return this;
        }

        public WorldVFXBuilder renderTriangle(VertexConsumer vertexConsumer, PoseStack stack, float size) {
            return renderTriangle(vertexConsumer, stack, size, size);
        }

        public WorldVFXBuilder renderTriangle(VertexConsumer vertexConsumer, PoseStack stack, float width, float height) {
            Matrix4f last = stack.last().pose();
            RenderHelper.vertexPosColorUVLight(vertexConsumer, last, -width, -height, 0, r, g, b, a, 0, 1, light);
            RenderHelper.vertexPosColorUVLight(vertexConsumer, last, width, -height, 0, r, g, b, a, 1, 1, light);
            RenderHelper.vertexPosColorUVLight(vertexConsumer, last, 0, height, 0, r, g, b, a, 0.5f, 0, light);
            return this;
        }


        public WorldVFXBuilder renderTrail(VertexConsumer vertexConsumer, PoseStack stack, java.util.List<Vector4f> trailSegments, Function<Float, Float> widthFunc) {
            return renderTrail(vertexConsumer, stack.last().pose(), trailSegments, widthFunc);
        }

        public WorldVFXBuilder renderTrail(VertexConsumer vertexConsumer, Matrix4f pose, java.util.List<Vector4f> trailSegments, Function<Float, Float> widthFunc) {
            if (trailSegments.size() < 3) {
                return this;
            }
            for (Vector4f pos : trailSegments) {
                pos.add(xOffset, yOffset, zOffset, 0);
                pos.transform(pose);
            }

            int count = trailSegments.size() - 1;
            float increment = 1.0F / (count - 1);
            ArrayList<TrailPoint> points = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                float width = widthFunc.apply(increment * i);
                Vector4f start = trailSegments.get(i);
                Vector4f end = trailSegments.get(i + 1);
                Vector4f mid = RenderHelper.midpoint(start, end);
                Vec2 offset = RenderHelper.corners(start, end, width);
                Vector4f positions = new Vector4f(mid.x() + offset.x, mid.x() - offset.x, mid.y() + offset.y, mid.y() - offset.y);
                points.add(new TrailPoint(positions.x(), positions.y(), positions.z(), positions.w(), mid.z()));
            }
            return renderPoints(vertexConsumer, points, u0, v0, u1, v1);
        }

        public WorldVFXBuilder renderPoints(VertexConsumer vertexConsumer, List<TrailPoint> trailPoints, float u0, float v0, float u1, float v1) {
            int count = trailPoints.size() - 1;
            float increment = 1.0F / count;
            trailPoints.get(0).renderStart(vertexConsumer, light, r, g, b, a, u0, v0, u1, Mth.lerp(increment, v0, v1));
            for (int i = 1; i < count; i++) {
                float current = Mth.lerp(i * increment, v0, v1);
                trailPoints.get(i).renderMid(vertexConsumer, light, r, g, b, a, u0, current, u1, current);
            }
            trailPoints.get(count).renderEnd(vertexConsumer, light, r, g, b, a, u0, Mth.lerp((count) * increment, v0, v1), u1, v1);
            return this;
        }

        public WorldVFXBuilder renderBeam(VertexConsumer vertexConsumer, PoseStack stack, Vec3 start, Vec3 end, float width) {
            Minecraft minecraft = Minecraft.getInstance();
            start.add(xOffset, yOffset, zOffset);
            end.add(xOffset, yOffset, zOffset);
            stack.translate(-start.x, -start.y, -start.z);
            Vec3 cameraPosition = minecraft.getBlockEntityRenderDispatcher().camera.getPosition();
            Vec3 delta = end.subtract(start);
            Vec3 normal = start.subtract(cameraPosition).cross(delta).normalize().multiply(width / 2f, width / 2f, width / 2f);
            Matrix4f last = stack.last().pose();
            Vec3[] positions = new Vec3[]{start.subtract(normal), start.add(normal), end.add(normal), end.subtract(normal)};
            RenderHelper.vertexPosColorUVLight(vertexConsumer, last, (float) positions[0].x, (float) positions[0].y, (float) positions[0].z, r, g, b, a, u0, v1, light);
            RenderHelper.vertexPosColorUVLight(vertexConsumer, last, (float) positions[1].x, (float) positions[1].y, (float) positions[1].z, r, g, b, a, u1, v1, light);
            RenderHelper.vertexPosColorUVLight(vertexConsumer, last, (float) positions[2].x, (float) positions[2].y, (float) positions[2].z, r, g, b, a, u1, v0, light);
            RenderHelper.vertexPosColorUVLight(vertexConsumer, last, (float) positions[3].x, (float) positions[3].y, (float) positions[3].z, r, g, b, a, u0, v0, light);
            stack.translate(start.x, start.y, start.z);
            return this;
        }

        public WorldVFXBuilder renderQuad(VertexConsumer vertexConsumer, PoseStack stack, float size) {
            return renderQuad(vertexConsumer, stack, size, size);
        }

        public WorldVFXBuilder renderQuad(VertexConsumer vertexConsumer, PoseStack stack, float width, float height) {
            Vector3f[] positions = new Vector3f[]{new Vector3f(-width, -height, 0), new Vector3f(width, -height, 0), new Vector3f(width, height, 0), new Vector3f(-width, height, 0)};
            return renderQuad(vertexConsumer, stack, positions, width, height);
        }

        public WorldVFXBuilder renderQuad(VertexConsumer vertexConsumer, PoseStack stack, Vector3f[] positions, float size) {
            return renderQuad(vertexConsumer, stack, positions, size, size);
        }

        public WorldVFXBuilder renderQuad(VertexConsumer vertexConsumer, PoseStack stack, Vector3f[] positions, float width, float height) {
            Matrix4f last = stack.last().pose();
            stack.translate(xOffset, yOffset, zOffset);
            for (Vector3f position : positions) {
                position.mul(width, height, width);
            }
            RenderHelper.vertexPosColorUVLight(vertexConsumer, last, positions[0].x(), positions[0].y(), positions[0].z(), r, g, b, a, u0, v1, light);
            RenderHelper.vertexPosColorUVLight(vertexConsumer, last, positions[1].x(), positions[1].y(), positions[1].z(), r, g, b, a, u1, v1, light);
            RenderHelper.vertexPosColorUVLight(vertexConsumer, last, positions[2].x(), positions[2].y(), positions[2].z(), r, g, b, a, u1, v0, light);
            RenderHelper.vertexPosColorUVLight(vertexConsumer, last, positions[3].x(), positions[3].y(), positions[3].z(), r, g, b, a, u0, v0, light);
            stack.translate(-xOffset, -yOffset, -zOffset);
            return this;
        }

        public WorldVFXBuilder renderSphere(VertexConsumer vertexConsumer, PoseStack stack, float radius, int longs, int lats) {
            Matrix4f last = stack.last().pose();
            float startU = 0;
            float startV = 0;
            float endU = Mth.PI * 2;
            float endV = Mth.PI;
            float stepU = (endU - startU) / longs;
            float stepV = (endV - startV) / lats;
            for (int i = 0; i < longs; ++i) {
                // U-points
                for (int j = 0; j < lats; ++j) {
                    // V-points
                    float u = i * stepU + startU;
                    float v = j * stepV + startV;
                    float un = (i + 1 == longs) ? endU : (i + 1) * stepU + startU;
                    float vn = (j + 1 == lats) ? endV : (j + 1) * stepV + startV;
                    Vector3f p0 = RenderHelper.parametricSphere(u, v, radius);
                    Vector3f p1 = RenderHelper.parametricSphere(u, vn, radius);
                    Vector3f p2 = RenderHelper.parametricSphere(un, v, radius);
                    Vector3f p3 = RenderHelper.parametricSphere(un, vn, radius);

                    float textureU = u / endU * radius;
                    float textureV = v / endV * radius;
                    float textureUN = un / endU * radius;
                    float textureVN = vn / endV * radius;
                    RenderHelper.vertexPosColorUVLight(vertexConsumer, last, p0.x(), p0.y(), p0.z(), r, g, b, a, textureU, textureV, light);
                    RenderHelper.vertexPosColorUVLight(vertexConsumer, last, p2.x(), p2.y(), p2.z(), r, g, b, a, textureUN, textureV, light);
                    RenderHelper.vertexPosColorUVLight(vertexConsumer, last, p1.x(), p1.y(), p1.z(), r, g, b, a, textureU, textureVN, light);

                    RenderHelper.vertexPosColorUVLight(vertexConsumer, last, p3.x(), p3.y(), p3.z(), r, g, b, a, textureUN, textureVN, light);
                    RenderHelper.vertexPosColorUVLight(vertexConsumer, last, p1.x(), p1.y(), p1.z(), r, g, b, a, textureU, textureVN, light);
                    RenderHelper.vertexPosColorUVLight(vertexConsumer, last, p2.x(), p2.y(), p2.z(), r, g, b, a, textureUN, textureV, light);
                }
            }
            return this;
        }
    }
}