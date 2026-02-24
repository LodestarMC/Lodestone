package team.lodestar.lodestone.systems.model.geo.data;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.joml.Vector3f;

public class GeoCube implements IRenderableModelPart<GeoCube> {
    private final Vector3f origin;
    private final Vector3f size;
    private final Vector3f rotation;
    private final GeoQuad[] quads;

    public GeoCube(GeoQuad[] quads, Vector3f origin, Vector3f size, Vector3f rotation) {
        this.origin = new Vector3f(origin);
        this.size   = new Vector3f(size);
        this.rotation = new Vector3f(rotation);
        this.quads  = quads;
    }

    @Override
    public void render(PoseStack poseStack, VertexConsumer vertexConsumer, VertexFormat vertexFormat, VertexFormat.Mode mode) {
        for (GeoQuad quad : quads) {
            if (quad != null) {
                quad.render(poseStack, vertexConsumer, vertexFormat, mode);
            }
        }
    }

    public Vector3f getOrigin() {
        return origin;
    }

    public Vector3f getSize() {
        return size;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public GeoQuad[] getQuads() {
        return quads;
    }

    /**
     * Creates a complete copy of this GeoCube.
     * @return The copied GeoCube.
     */
    @Override
    public GeoCube copy() {
        GeoQuad[] copiedQuads = new GeoQuad[this.quads.length];
        for (int i = 0; i < this.quads.length; i++) {
            copiedQuads[i] = this.quads[i].copy();
        }
        return new GeoCube(copiedQuads, new Vector3f(this.origin), new Vector3f(this.size), new Vector3f(this.rotation));
    }
}
