package team.lodestar.lodestone.systems.rendering.ghost;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import team.lodestar.lodestone.handlers.PlacementAssistantHandler;
import team.lodestar.lodestone.handlers.RenderHandler;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

public abstract class GhostBlockRenderer {

    public static final GhostBlockRenderer STANDARD = new DefaultGhostBlockRenderer();
    public static final GhostBlockRenderer TRANSPARENT = new TransparentGhostBlockRenderer();

    public static GhostBlockRenderer standard() {
        return STANDARD;
    }

    public static GhostBlockRenderer transparent() {
        return TRANSPARENT;
    }

    public abstract void render(PoseStack ps, GhostBlockOptions params);

    private static class DefaultGhostBlockRenderer extends GhostBlockRenderer {
        @Override
        public void render(PoseStack ps, GhostBlockOptions options) {
            ps.pushPose();
            BlockRenderDispatcher dispatch = Minecraft.getInstance().getBlockRenderer();
            BakedModel bakedModel = dispatch.getBlockModel(options.blockState);
            RenderType renderType = ItemBlockRenderTypes.getRenderType(options.blockState, false);
            VertexConsumer consumer = RenderHandler.DELAYED_RENDER.getTarget().getBuffer(renderType);
            BlockPos pos = options.blockPos;

            ps.translate(pos.getX(), pos.getY(), pos.getZ());
            dispatch.getModelRenderer().renderModel(
                    ps.last(),
                    consumer,
                    options.blockState,
                    bakedModel,
                    1.0F,
                    1.0F,
                    1.0F,
                    LightTexture.FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY
            );
            ps.popPose();
        }
    }

    private static class TransparentGhostBlockRenderer extends GhostBlockRenderer {
        @Override
        public void render(PoseStack ps, GhostBlockOptions options) {
            ps.pushPose();
            Minecraft minecraft = Minecraft.getInstance();
            BlockRenderDispatcher dispatch = minecraft.getBlockRenderer();
            BakedModel bakedModel = dispatch.getBlockModel(options.blockState);
            RenderType renderType = RenderType.translucent();
            VertexConsumer consumer = RenderHandler.DELAYED_RENDER.getTarget().getBuffer(renderType);
            BlockPos pos = options.blockPos;

            ps.translate(pos.getX(), pos.getY(), pos.getZ());

            ps.translate(0.5D, 0.5D, 0.5D);
            ps.scale(0.85F, 0.85F, 0.85F);
            ps.translate(-0.5D, -0.5D, -0.5D);

            float alpha = options.alphaSupplier.get() * 0.75F * PlacementAssistantHandler.getCurrentAlpha();
            renderModel(
                    ps.last(),
                    consumer,
                    options.blockState,
                    bakedModel,
                    1.0F,
                    1.0F,
                    1.0F,
                    alpha,
                    LevelRenderer.getLightColor(minecraft.level, pos),
                    OverlayTexture.NO_OVERLAY,
                    minecraft.level.getRandom());

            ps.popPose();
        }

        public static void renderModel(PoseStack.Pose pose, VertexConsumer consumer, BlockState state, BakedModel model, float red, float green, float blue, float alpha, int packedLight, int packedOverlay, RandomSource random) {
            for (Direction direction : Direction.values()) {
                random.setSeed(42L);
                renderQuadList(pose, consumer, red, green, blue, alpha, model.getQuads(state, direction, random), packedLight, packedOverlay);
            }
            random.setSeed(42L);
            renderQuadList(pose, consumer, red, green, blue, alpha, model.getQuads(state, null, random), packedLight, packedOverlay);
        }

        private static void renderQuadList(PoseStack.Pose pose, VertexConsumer consumer, float red, float green, float blue, float alpha, List<BakedQuad> quads, int packedLight, int packedOverlay) {
            for (BakedQuad quad : quads) {
                float r, g, b;
                if (quad.isTinted()) {
                    r = Mth.clamp(red, 0.0F, 1.0F);
                    g = Mth.clamp(green, 0.0F, 1.0F);
                    b = Mth.clamp(blue, 0.0F, 1.0F);
                } else {
                    r = 1.0F;
                    g = 1.0F;
                    b = 1.0F;
                }
                putBulkData(consumer, pose, quad, new float[]{1.0F, 1.0F, 1.0F, 1.0F}, r, g, b, alpha, new int[]{packedLight, packedLight, packedLight, packedLight}, packedOverlay, true);
            }
        }

        private static int findOffset(VertexFormatElement element) {
            // Divide by 4 because we want the int offset
            var index = DefaultVertexFormat.BLOCK.getElements().indexOf(element);
            return index < 0 ? -1 : DefaultVertexFormat.BLOCK.getOffset(index) / 4;
        }

        static int applyBakedLighting(int packedLight, ByteBuffer data) {
            int bl = packedLight & 0xFFFF;
            int sl = (packedLight >> 16) & 0xFFFF;
            int offset = findOffset(DefaultVertexFormat.ELEMENT_UV2) * 4; // int offset for vertex 0 * 4 bytes per int
            int blBaked = Short.toUnsignedInt(data.getShort(offset));
            int slBaked = Short.toUnsignedInt(data.getShort(offset + 2));
            bl = Math.max(bl, blBaked);
            sl = Math.max(sl, slBaked);
            return bl | (sl << 16);
        }

        static void applyBakedNormals(Vector3f generated, ByteBuffer data, Matrix3f normalTransform) {
            byte nx = data.get(28);
            byte ny = data.get(29);
            byte nz = data.get(30);
            if (nx != 0 || ny != 0 || nz != 0) {
                generated.set(nx / 127f, ny / 127f, nz / 127f);
                generated.mul(normalTransform);
            }
        }

        static void putBulkData(VertexConsumer vertexConsumer, PoseStack.Pose poseEntry, BakedQuad quad, float[] colorMuls, float red, float green, float blue, float alpha, int[] combinedLights, int combinedOverlay, boolean mulColor) {
            float[] fs = new float[]{colorMuls[0], colorMuls[1], colorMuls[2], colorMuls[3]};
            int[] is = new int[]{combinedLights[0], combinedLights[1], combinedLights[2], combinedLights[3]};
            int[] js = quad.getVertices();
            Vec3i vec3i = quad.getDirection().getNormal();
            Matrix4f matrix4f = poseEntry.pose();
            Vector3f vector3f = poseEntry.normal().transform(new Vector3f((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ()));
            int j = js.length / 8;
            MemoryStack memoryStack = MemoryStack.stackPush();

            try {
                ByteBuffer byteBuffer = memoryStack.malloc(DefaultVertexFormat.BLOCK.getVertexSize());
                IntBuffer intBuffer = byteBuffer.asIntBuffer();

                for (int k = 0; k < j; ++k) {
                    intBuffer.clear();
                    intBuffer.put(js, k * 8, 8);
                    float f = byteBuffer.getFloat(0);
                    float g = byteBuffer.getFloat(4);
                    float h = byteBuffer.getFloat(8);
                    float o;
                    float p;
                    float q;
                    float m;
                    float n;
                    if (mulColor) {
                        float l = (float) (byteBuffer.get(12) & 255) / 255.0F;
                        m = (float) (byteBuffer.get(13) & 255) / 255.0F;
                        n = (float) (byteBuffer.get(14) & 255) / 255.0F;
                        o = l * fs[k] * red;
                        p = m * fs[k] * green;
                        q = n * fs[k] * blue;
                    } else {
                        o = fs[k] * red;
                        p = fs[k] * green;
                        q = fs[k] * blue;
                    }

                    //int r = is[k];
                    int r = applyBakedLighting(combinedLights[k], byteBuffer);
                    m = byteBuffer.getFloat(16);
                    n = byteBuffer.getFloat(20);
                    Vector4f vector4f = matrix4f.transform(new Vector4f(f, g, h, 1.0F));
                    applyBakedNormals(vector3f, byteBuffer, poseEntry.normal());
                    float vertexAlpha = mulColor ? alpha * (float) (byteBuffer.get(15) & 255) / 255.0F : alpha;
                    vertexConsumer.vertex(vector4f.x(), vector4f.y(), vector4f.z(), o, p, q, vertexAlpha, m, n, combinedOverlay, r, vector3f.x(), vector3f.y(), vector3f.z());
                    //vertexConsumer.vertex(vector4f.x(), vector4f.y(), vector4f.z(), o, p, q, 1.0F, m, n, combinedOverlay, r, vector3f.x(), vector3f.y(), vector3f.z());
                }
            } catch (Throwable var33) {
                if (memoryStack != null) {
                    try {
                        memoryStack.close();
                    } catch (Throwable var32) {
                        var33.addSuppressed(var32);
                    }
                }

                throw var33;
            }

            if (memoryStack != null) {
                memoryStack.close();
            }

        }
    }
}