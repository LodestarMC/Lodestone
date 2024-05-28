package team.lodestar.lodestone.systems.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;
import java.util.Locale;

public record Face(List<Vector3f> vertices, List<Vector3f> normals, List<Vec2> uvs) {
    public void renderFace(PoseStack poseStack, VertexConsumer buffer, int packedLight) {
        if (vertices.size() == 3) {
            renderTriangle(poseStack, buffer, packedLight);
        } else if (vertices.size() == 4) {
            renderQuad(poseStack, buffer, packedLight);
        } else {
            throw new RuntimeException("Face has invalid number of vertices. Supported vertex counts are 3 and 4.");
        }
    }
    public void renderTriangle(PoseStack poseStack, VertexConsumer buffer, int packedLight) {
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f normalMatrix = poseStack.last().normal();
        List<Vector3f> vertices = this.vertices();
        List<Vector3f> normals = this.normals();
        List<Vec2> uvs = this.uvs();

        for (int i = 0; i < 3; i++) {
            addVertex(buffer, matrix4f, vertices.get(i), uvs.get(i), normalMatrix, normals.get(i), packedLight);
        }
        addVertex(buffer, matrix4f, vertices.get(0), uvs.get(0), normalMatrix, normals.get(0), packedLight);
    }

    public void renderQuad(PoseStack poseStack, VertexConsumer buffer, int packedLight) {
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f normalMatrix = poseStack.last().normal();
        List<Vector3f> vertices = this.vertices();
        List<Vector3f> normal = this.normals();
        List<Vec2> uvs = this.uvs();

        for (int i = 0; i < 4; i++) {
            addVertex(buffer, matrix4f, vertices.get(i), uvs.get(i), normalMatrix, normals.get(i), packedLight);
        }
    }

    private void addVertex(VertexConsumer buffer, Matrix4f matrix4f, Vector3f vertex, Vec2 uv, Matrix3f normalMatrix, Vector3f normal, int packedLight) {
        buffer.vertex(matrix4f, vertex.x(), vertex.y(), vertex.z())
                .color(255, 255, 255, 255)
                .uv(uv.x, -uv.y)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .normal(normalMatrix, normal.x(), normal.y(), normal.z())
                .uv2(packedLight)
                .endVertex();
    }

    public Vector3f getCentroid() {
        Vector3f centroid = new Vector3f();
        for (Vector3f vertex : vertices) {
            centroid.add(vertex);
        }
        centroid.div(vertices.size());
        return centroid;
    }
}
