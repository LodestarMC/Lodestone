package team.lodestar.lodestone.systems.model.geo.data;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GeoQuad implements IRenderableModelPart<GeoQuad> {
    GeoVertex[] vertices;

    public GeoQuad(GeoVertex[] vertices) {
        this.vertices = vertices;
    }

    public static GeoQuad build(GeoVertex[] vertices, Vector2f uv, Vector2f uvSize, int rotation, Vector2f textureSize) {
        Vector2f uv0 = new Vector2f(uv).div(textureSize);
        Vector2f uv1 = new Vector2f(uv).add(uvSize).div(textureSize);

        float tmp = uv1.x;
        uv1.x = uv0.x;
        uv0.x = tmp;

        Vector2f[] rotatedUVs = rotateUVs(uv0, uv1, rotation);
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = vertices[i].copy();
            vertices[i].setTexCoords(rotatedUVs[i]);
        }

        return new GeoQuad(vertices);
    }

    private static Vector2f[] rotateUVs(Vector2f uv0, Vector2f uv1, int rotation) {
        Vector2f a = new Vector2f(uv1.x, uv0.y);
        Vector2f b = new Vector2f(uv0.x, uv1.y);
        return switch (rotation) {
            case 0 -> new Vector2f[] {uv0, a, uv1, b};
            case 90 -> new Vector2f[] {a, uv1, b, uv0};
            case 180 -> new Vector2f[] {uv1, b, uv0, a};
            case 270 -> new Vector2f[] {b, uv0, a, uv1};
            default -> throw new IllegalArgumentException("Invalid rotation: " + rotation);
        };
    }

    @Override
    public GeoQuad copy() {
        GeoVertex[] copiedVertices = new GeoVertex[this.vertices.length];
        for (int i = 0; i < this.vertices.length; i++) {
            copiedVertices[i] = this.vertices[i].copy();
        }
        return new GeoQuad(copiedVertices);
    }

    @Override
    public void render(PoseStack poseStack, VertexConsumer vertexConsumer, VertexFormat vertexFormat, VertexFormat.Mode mode) {
        for (GeoVertex vertex : this.vertices) {
            Vector3f pos = vertex.getPosition();
            vertexConsumer.addVertex(poseStack.last().pose(), pos.x, pos.y, pos.z)
                    .setUv(vertex.getTexCoord().x, vertex.getTexCoord().y)
                    .setColor(255, 255, 255, 255);
        }
    }
}
