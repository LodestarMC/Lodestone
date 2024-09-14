package team.lodestar.lodestone.systems.model.obj.data;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4i;
import team.lodestar.lodestone.systems.model.obj.ObjParser;

/**
 * Represents a vertex with position, normal, uv, color, light, and overlay data.
 * <p>Each vertex attribute uses a {@link FallbackPair} to ensure that default values are used if the data is not present</p>
 * <p>To set your own data, use the {@link VertexConsumer} methods</p>
 */
public class Vertex implements VertexConsumer {
    private final FallbackPair<Vector3f> position;
    private final FallbackPair<Vector3f> normal;
    private final FallbackPair<Vector2f> uv;
    private final FallbackPair<Vector4i> color;
    private final FallbackPair<Integer> packedLight;
    private final FallbackPair<Integer> packedOverlay;

    public Vertex(IndexedVertex indexedVertex, ObjParser.Builder builder) {
        this.position = FallbackPair.ofDefault(builder.positions.get(indexedVertex.positionIndex()));
        this.normal = FallbackPair.ofDefault(builder.normals.get(indexedVertex.normalIndex()));
        this.uv = FallbackPair.ofDefault(builder.uvs.get(indexedVertex.uvIndex()));
        this.color = FallbackPair.ofDefault(new Vector4i(255, 255, 255, 255));
        this.packedLight = FallbackPair.ofDefault(LightTexture.FULL_BRIGHT);
        this.packedOverlay = FallbackPair.ofDefault(OverlayTexture.NO_OVERLAY);
    }

    public Vertex(Vector3f position, Vector3f normal, Vector2f uv, Vector4i color, int packedLight, int packedOverlay) {
        this.position = FallbackPair.ofDefault(position);
        this.normal = FallbackPair.ofDefault(normal);
        this.uv = FallbackPair.ofDefault(uv);
        this.color = FallbackPair.ofDefault(color);
        this.packedLight = FallbackPair.ofDefault(packedLight);
        this.packedOverlay = FallbackPair.ofDefault(packedOverlay);
    }

    public Vector3f getPosition() {
        return this.position.get();
    }

    public Vector3f getNormal() {
        return this.normal.get();
    }

    public Vector2f getUv() {
        return this.uv.get();
    }

    public Vector4i getColor() {
        return this.color.get();
    }

    public int getPackedLight() {
        return this.packedLight.get();
    }

    public int getPackedOverlay() {
        return this.packedOverlay.get();
    }

    @Override
    @NotNull
    public VertexConsumer addVertex(float v, float v1, float v2) {
        this.position.setOverride(new Vector3f(v, v1, v2));
        return this;
    }

    @Override
    @NotNull
    public VertexConsumer setColor(int i, int i1, int i2, int i3) {
        this.color.setOverride(new Vector4i(i, i1, i2, i3));
        return this;
    }

    @Override
    @NotNull
    public VertexConsumer setUv(float v, float v1) {
        this.uv.setOverride(new Vector2f(v, v1));
        return this;
    }

    @Override
    @NotNull
    public VertexConsumer setUv1(int i, int i1) {
        this.packedOverlay.setOverride(OverlayTexture.pack(i, i1));
        return this;
    }

    @Override
    @NotNull
    public VertexConsumer setUv2(int i, int i1) {
        this.packedLight.setOverride(LightTexture.pack(i, i1));
        return this;
    }

    @Override
    @NotNull
    public VertexConsumer setNormal(float v, float v1, float v2) {
        this.normal.setOverride(new Vector3f(v, v1, v2));
        return this;
    }

    public void supplyPosition(VertexConsumer vertexConsumer, PoseStack poseStack) {
        Vector3f position = this.getPosition();
        vertexConsumer.addVertex(poseStack.last().pose(), position.x(), position.y(), position.z());
    }

    public void supplyColor(VertexConsumer vertexConsumer) {
        Vector4i color = this.getColor();
        vertexConsumer.setColor(color.x, color.y, color.z, color.w);
    }

    public void supplyUv(VertexConsumer vertexConsumer) {
        Vector2f uv = this.getUv();
        vertexConsumer.setUv(uv.x, uv.y);
    }

    public void supplyUv1(VertexConsumer vertexConsumer) {
        int packedOverlay = this.getPackedOverlay();
        vertexConsumer.setOverlay(packedOverlay);
    }

    public void supplyUv2(VertexConsumer vertexConsumer) {
        int packedLight = this.getPackedLight();
        vertexConsumer.setLight(packedLight);
    }

    public void supplyNormal(VertexConsumer vertexConsumer, PoseStack poseStack) {
        Vector3f normal = this.getNormal();
        vertexConsumer.setNormal(poseStack.last(), normal.x, normal.y, normal.z);
    }

    public void supplyVertexData(VertexConsumer vertexConsumer, VertexFormat format, PoseStack poseStack) {
        format.getElements().forEach(element -> {
            if (element == VertexFormatElement.POSITION) {
                this.supplyPosition(vertexConsumer, poseStack);
            } else if (element == VertexFormatElement.COLOR) {
                this.supplyColor(vertexConsumer);
            } else if (element == VertexFormatElement.UV) {
                this.supplyUv(vertexConsumer);
            } else if (element == VertexFormatElement.UV1) {
                this.supplyUv1(vertexConsumer);
            } else if (element == VertexFormatElement.UV2) {
                this.supplyUv2(vertexConsumer);
            } else if (element == VertexFormatElement.NORMAL) {
                this.supplyNormal(vertexConsumer, poseStack);
            }
        });
    }

    public void clearOverrides() {
        this.position.clearOverride();
        this.normal.clearOverride();
        this.uv.clearOverride();
        this.color.clearOverride();
        this.packedLight.clearOverride();
        this.packedOverlay.clearOverride();
    }

    public String toString() {
        return "Vertex{" +
                "position=" + position.get() +
                ", normal=" + normal.get() +
                ", uv=" + uv.get() +
                ", color=" + color.get() +
                ", packedLight=" + packedLight.get() +
                ", packedOverlay=" + packedOverlay.get() +
                '}';
    }
}
