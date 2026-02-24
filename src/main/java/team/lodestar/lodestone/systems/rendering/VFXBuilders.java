package team.lodestar.lodestone.systems.rendering;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.*;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.phys.*;
import org.joml.*;
import team.lodestar.lodestone.systems.rendering.cube.*;
import team.lodestar.lodestone.systems.rendering.rendeertype.*;
import team.lodestar.lodestone.systems.rendering.trail.*;
import team.lodestar.lodestone.handlers.rendering.*;
import team.lodestar.lodestone.helpers.*;
import team.lodestar.lodestone.systems.rendering.buffer.LodestoneRenderLayer;

import javax.annotation.*;
import java.awt.*;
import java.lang.Math;
import java.util.*;
import java.util.function.*;

public class VFXBuilders {

    public static final HashMap<VertexFormatElement, VertexConsumerActor> CONSUMER_INFO_MAP = new HashMap<>();

    static {
        CONSUMER_INFO_MAP.put(VertexFormatElement.POSITION, (consumer, pose, builder, normal, x, y, z, u, v) -> {
            if (pose == null) {
                consumer.addVertex(x, y, z);
                return;
            }
            consumer.addVertex(pose, x, y, z);
        });
        CONSUMER_INFO_MAP.put(VertexFormatElement.NORMAL, (consumer, pose, builder, normal, x, y, z, u, v) -> {
            if (normal == null) {
                return;
            }
            if (pose == null) {
                consumer.setNormal(normal.x, normal.y, normal.z);
                return;
            }
            consumer.setNormal(pose, normal.x, normal.y, normal.z);
        });
        CONSUMER_INFO_MAP.put(VertexFormatElement.COLOR, (consumer, pose, builder, normal, x, y, z, u, v) -> consumer.setColor(builder.r, builder.g, builder.b, builder.a));
        CONSUMER_INFO_MAP.put(VertexFormatElement.UV0, (consumer, pose, builder, normal, x, y, z, u, v) -> consumer.setUv(u, v));
        CONSUMER_INFO_MAP.put(VertexFormatElement.UV2, (consumer, pose, builder, normal, x, y, z, u, v) -> consumer.setLight(builder.light));
    } //TODO: add more here


    public interface VertexConsumerActor {
        void placeVertex(VertexConsumer consumer, PoseStack.Pose pose, AbstractVFXBuilder builder, Vector3f normal, float x, float y, float z, float u, float v);

        default void placeVertex(VertexConsumer consumer, PoseStack stack, AbstractVFXBuilder builder, float x, float y, float z, float u, float v) {
            Vector3f normal = normal(stack);
            placeVertex(consumer, stack.last(), builder, normal, x, y, z, u, v);
        }

        default void placeVertex(VertexConsumer consumer, PoseStack.Pose pose, AbstractVFXBuilder builder, float x, float y, float z, float u, float v) {
            placeVertex(consumer, pose, builder, null, x, y, z, u, v);
        }

        default void placeVertex(VertexConsumer consumer, AbstractVFXBuilder builder, Vector3f normal, float x, float y, float z, float u, float v) {
            placeVertex(consumer, null, builder, normal, x, y, z, u, v);
        }

        default void placeVertex(VertexConsumer consumer, AbstractVFXBuilder builder, float x, float y, float z, float u, float v) {
            placeVertex(consumer, builder, null, x, y, z, u, v);
        }
    }


    public static Vector3f normal(PoseStack stack) {
        return normal(stack.last().normal());
    }

    public static Vector3f normal(Matrix3f transform) {
        return new Vector3f(0, 1, 0).mul(transform);
    }

    public static abstract class AbstractVFXBuilder {
        float r = 1, g = 1, b = 1, a = 1;
        int light = RenderHelper.FULL_BRIGHT;
        float u0 = 0, v0 = 0, u1 = 1, v1 = 1;

        VertexFormat format;
        VertexConsumerActor supplier;
        VertexFormat.Mode mode = VertexFormat.Mode.QUADS;

        public AbstractVFXBuilder setFormat(VertexFormat format) {
            ImmutableList<VertexFormatElement> elements = ImmutableList.copyOf(format.getElements());
            return setFormatRaw(format).setVertexSupplier((consumer, last, builder, normal, x, y, z, u, v) -> {
                for (VertexFormatElement element : elements) {
                    CONSUMER_INFO_MAP.get(element).placeVertex(consumer, last, this, normal, x, y, z, u, v);
                }
            });
        }

        public AbstractVFXBuilder setVertexSupplier(VertexConsumerActor supplier) {
            this.supplier = supplier;
            return this;
        }

        public AbstractVFXBuilder setFormatRaw(VertexFormat format) {
            this.format = format;
            return this;
        }

        public AbstractVFXBuilder setColor(int rgba) {
            return setColor((rgba >> 16) & 0xFF, (rgba >> 8) & 0xFF, rgba & 0xFF, (rgba >> 24) & 0xFF);
        }
        public AbstractVFXBuilder setColor(Color color) {
            return setColor(color.getRed(), color.getGreen(), color.getBlue());
        }

        public AbstractVFXBuilder setColor(Color color, int a) {
            return setColor(color).setAlpha(a);
        }

        public AbstractVFXBuilder setColor(Color color, float a) {
            return setColor(color).setAlpha(a);
        }

        public AbstractVFXBuilder setColor(int r, int g, int b, int a) {
            return setColor(r, g, b).setAlpha(a);
        }

        public AbstractVFXBuilder setColor(float r, float g, float b, float a) {
            return setColor(r, g, b).setAlpha(a);
        }

        public AbstractVFXBuilder setColor(int r, int g, int b) {
            return setColor(r / 255f, g / 255f, b / 255f);
        }

        public AbstractVFXBuilder setColor(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
            return this;
        }

        public AbstractVFXBuilder multiplyColor(float scalar) {
            return multiplyColor(scalar, scalar, scalar);
        }
        public AbstractVFXBuilder multiplyColor(float r, float g, float b) {
            return setColor(this.r * r, this.g * g, this.b * b);
        }

        public AbstractVFXBuilder setAlpha(int a) {
            return setAlpha(a / 255f);
        }

        public AbstractVFXBuilder setAlpha(float a) {
            this.a = a;
            return this;
        }

        public AbstractVFXBuilder setLight(int light) {
            this.light = light;
            return this;
        }

        public AbstractVFXBuilder setUVWithWidth(float u, float v, float width, float height, float canvasSize) {
            return setUVWithWidth(u, v, width, height, canvasSize, canvasSize);
        }

        public AbstractVFXBuilder setUVWithWidth(float u, float v, float width, float height, float canvasSizeX, float canvasSizeY) {
            return setUVWithWidth(u / canvasSizeX, v / canvasSizeY, width / canvasSizeX, height / canvasSizeY);
        }

        public AbstractVFXBuilder setUVWithWidth(float u, float v, float width, float height) {
            this.u0 = u;
            this.v0 = v;
            this.u1 = (u + width);
            this.v1 = (v + height);
            return this;
        }

        public AbstractVFXBuilder setUV(float u0, float v0, float u1, float v1, float canvasSize) {
            return setUV(u0, v0, u1, v1, canvasSize, canvasSize);
        }

        public AbstractVFXBuilder setUV(float u0, float v0, float u1, float v1, float canvasSizeX, float canvasSizeY) {
            return setUV(u0 / canvasSizeX, v0 / canvasSizeY, u1 / canvasSizeX, v1 / canvasSizeY);
        }

        public AbstractVFXBuilder setUV(float u0, float v0, float u1, float v1) {
            this.u0 = u0;
            this.v0 = v0;
            this.u1 = u1;
            this.v1 = v1;
            return this;
        }
    }

    public static ScreenVFXBuilder createScreen() {
        return new ScreenVFXBuilder();
    }

    public static WorldVFXBuilder createWorld() {
        return new WorldVFXBuilder();
    }

    public static class ScreenVFXBuilder extends AbstractVFXBuilder {
        float x0 = 0, y0 = 0, x1 = 1, y1 = 1;
        int zLevel;

        Supplier<ShaderInstance> shader;
        ResourceLocation texture;
        Tesselator tesselator = Tesselator.getInstance();

        @Override
        public ScreenVFXBuilder setColor(int rgba) {
            return (ScreenVFXBuilder) super.setColor(rgba);
        }

        @Override
        public ScreenVFXBuilder setColor(Color color) {
            return (ScreenVFXBuilder) super.setColor(color);
        }

        @Override
        public ScreenVFXBuilder setColor(Color color, int a) {
            return (ScreenVFXBuilder) super.setColor(color, a);
        }

        @Override
        public ScreenVFXBuilder setColor(Color color, float a) {
            return (ScreenVFXBuilder) super.setColor(color, a);
        }

        @Override
        public ScreenVFXBuilder setColor(int r, int g, int b, int a) {
            return (ScreenVFXBuilder) super.setColor(r, g, b, a);
        }

        @Override
        public ScreenVFXBuilder setColor(float r, float g, float b, float a) {
            return (ScreenVFXBuilder) super.setColor(r, g, b, a);
        }

        @Override
        public ScreenVFXBuilder setColor(int r, int g, int b) {
            return (ScreenVFXBuilder) super.setColor(r, g, b);
        }

        @Override
        public ScreenVFXBuilder setColor(float r, float g, float b) {
            return (ScreenVFXBuilder) super.setColor(r, g, b);
        }

        @Override
        public ScreenVFXBuilder multiplyColor(float scalar) {
            return (ScreenVFXBuilder) super.multiplyColor(scalar);
        }

        @Override
        public ScreenVFXBuilder multiplyColor(float r, float g, float b) {
            return (ScreenVFXBuilder) super.multiplyColor(r, g, b);
        }

        @Override
        public ScreenVFXBuilder setAlpha(int a) {
            return (ScreenVFXBuilder) super.setAlpha(a);
        }

        @Override
        public ScreenVFXBuilder setAlpha(float a) {
            return (ScreenVFXBuilder) super.setAlpha(a);
        }


        @Override
        public ScreenVFXBuilder setUVWithWidth(float u, float v, float width, float height, float canvasSize) {
            return (ScreenVFXBuilder) super.setUVWithWidth(u, v, width, height, canvasSize);
        }

        @Override
        public ScreenVFXBuilder setUVWithWidth(float u, float v, float width, float height, float canvasSizeX, float canvasSizeY) {
            return (ScreenVFXBuilder) super.setUVWithWidth(u, v, width, height, canvasSizeX, canvasSizeY);
        }

        @Override
        public ScreenVFXBuilder setUVWithWidth(float u, float v, float width, float height) {
            return (ScreenVFXBuilder) super.setUVWithWidth(u, v, width, height);
        }

        @Override
        public ScreenVFXBuilder setUV(float u0, float v0, float u1, float v1, float canvasSize) {
            return (ScreenVFXBuilder) super.setUV(u0, v0, u1, v1, canvasSize);
        }

        @Override
        public ScreenVFXBuilder setUV(float u0, float v0, float u1, float v1, float canvasSizeX, float canvasSizeY) {
            return (ScreenVFXBuilder) super.setUV(u0, v0, u1, v1, canvasSizeX, canvasSizeY);
        }

        @Override
        public ScreenVFXBuilder setUV(float u0, float v0, float u1, float v1) {
            return (ScreenVFXBuilder) super.setUV(u0, v0, u1, v1);
        }

        @Override
        public ScreenVFXBuilder setFormat(VertexFormat format) {
            return (ScreenVFXBuilder) super.setFormat(format);
        }

        @Override
        public ScreenVFXBuilder setVertexSupplier(VertexConsumerActor supplier) {
            return (ScreenVFXBuilder) super.setVertexSupplier(supplier);
        }

        @Override
        public ScreenVFXBuilder setFormatRaw(VertexFormat format) {
            return (ScreenVFXBuilder) super.setFormatRaw(format);
        }

        public ScreenVFXBuilder setShader(Supplier<ShaderInstance> shader) {
            this.shader = shader;
            return updateVertexFormat();
        }

        public ScreenVFXBuilder setShader(ShaderInstance shader) {
            this.shader = () -> shader;
            return updateVertexFormat();
        }

        public Supplier<ShaderInstance> getShader() {
            if (shader == null) {
                setShader(GameRenderer::getPositionTexShader);
            }
            return shader;
        }

        public ScreenVFXBuilder setTexture(ResourceLocation texture) {
            this.texture = texture;
            return this;
        }

        public ScreenVFXBuilder setSprite(ResourceLocation location) {
            var sprite = Minecraft.getInstance().getGuiSprites().getSprite(location);
            this.u0 = sprite.getU0();
            this.v0 = sprite.getV0();
            this.u1 = sprite.getU1();
            this.v1 = sprite.getV1();
            return setTexture(sprite.atlasLocation());
        }

        public final ScreenVFXBuilder updateVertexFormat() {
            return setFormat(getShader().get().getVertexFormat());
        }

        public ScreenVFXBuilder setLight(int light) {
            this.light = light;
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

        public ScreenVFXBuilder blit(GuiGraphics graphics) {
            return blit(graphics.pose());
        }

        public ScreenVFXBuilder blit(PoseStack stack) {
            RenderSystem.setShader(getShader());
            if (texture != null) {
                RenderSystem.setShaderTexture(0, texture);
            }
            var bufferBuilder = tesselator.begin(mode, format);
            supplier.placeVertex(bufferBuilder, stack, this, x0, y1, zLevel, u0, v1);
            supplier.placeVertex(bufferBuilder, stack, this, x1, y1, zLevel, u1, v1);
            supplier.placeVertex(bufferBuilder, stack, this, x1, y0, zLevel, u1, v0);
            supplier.placeVertex(bufferBuilder, stack, this, x0, y0, zLevel, u0, v0);
            BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
            return this;
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public static class WorldVFXBuilder extends AbstractVFXBuilder {

        private final Minecraft minecraft = Minecraft.getInstance();

        @Nonnull
        protected LodestoneRenderLayer renderLayer = LodestoneRenderHandler.DEFERRED_RENDER;

        protected MultiBufferSource bufferSource;
        protected RenderType renderType;
        protected VertexConsumer vertexConsumer;
        protected boolean usePartialTicks;
        protected float partialTicks;


        @Override
        public WorldVFXBuilder setColor(int rgba) {
            return (WorldVFXBuilder) super.setColor(rgba);
        }

        @Override
        public WorldVFXBuilder setColor(Color color) {
            return (WorldVFXBuilder) super.setColor(color);
        }

        @Override
        public WorldVFXBuilder setColor(Color color, int a) {
            return (WorldVFXBuilder) super.setColor(color, a);
        }

        @Override
        public WorldVFXBuilder setColor(Color color, float a) {
            return (WorldVFXBuilder) super.setColor(color, a);
        }

        @Override
        public WorldVFXBuilder setColor(int r, int g, int b, int a) {
            return (WorldVFXBuilder) super.setColor(r, g, b, a);
        }

        @Override
        public WorldVFXBuilder setColor(float r, float g, float b, float a) {
            return (WorldVFXBuilder) super.setColor(r, g, b, a);
        }

        @Override
        public WorldVFXBuilder setColor(int r, int g, int b) {
            return (WorldVFXBuilder) super.setColor(r, g, b);
        }

        @Override
        public WorldVFXBuilder setColor(float r, float g, float b) {
            return (WorldVFXBuilder) super.setColor(r, g, b);
        }

        @Override
        public WorldVFXBuilder multiplyColor(float scalar) {
            return (WorldVFXBuilder) super.multiplyColor(scalar);
        }

        @Override
        public WorldVFXBuilder multiplyColor(float r, float g, float b) {
            return (WorldVFXBuilder) super.multiplyColor(r, g, b);
        }

        @Override
        public WorldVFXBuilder setAlpha(int a) {
            return (WorldVFXBuilder) super.setAlpha(a);
        }

        @Override
        public WorldVFXBuilder setAlpha(float a) {
            return (WorldVFXBuilder) super.setAlpha(a);
        }

        @Override
        public WorldVFXBuilder setLight(int light) {
            return (WorldVFXBuilder) super.setLight(light);
        }

        @Override
        public WorldVFXBuilder setUVWithWidth(float u, float v, float width, float height, float canvasSize) {
            return (WorldVFXBuilder) super.setUVWithWidth(u, v, width, height, canvasSize);
        }

        @Override
        public WorldVFXBuilder setUVWithWidth(float u, float v, float width, float height, float canvasSizeX, float canvasSizeY) {
            return (WorldVFXBuilder) super.setUVWithWidth(u, v, width, height, canvasSizeX, canvasSizeY);
        }

        @Override
        public WorldVFXBuilder setUVWithWidth(float u, float v, float width, float height) {
            return (WorldVFXBuilder) super.setUVWithWidth(u, v, width, height);
        }

        @Override
        public WorldVFXBuilder setUV(float u0, float v0, float u1, float v1, float canvasSize) {
            return (WorldVFXBuilder) super.setUV(u0, v0, u1, v1, canvasSize);
        }

        @Override
        public WorldVFXBuilder setUV(float u0, float v0, float u1, float v1, float canvasSizeX, float canvasSizeY) {
            return (WorldVFXBuilder) super.setUV(u0, v0, u1, v1, canvasSizeX, canvasSizeY);
        }

        @Override
        public WorldVFXBuilder setUV(float u0, float v0, float u1, float v1) {
            return (WorldVFXBuilder) super.setUV(u0, v0, u1, v1);
        }

        @Override
        public WorldVFXBuilder setFormatRaw(VertexFormat format) {
            return (WorldVFXBuilder) super.setFormatRaw(format);
        }

        @Override
        public WorldVFXBuilder setVertexSupplier(VertexConsumerActor supplier) {
            return (WorldVFXBuilder) super.setVertexSupplier(supplier);
        }

        @Override
        public WorldVFXBuilder setFormat(VertexFormat format) {
            return (WorldVFXBuilder) super.setFormat(format);
        }

        public WorldVFXBuilder setRenderType(LodestoneRenderTypeBuilder renderType) {
            return setRenderType(renderType.getRenderType());
        }

        public WorldVFXBuilder setRenderType(RenderType renderType) {
            return setRenderTypeRaw(renderType).setFormat(renderType.format()).setVertexConsumer(getBufferSource().getBuffer(renderType));
        }

        public WorldVFXBuilder setRenderTypeRaw(RenderType renderType) {
            this.renderType = renderType;
            return this;
        }

        public WorldVFXBuilder setVertexConsumer(VertexConsumer vertexConsumer) {
            this.vertexConsumer = vertexConsumer;
            return this;
        }

        public VertexConsumer getVertexConsumer() {
            if (vertexConsumer == null) {
                setVertexConsumer(getBufferSource().getBuffer(getRenderType()));
            }
            return vertexConsumer;
        }

        public WorldVFXBuilder replaceBufferSource(LodestoneRenderLayer renderLayer) {
            this.renderLayer = renderLayer;
            this.vertexConsumer = null;
            return this;
        }

        public WorldVFXBuilder replaceBufferSource(MultiBufferSource bufferSource) {
            this.bufferSource = bufferSource;
            this.vertexConsumer = null;
            return this;
        }

        protected MultiBufferSource getBufferSource() {
            if (bufferSource == null) {
                replaceBufferSource(renderLayer.getTarget());
            }
            return bufferSource;
        }

        public WorldVFXBuilder usePartialTicks(float partialTicks) {
            this.usePartialTicks = true;
            this.partialTicks = partialTicks;
            return this;
        }

        @SuppressWarnings({"DataFlowIssue", "deprecation"})
        public WorldVFXBuilder setLightLevel(BlockPos pos) {
            ClientLevel level = minecraft.level;
            int light = level.hasChunkAt(pos) ? LevelRenderer.getLightColor(level, pos) : 0;
            return setLight(light);
        }

        protected RenderType getRenderType() {
            return renderType;
        }

        protected VertexFormat getFormat() {
            return format;
        }

        public VertexConsumerActor getSupplier() {
            return supplier;
        }

        protected Vec3 getCameraPosition() {
            return minecraft.getBlockEntityRenderDispatcher().camera.getPosition();
        }

        protected Matrix4f getOffsetViewMatrix() {
            var pose = new Matrix4f(LodestoneRenderHandler.MODEL_VIEW);
            var cameraPosition = getCameraPosition().toVector3f();
            return pose.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
        }

        public WorldVFXBuilder renderBeam(@Nullable PoseStack.Pose last, BlockPos start, BlockPos end, float width) {
            return renderBeam(last, start.getCenter(), end.getCenter(), width, getCameraPosition());
        }

        public WorldVFXBuilder renderBeam(@Nullable PoseStack.Pose last, Vec3 start, Vec3 end, float width) {
            return renderBeam(last, start, end, width, getCameraPosition());
        }

        public WorldVFXBuilder renderBeam(@Nullable PoseStack.Pose last, Vec3 start, Vec3 end, float width, Consumer<WorldVFXBuilder> consumer) {
            return renderBeam(last, start, end, width, getCameraPosition(), consumer);
        }

        public WorldVFXBuilder renderBeam(@Nullable PoseStack.Pose last, Vec3 start, Vec3 end, float width, Vec3 cameraPosition) {
            return renderBeam(last, start, end, width, cameraPosition, builder -> {
            });
        }

        public WorldVFXBuilder renderBeam(@Nullable PoseStack.Pose last, Vec3 start, Vec3 end, float width, Vec3 cameraPosition, Consumer<WorldVFXBuilder> consumer) {
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

        public WorldVFXBuilder renderTrail(TrailPointBuilder trailPoints, float width) {
            return renderTrail(trailPoints, f -> width, f -> {
            });
        }

        public WorldVFXBuilder renderTrail(TrailPointBuilder trailPoints, Function<Float, Float> widthFunc) {
            return renderTrail(trailPoints, widthFunc, f -> {
            });
        }

        public WorldVFXBuilder renderTrail(TrailPointBuilder builder, Function<Float, Float> widthFunc, Consumer<Float> vfxOperator) {
            var trailPoints = builder.getTrailPoints();
            if (trailPoints.size() < 2) {
                return this;
            }
            var pose = getOffsetViewMatrix();
            var positions = usePartialTicks ? builder.build(pose, partialTicks) : builder.build(pose);
            positions.getLast().set(TrailPoint.getMatrixPosition(builder.getOrigin(), pose));
            int count = trailPoints.size() - 1;
            float increment = 1.0F / count;
            TrailPointRenderData[] renderData = new TrailPointRenderData[trailPoints.size()];
            for (int i = 1; i < count; i++) {
                float width = widthFunc.apply(increment * i);
                Vector4f previous = positions.get(i - 1);
                Vector4f current = positions.get(i);
                Vector4f next = positions.get(i + 1);
                renderData[i] = new TrailPointRenderData(current, RenderHelper.perpendicularTrailPoints(previous, next, width));
            }
            Vector4f first = positions.get(0);
            Vector4f second = positions.get(1);
            Vector4f secondToLast = positions.get(count - 1);
            Vector4f last = positions.get(count);
            renderData[0] = new TrailPointRenderData(first, RenderHelper.perpendicularTrailPoints(first, second, widthFunc.apply(0f)));
            renderData[count] = new TrailPointRenderData(last, RenderHelper.perpendicularTrailPoints(secondToLast, last, widthFunc.apply(1f)));
            return renderConnectedPoints(renderData, u0, v0, u1, v1, vfxOperator);
        }

        public WorldVFXBuilder renderConnectedPoints(TrailPointRenderData[] points, float u0, float v0, float u1, float v1, Consumer<Float> vfxOperator) {
            replaceBufferSource(renderLayer.getTrailTarget());
            int count = points.length - 1;
            float increment = 1.0F / count;
            vfxOperator.accept(0f);
            points[0].renderStart(getVertexConsumer(), this, u0, v0, u1);
            for (int i = 1; i < count; i++) {
                float current = Mth.lerp(i * increment, v0, v1);
                vfxOperator.accept(current);
                points[i].renderMid(getVertexConsumer(), this, u0, current, u1, current);
            }
            vfxOperator.accept(1f);
            points[count].renderEnd(getVertexConsumer(), this, u0, u1, v1);
            return this;
        }

        public WorldVFXBuilder renderCube(PoseStack poseStack, CubeVertexData cubeVertexData) {
            var topVertices = cubeVertexData.topVertices().vertices();
            var bottomVertices = cubeVertexData.bottomVertices().invert();

            renderQuad(poseStack, topVertices);
            renderQuad(poseStack, bottomVertices);
            for (CubeVertexData.CubeVertices horizontal : cubeVertexData.byHorizontalDirection()) {
                renderQuad(poseStack, horizontal.vertices());
            }
            return this;
        }

        public WorldVFXBuilder renderCubeSides(PoseStack poseStack, CubeVertexData cubeVertexData, Direction... directions) {
            return renderCubeSides(poseStack, cubeVertexData, Arrays.asList(directions));
        }

        public WorldVFXBuilder renderCubeSides(PoseStack poseStack, CubeVertexData cubeVertexData, Collection<Direction> directions) {
            for (Direction direction : directions) {
                renderCubeSide(poseStack, cubeVertexData, direction);
            }
            return this;
        }

        public WorldVFXBuilder renderCubeSide(PoseStack poseStack, CubeVertexData cubeVertexData, Direction direction) {
            Vector3f[] vertices = cubeVertexData.getVerticesByDirection(direction);
            renderQuad(poseStack, vertices);
            return this;
        }

        public WorldVFXBuilder renderQuad(PoseStack stack) {
            return renderQuad(stack, 1f);
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
            return renderQuad(stack, positions);
        }

        public WorldVFXBuilder renderQuad(PoseStack stack, Vector3f[] positions) {
            var consumer = getVertexConsumer();
            supplier.placeVertex(consumer, stack, this, positions[0].x(), positions[0].y(), positions[0].z(), u0, v1);
            supplier.placeVertex(consumer, stack, this, positions[1].x(), positions[1].y(), positions[1].z(), u1, v1);
            supplier.placeVertex(consumer, stack, this, positions[2].x(), positions[2].y(), positions[2].z(), u1, v0);
            supplier.placeVertex(consumer, stack, this, positions[3].x(), positions[3].y(), positions[3].z(), u0, v0);
            return this;
        }

        public WorldVFXBuilder placeVertex(PoseStack stack, float x, float y, float z) {
            supplier.placeVertex(getVertexConsumer(), stack.last(), this, x, y, z, u0, v1);
            return this;
        }

        /**
         * RenderSphere requires a triangle-based RenderType.
         */
        public WorldVFXBuilder renderSphere(PoseStack stack, float radius, int longs, int lats) {
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
                    supplier.placeVertex(getVertexConsumer(), stack, this, p0.x(), p0.y(), p0.z(), textureU, textureV);
                    supplier.placeVertex(getVertexConsumer(), stack, this, p2.x(), p2.y(), p2.z(), textureUN, textureV);
                    supplier.placeVertex(getVertexConsumer(), stack, this, p1.x(), p1.y(), p1.z(), textureU, textureVN);

                    supplier.placeVertex(getVertexConsumer(), stack, this, p3.x(), p3.y(), p3.z(), textureUN, textureVN);
                    supplier.placeVertex(getVertexConsumer(), stack, this, p1.x(), p1.y(), p1.z(), textureU, textureVN);
                    supplier.placeVertex(getVertexConsumer(), stack, this, p2.x(), p2.y(), p2.z(), textureUN, textureV);
                }
            }
            return this;
        }

        /**
         * RenderTorus requires a triangle-based RenderType.
         */
        public WorldVFXBuilder renderTorus(PoseStack stack, float majorRadius, float minorRadius, int majorSegments, int minorSegments) {
            float TAU = (float) (Math.PI * 2);

            for (int i = 0; i < majorSegments; ++i) {
                float u0n = (float) i / majorSegments;
                float u1n = (float) (i + 1) / majorSegments;
                float u0 = u0n * TAU;
                float u1 = u1n * TAU;

                for (int j = 0; j < minorSegments; ++j) {
                    float v0n = (float) j / minorSegments;
                    float v1n = (float) (j + 1) / minorSegments;
                    float v0 = v0n * TAU;
                    float v1 = v1n * TAU;

                    Vector3f p0 = RenderHelper.parametricTorus(u0, v0, majorRadius, minorRadius);
                    Vector3f p1 = RenderHelper.parametricTorus(u0, v1, majorRadius, minorRadius);
                    Vector3f p2 = RenderHelper.parametricTorus(u1, v0, majorRadius, minorRadius);
                    Vector3f p3 = RenderHelper.parametricTorus(u1, v1, majorRadius, minorRadius);

                    supplier.placeVertex(getVertexConsumer(), stack, this, p0.x(), p0.y(), p0.z(), u0n, v0n);
                    supplier.placeVertex(getVertexConsumer(), stack, this, p2.x(), p2.y(), p2.z(), u1n, v0n);
                    supplier.placeVertex(getVertexConsumer(), stack, this, p1.x(), p1.y(), p1.z(), u0n, v1n);

                    supplier.placeVertex(getVertexConsumer(), stack, this, p3.x(), p3.y(), p3.z(), u1n, v1n);
                    supplier.placeVertex(getVertexConsumer(), stack, this, p1.x(), p1.y(), p1.z(), u0n, v1n);
                    supplier.placeVertex(getVertexConsumer(), stack, this, p2.x(), p2.y(), p2.z(), u1n, v0n);
                }
            }
            return this;
        }
    }
}