package team.lodestar.lodestone.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import team.lodestar.lodestone.systems.rendering.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class RenderHelper {
    public static final int FULL_BRIGHT = 15728880;

    public static ShaderInstance getShader(RenderType type) {
        if (type instanceof LodestoneRenderType renderType) {
            Optional<Supplier<ShaderInstance>> shader = renderType.state.shaderState.shader;
            if (shader.isPresent()) {
                return shader.get().get();
            }
        }
        return null;
    }

    public static RenderStateShard.TransparencyStateShard getTransparencyShard(RenderType type) {
        if (type instanceof LodestoneRenderType compositeRenderType) {
            return compositeRenderType.state.transparencyState;
        }
        return null;
    }

    public static void vertexPos(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z) {
        vertexConsumer.addVertex(last, x, y, z);
    }

    public static void vertexPosUV(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float u, float v) {
        vertexConsumer.addVertex(last, x, y, z).setUv(u, v);
    }

    public static void vertexPosUVLight(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float u, float v, int light) {
        vertexConsumer.addVertex(last, x, y, z).setUv(u, v).setLight(light);
    }

    public static void vertexPosColor(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float r, float g, float b, float a) {
        vertexConsumer.addVertex(last, x, y, z).setColor(r, g, b, a);
    }

    public static void vertexPosColorUV(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float r, float g, float b, float a, float u, float v) {
        vertexConsumer.addVertex(last, x, y, z).setColor(r, g, b, a).setUv(u, v);
    }

    public static void vertexPosColorUVLight(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float r, float g, float b, float a, float u, float v, int light) {
        vertexConsumer.addVertex(last, x, y, z).setColor(r, g, b, a).setUv(u, v).setLight(light);
    }

    public static Vector3f parametricSphere(float u, float v, float r) {
        return new Vector3f(Mth.cos(u) * Mth.sin(v) * r, Mth.cos(v) * r, Mth.sin(u) * Mth.sin(v) * r);
    }

    public static Vec2 perpendicularTrailPoints(Vector4f start, Vector4f end, float width) {
        float x = -start.x();
        float y = -start.y();
        if (Math.abs(start.z()) > 0) {
            float ratio = end.z() / start.z();
            x = end.x() + x * ratio;
            y = end.y() + y * ratio;
        } else if (Math.abs(end.z()) <= 0) {
            x += end.x();
            y += end.y();
        }
        if (start.z() > 0) {
            x = -x;
            y = -y;
        }
        if (x * x + y * y > 0F) {
            float normalize = width * 0.5F / DataHelper.distance(x, y);
            x *= normalize;
            y *= normalize;
        }
        return new Vec2(-y, x);
    }

    public static Vector4f midpoint(Vector4f a, Vector4f b) {
        return new Vector4f((a.x() + b.x()) * 0.5F, (a.y() + b.y()) * 0.5F, (a.z() + b.z()) * 0.5F, (a.w() + b.w()) * 0.5F);
    }

    public static Vec2 worldPosToTexCoord(Vector3f worldPos, PoseStack viewModelStack) {
        Matrix4f viewMat = viewModelStack.last().pose();
        Matrix4f projMat = RenderSystem.getProjectionMatrix();

        Vector3f localPos = new Vector3f(worldPos);
        localPos.sub(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().toVector3f());

        Vector4f pos = new Vector4f(localPos, 0);
        pos.mul(viewMat);
        pos.mul(projMat);
        VecHelper.Vector4fHelper.perspectiveDivide(pos);

        return new Vec2((pos.x() + 1F) / 2F, (pos.y() + 1F) / 2F);
    }

    /**
     * Draw a segmented line between two points, subdividing the line into a number of segments
     *
     * @param buffer    The buffer to draw to
     * @param ps        The pose stack to draw with
     * @param lineWidth The width of the line
     * @param points    The points to draw between
     **/

    public static void drawSteppedLineBetween(MultiBufferSource buffer, PoseStack ps, List<Vec3> points, float lineWidth, int r, int g, int b, int a) {
        Vec3 origin = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            Vec3 target = points.get(i);
            drawLineBetween(buffer, ps, origin, target, lineWidth, r, g, b, a);
            origin = target;
        }
    }

    /**
     * Draw a segmented line between two points, subdividing the line into a number of segments
     *
     * @param buffer        The buffer to draw to
     * @param ps            The pose stack to draw with
     * @param start         The start point
     * @param end           The end point
     * @param steps         The number of steps to divide the line into
     * @param lineWidth     The width of the line
     * @param pointConsumer A consumer to call for each point in the line
     */

    public static void drawSteppedLineBetween(MultiBufferSource buffer, PoseStack ps, Vec3 start, Vec3 end, int steps, float lineWidth, int r, int g, int b, int a, Consumer<Vec3> pointConsumer) {
        Vec3 origin = start;
        for (int i = 1; i <= steps; i++) {
            Vec3 target = start.add(end.subtract(start).scale(i / (float) steps));
            pointConsumer.accept(target);
            drawLineBetween(buffer, ps, origin, target, lineWidth, r, g, b, a);
            origin = target;
        }
    }

    /**
     * Draw a line between two points
     *
     * @param buffer    The buffer to draw to
     * @param ps        The pose stack to draw with
     * @param local     The start point
     * @param target    The end point
     * @param lineWidth The width of the line
     */

    public static void drawLineBetween(MultiBufferSource buffer, PoseStack ps, Vec3 local, Vec3 target, float lineWidth, int r, int g, int b, int a) {
        VertexConsumer builder = buffer.getBuffer(RenderType.leash());

        //Calculate yaw
        float rotY = (float) Mth.atan2(target.x - local.x, target.z - local.z);

        //Calculate pitch
        double distX = target.x - local.x;
        double distZ = target.z - local.z;
        float rotX = (float) Mth.atan2(target.y - local.y, Mth.sqrt((float) (distX * distX + distZ * distZ)));

        ps.pushPose();

        //Translate to start point
        ps.translate(local.x, local.y, local.z);
        //Rotate to point towards end point
        ps.mulPose(VecHelper.Vector3fHelper.rotation(rotY, VecHelper.Vector3fHelper.YP));
        ps.mulPose(VecHelper.Vector3fHelper.rotation(rotX, VecHelper.Vector3fHelper.XN));

        //Calculate distance between points -> length of the line
        float distance = (float) local.distanceTo(target);

        Matrix4f matrix = ps.last().pose();
        float halfWidth = lineWidth / 2F;

        //Draw horizontal quad
        builder.addVertex(matrix, -halfWidth, 0, 0).setColor(r, g, b, a).setLight(0xF000F0);
        builder.addVertex(matrix, halfWidth, 0, 0).setColor(r, g, b, a).setLight(0xF000F0);
        builder.addVertex(matrix, halfWidth, 0, distance).setColor(r, g, b, a).setLight(0xF000F0);
        builder.addVertex(matrix, -halfWidth, 0, distance).setColor(r, g, b, a).setLight(0xF000F0);

        //Draw vertical Quad
        builder.addVertex(matrix, 0, -halfWidth, 0).setColor(r, g, b, a).setLight(0xF000F0);
        builder.addVertex(matrix, 0, halfWidth, 0).setColor(r, g, b, a).setLight(0xF000F0);
        builder.addVertex(matrix, 0, halfWidth, distance).setColor(r, g, b, a).setLight(0xF000F0);
        builder.addVertex(matrix, 0, -halfWidth, distance).setColor(r, g, b, a).setLight(0xF000F0);

        ps.popPose();
    }
}