package team.lodestar.lodestone.systems.model.obj.data;

import com.mojang.blaze3d.vertex.VertexFormat;
import team.lodestar.lodestone.systems.model.obj.IndexedModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a mesh with indices to vertices.
 * <p>To get the vertices, use {@link #getVertices(IndexedModel)} or
 */
public class IndexedMesh {
    public List<Integer> indices;

    public IndexedMesh() {
        this.indices = new ArrayList<>();
    }

    public void addIndex(int index) {
        this.indices.add(index);
    }

    public List<Integer> getIndices() {
        return this.indices;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }

    public int getMeshSize() {
        return this.indices.size();
    }

    public List<Vertex> getVertices(IndexedModel model) {
        List<Vertex> vertices = new ArrayList<>();
        for (int index : this.indices) {
            vertices.add(getVertex(model, index));
        }
        return vertices;
    }

    public Vertex getVertex(IndexedModel model, int index) {
        return model.getVertices().get(index);
    }

    public boolean isCompatibleWith(VertexFormat.Mode mode) {
        return this.indices.size() == mode.primitiveLength;
    }

    public boolean isEdge() {
        return this.indices.size() == 2;
    }

    public boolean isTriangle() {
        return this.indices.size() == 3;
    }

    public boolean isQuad() {
        return this.indices.size() == 4;
    }

    public boolean isNgon() {
        return this.indices.size() > 4;
    }

}

