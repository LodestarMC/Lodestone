package team.lodestar.lodestone.systems.model.obj.data;

import team.lodestar.lodestone.systems.model.obj.ObjModel;

import java.util.ArrayList;
import java.util.List;

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

    public List<Vertex> getVertices(ObjModel objModel) {
        List<Vertex> vertices = new ArrayList<>();
        this.indices.forEach(index -> vertices.add(objModel.getVertices().get(index)));
        for (int index : this.indices) {
            vertices.add(objModel.getVertices().get(index));
        }
        return vertices;
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

