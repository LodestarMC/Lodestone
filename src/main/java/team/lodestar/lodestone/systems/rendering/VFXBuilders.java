package team.lodestar.lodestone.systems.rendering;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.helpers.RenderHelper;
import team.lodestar.lodestone.helpers.VecHelper;
import team.lodestar.lodestone.systems.rendering.trail.TrailPoint;
import team.lodestar.lodestone.systems.rendering.trail.TrailRenderPoint;


import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class VFXBuilders {
    public static ScreenVFXBuilder createScreen() {
        return new ScreenVFXBuilder();
    }

    public static class ScreenVFXBuilder {
        float r = 1, g = 1, b = 1, a = 1;
        int light = -1;
        float u0 = 0, v0 = 0, u1 = 1, v1 = 1;
        float x0 = 0, y0 = 0, x1 = 1, y1 = 1;
        int zLevel;

        VertexFormat format;
        Supplier<ShaderInstance> shader = GameRenderer::getPositionTexShader;
        ResourceLocation texture;
        ScreenVertexPlacementSupplier supplier;
        VertexFormat.Mode mode = VertexFormat.Mode.QUADS;
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder;

        public ScreenVFXBuilder setPosTexDefaultFormat() {
            supplier = (b, l, x, y, u, v) -> b.addVertex(l, x, y, this.zLevel).setUv(u, v);
            format = DefaultVertexFormat.POSITION_TEX;
            return this;
        }

        public ScreenVFXBuilder setPosColorDefaultFormat() {
            supplier = (b, l, x, y, u, v) -> b.addVertex(l, x, y, this.zLevel).setColor(this.r, this.g, this.b, this.a);
            format = DefaultVertexFormat.POSITION_COLOR;
            return this;
        }

        public ScreenVFXBuilder setPosTexColorDefaultFormat() {
            supplier = (b, l, x, y, u, v) -> b.addVertex(l, x, y, this.zLevel).setUv(u, v).setColor(this.r, this.g, this.b, this.a);
            format = DefaultVertexFormat.POSITION_TEX_COLOR;
            return this;
        }

        public ScreenVFXBuilder setPosColorTexLightmapDefaultFormat() {
            supplier = (b, l, x, y, u, v) -> b.addVertex(l, x, y, this.zLevel).setColor(this.r, this.g, this.b, this.a).setUv(u, v).setLight(this.light);
            format = DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP;
            return this;
        }

        public ScreenVFXBuilder setFormat(VertexFormat format) {
            this.format = format;
            return this;
        }

        public ScreenVFXBuilder setShaderTexture(ResourceLocation texture) {
            this.texture = texture;
            return this;
        }

        public ScreenVFXBuilder setShader(Supplier<ShaderInstance> shader) {
            this.shader = shader;
            return this;
        }

        public ScreenVFXBuilder setShader(ShaderInstance shader) {
            this.shader = () -> shader;
            return this;
        }

        public ScreenVFXBuilder setVertexSupplier(ScreenVertexPlacementSupplier supplier) {
            this.supplier = supplier;
            return this;
        }

        public ScreenVFXBuilder overrideBufferBuilder(BufferBuilder builder) {
            this.bufferbuilder = builder;
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

        public ScreenVFXBuilder setColorRaw(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
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

        public ScreenVFXBuilder setZLevel(int z) {
            this.zLevel = z;
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
            if (texture != null) {
                RenderSystem.setShaderTexture(0, texture);
            }
            var bufferbuilder = tesselator.begin(mode, format);
            supplier.placeVertex(bufferbuilder, last, x0, y1, u0, v1);
            supplier.placeVertex(bufferbuilder, last, x1, y1, u1, v1);
            supplier.placeVertex(bufferbuilder, last, x1, y0, u1, v0);
            supplier.placeVertex(bufferbuilder, last, x0, y0, u0, v0);
            return this;
        }

        public ScreenVFXBuilder draw(PoseStack stack) {
            if (bufferbuilder.building) {
                bufferbuilder.buildOrThrow();
            }
            begin();
            blit(stack);
            end();
            return this;
        }

        public ScreenVFXBuilder endAndProceed() {
            return end().begin();
        }

        public ScreenVFXBuilder begin() {
            bufferbuilder = tesselator.begin(VertexFormat.Mode.QUADS, format);
            return this;
        }

        public ScreenVFXBuilder end() {
            BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
            return this;
        }

        private interface ScreenVertexPlacementSupplier {
            void placeVertex(BufferBuilder bufferBuilder, Matrix4f last, float x, float y, float u, float v);
        }
    }

    public static WorldVFXBuilder createWorld() {
        return new WorldVFXBuilder();
    }

    public static class WorldVFXBuilder {
        protected float r = 1, g = 1, b = 1, a = 1;
        protected int light = RenderHelper.FULL_BRIGHT;
        protected float u0 = 0, v0 = 0, u1 = 1, v1 = 1;

        protected MultiBufferSource bufferSource = RenderHandler.DELAYED_RENDER.getTarget();
        protected RenderType renderType;
        protected VertexFormat format;
        protected WorldVertexConsumerActor supplier;
        protected VertexConsumer vertexConsumer;

        protected HashMap<Object, Consumer<WorldVFXBuilder>> modularActors = new HashMap<>();
        protected int modularActorAddIndex;
        protected int modularActorGetIndex;

        public WorldVFXBuilder replaceBufferSource(RenderHandler.LodestoneRenderLayer renderLayer) {
            return replaceBufferSource(renderLayer.getTarget());
        }

        public WorldVFXBuilder replaceBufferSource(MultiBufferSource bufferSource) {
            this.bufferSource = bufferSource;
            return this;
        }

        public static final HashMap<VertexFormatElement, WorldVertexConsumerActor> CONSUMER_INFO_MAP = new HashMap<>();

        static {
            CONSUMER_INFO_MAP.put(VertexFormatElement.POSITION, (consumer, last, builder, x, y, z, u, v) -> {
                if (last == null)
                    consumer.addVertex(x, y, z);
                else
                    consumer.addVertex(last, x, y, z);
            });
            CONSUMER_INFO_MAP.put(VertexFormatElement.COLOR, (consumer, last, builder, x, y, z, u, v) -> consumer.setColor(builder.r, builder.g, builder.b, builder.a));
            CONSUMER_INFO_MAP.put(VertexFormatElement.UV0, (consumer, last, builder, x, y, z, u, v) -> consumer.setUv(u, v));
            CONSUMER_INFO_MAP.put(VertexFormatElement.UV2, (consumer, last, builder, x, y, z, u, v) -> consumer.setLight(builder.light));
        } //TODO: add more here 11!!~!!!!!!!!!!

        public WorldVFXBuilder setRenderType(RenderType renderType) {
            return setRenderTypeRaw(renderType).setFormat(renderType.format()).setVertexConsumer(bufferSource.getBuffer(renderType));
        }

        public WorldVFXBuilder setRenderTypeRaw(RenderType renderType) {
            this.renderType = renderType;
            return this;
        }

        public WorldVFXBuilder setFormat(VertexFormat format) {
            ImmutableList<VertexFormatElement> elements = ImmutableList.copyOf(format.getElements());
            return setFormatRaw(format).setVertexSupplier((consumer, last, builder, x, y, z, u, v) -> {
                for (VertexFormatElement element : elements) {
                    CONSUMER_INFO_MAP.get(element).placeVertex(consumer, last, this, x, y, z, u, v);
                }
            });
        }

        public WorldVFXBuilder setFormatRaw(VertexFormat format) {
            this.format = format;
            return this;
        }

        public WorldVFXBuilder setVertexSupplier(WorldVertexConsumerActor supplier) {
            this.supplier = supplier;
            return this;
        }

        public WorldVFXBuilder setVertexConsumer(VertexConsumer vertexConsumer) {
            this.vertexConsumer = vertexConsumer;
            return this;
        }

        public VertexConsumer getVertexConsumer() {
            if (vertexConsumer == null) {
                setVertexConsumer(bufferSource.getBuffer(renderType));
            }
            return vertexConsumer;
        }

        public WorldVFXBuilder addModularActor(Consumer<WorldVFXBuilder> actor) {
            return addModularActor(modularActorAddIndex, actor);
        }

        public WorldVFXBuilder addModularActor(Object key, Consumer<WorldVFXBuilder> actor) {
            if (modularActors == null) {
                modularActors = new HashMap<>();
            }
            modularActors.put(key, actor);
            return this;
        }

        public Optional<HashMap<Object, Consumer<WorldVFXBuilder>>> getModularActors() {
            return Optional.ofNullable(modularActors);
        }

        public Optional<Consumer<WorldVFXBuilder>> getNextModularActor() {
            return Optional.ofNullable(modularActors).map(m -> m.get(modularActorGetIndex++));
        }

        public MultiBufferSource getBufferSource() {
            return bufferSource;
        }

        public RenderType getRenderType() {
            return renderType;
        }

        public VertexFormat getFormat() {
            return format;
        }

        public WorldVertexConsumerActor getSupplier() {
            return supplier;
        }

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

        public WorldVFXBuilder setColorRaw(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
            return this;
        }

        public WorldVFXBuilder setAlpha(float a) {
            this.a = a;
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

        public WorldVFXBuilder renderBeam(Matrix4f last, BlockPos start, BlockPos end, float width) {
            return renderBeam(last, VecHelper.getCenterOf(start), VecHelper.getCenterOf(end), width);
        }

        public WorldVFXBuilder renderBeam(@Nullable Matrix4f last, Vec3 start, Vec3 end, float width) {
            Minecraft minecraft = Minecraft.getInstance();
            Vec3 cameraPosition = minecraft.getBlockEntityRenderDispatcher().camera.getPosition();
            return renderBeam(last, start, end, width, cameraPosition);
        }

        public WorldVFXBuilder renderBeam(@Nullable Matrix4f last, Vec3 start, Vec3 end, float width, Consumer<WorldVFXBuilder> consumer) {
            Minecraft minecraft = Minecraft.getInstance();
            Vec3 cameraPosition = minecraft.getBlockEntityRenderDispatcher().camera.getPosition();
            return renderBeam(last, start, end, width, cameraPosition, consumer);
        }

        public WorldVFXBuilder renderBeam(@Nullable Matrix4f last, Vec3 start, Vec3 end, float width, Vec3 cameraPosition) {
            return renderBeam(last, start, end, width, cameraPosition, builder -> {
            });
        }

        public WorldVFXBuilder renderBeam(@Nullable Matrix4f last, Vec3 start, Vec3 end, float width, Vec3 cameraPosition, Consumer<WorldVFXBuilder> consumer) {
            Vec3 delta = end.subtract(start);
            Vec3 normal = start.subtract(cameraPosition).cross(delta).normalize().multiply(width / 2f, width / 2f, width / 2f);

            Vec3[] positions = new Vec3[]{start.subtract(normal), start.add(normal), end.add(normal), end.subtract(normal)};

            supplier.placeVertex(getVertexConsumer(), last, this, (float) positions[0].x, (float) positions[0].y, (float) positions[0].z, u0, v1);
            supplier.placeVertex(getVertexConsumer(), last, this, (float) positions[1].x, (float) positions[1].y, (float) positions[1].z, u1, v1);
            consumer.accept(this);
            supplier.placeVertex(getVertexConsumer(), last, this, (float) positions[2].x, (float) positions[2].y, (float) positions[2].z, u1, v0);
            supplier.placeVertex(getVertexConsumer(), last, this, (float) positions[3].x, (float) positions[3].y, (float) positions[3].z, u0, v0);

            return this;
        }

        public WorldVFXBuilder renderTrail(PoseStack stack, List<TrailPoint> trailSegments, float width) {
            return renderTrail(stack, trailSegments, f -> width, f -> {
            });
        }

        public WorldVFXBuilder renderTrail(PoseStack stack, List<TrailPoint> trailSegments, Function<Float, Float> widthFunc) {
            return renderTrail(stack, trailSegments, widthFunc, f -> {
            });
        }

        public WorldVFXBuilder renderTrail(PoseStack stack, List<TrailPoint> trailSegments, Function<Float, Float> widthFunc, Consumer<Float> vfxOperator) {
            return renderTrail(stack.last().pose(), trailSegments, widthFunc, vfxOperator);
        }

        public WorldVFXBuilder renderTrail(Matrix4f pose, List<TrailPoint> trailSegments, Function<Float, Float> widthFunc, Consumer<Float> vfxOperator) {
            if (trailSegments.size() < 2) {
                return this;
            }
            List<Vector4f> positions = trailSegments.stream().map(TrailPoint::getMatrixPosition).peek(p -> p.mul(pose)).toList();
            int count = trailSegments.size() - 1;
            float increment = 1.0F / count;
            TrailRenderPoint[] points = new TrailRenderPoint[trailSegments.size()];
            for (int i = 1; i < count; i++) {
                float width = widthFunc.apply(increment * i);
                Vector4f previous = positions.get(i - 1);
                Vector4f current = positions.get(i);
                Vector4f next = positions.get(i + 1);
                points[i] = new TrailRenderPoint(current, RenderHelper.perpendicularTrailPoints(previous, next, width));
            }
            points[0] = new TrailRenderPoint(positions.get(0),
                    RenderHelper.perpendicularTrailPoints(positions.get(0), positions.get(1), widthFunc.apply(0f)));
            points[count] = new TrailRenderPoint(positions.get(count),
                    RenderHelper.perpendicularTrailPoints(positions.get(count-1), positions.get(count), widthFunc.apply(1f)));
            return renderPoints(points, u0, v0, u1, v1, vfxOperator);
        }

        public WorldVFXBuilder renderPoints(TrailRenderPoint[] points, float u0, float v0, float u1, float v1, Consumer<Float> vfxOperator) {
            int count = points.length - 1;
            float increment = 1.0F / count;
            vfxOperator.accept(0f);
            points[0].renderStart(getVertexConsumer(), this, u0, v0, u1, Mth.lerp(increment, v0, v1));
            for (int i = 1; i < count; i++) {
                float current = Mth.lerp(i * increment, v0, v1);
                vfxOperator.accept(current);
                points[i].renderMid(getVertexConsumer(), this, u0, current, u1, current);
            }
            vfxOperator.accept(1f);
            points[count].renderEnd(getVertexConsumer(), this, u0, Mth.lerp((count) * increment, v0, v1), u1, v1);
            return this;
        }

        public WorldVFXBuilder renderQuad(PoseStack stack, float size) {
            return renderQuad(stack, size, size);
        }

        public WorldVFXBuilder renderQuad(PoseStack stack, float width, float height) {
            Vector3f[] positions = new Vector3f[]{new Vector3f(-1, -1, 0), new Vector3f(1, -1, 0), new Vector3f(1, 1, 0), new Vector3f(-1, 1, 0)};
            return renderQuad(stack, positions, width, height);
        }

        public WorldVFXBuilder renderQuad(PoseStack stack, Vector3f[] positions, float size) {
            return renderQuad(stack, positions, size, size);
        }

        public WorldVFXBuilder renderQuad(PoseStack stack, Vector3f[] positions, float width, float height) {
            for (Vector3f position : positions) {
                position.mul(width, height, width);
            }
            return renderQuad(stack.last().pose(), positions);
        }

        public WorldVFXBuilder renderQuad(Matrix4f last, Vector3f[] positions) {
            supplier.placeVertex(getVertexConsumer(), last, this, positions[0].x(), positions[0].y(), positions[0].z(), u0, v1);
            supplier.placeVertex(getVertexConsumer(), last, this, positions[1].x(), positions[1].y(), positions[1].z(), u1, v1);
            supplier.placeVertex(getVertexConsumer(), last, this, positions[2].x(), positions[2].y(), positions[2].z(), u1, v0);
            supplier.placeVertex(getVertexConsumer(), last, this, positions[3].x(), positions[3].y(), positions[3].z(), u0, v0);
            return this;
        }

        /**
         * RenderSphere requires a triangle-based RenderType.
         */
        public WorldVFXBuilder renderSphere(PoseStack stack, float radius, int longs, int lats) {
            Matrix4f last = stack.last().pose();
            float startU = u0;
            float startV = v0;
            float endU = Mth.PI * 2 * u1;
            float endV = Mth.PI * v1;
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
                    supplier.placeVertex(getVertexConsumer(), last, this, p0.x(), p0.y(), p0.z(), textureU, textureV);
                    supplier.placeVertex(getVertexConsumer(), last, this, p2.x(), p2.y(), p2.z(), textureUN, textureV);
                    supplier.placeVertex(getVertexConsumer(), last, this, p1.x(), p1.y(), p1.z(), textureU, textureVN);

                    supplier.placeVertex(getVertexConsumer(), last, this, p3.x(), p3.y(), p3.z(), textureUN, textureVN);
                    supplier.placeVertex(getVertexConsumer(), last, this, p1.x(), p1.y(), p1.z(), textureU, textureVN);
                    supplier.placeVertex(getVertexConsumer(), last, this, p2.x(), p2.y(), p2.z(), textureUN, textureV);
                }
            }
            return this;
        }


        public interface WorldVertexConsumerActor {
            void placeVertex(VertexConsumer consumer, Matrix4f last, WorldVFXBuilder builder, float x, float y, float z, float u, float v);
        }
    }
}