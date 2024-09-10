package team.lodestar.lodestone.systems.model.obj.data;

import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.world.phys.Vec2;
import org.joml.Vector2f;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.model.obj.ObjParser;
import team.lodestar.lodestone.systems.model.obj.VertexException;
import team.lodestar.lodestone.systems.model.obj.data.IndexedVertex;
import team.lodestar.lodestone.systems.model.obj.data.VertexData;

import java.util.List;

public class Vertex extends VertexData {

    public Vertex(Vector3f position, Vector3f normal, Vector2f uv) {
        this.putBulkDefault(position, normal, uv);
    }

    public Vertex(IndexedVertex indexedVertex, ObjParser.Builder builder) {
        Vector3f position = builder.positions.get(indexedVertex.positionIndex());
        Vector3f normal = builder.normals.get(indexedVertex.normalIndex());
        Vector2f uv = builder.uvs.get(indexedVertex.uvIndex());
        this.putBulkDefault(position, normal, uv);
    }

    public Vector3f getPosition() {
        List<Float> position = this.getOrDefaultAttribute(VertexFormatElement.POSITION);
        return new Vector3f(position.get(0), position.get(1), position.get(2));
    }

    public Vector3f getNormal() {
        List<Float> normal = this.getOrDefaultAttribute(VertexFormatElement.NORMAL);
        return new Vector3f(normal.get(0), normal.get(1), normal.get(2));
    }

    public Vec2 getUv() {
        List<Float> uv = this.getOrDefaultAttribute(VertexFormatElement.UV);
        return new Vec2(uv.get(0), uv.get(1));
    }

    public List<Float> getAttribute(VertexFormatElement element) throws VertexException {
        return this.get(element);
    }

    public void putVertexAttribute(VertexFormatElement element, Float... data) {
        this.put(element, data);
    }

    public void clearElements() {
        this.clear();
    }

    public void test() {
        this.putVertexAttribute(VertexFormatElement.NORMAL, 1.0f, 0.0f, 1.0f);
    }

}