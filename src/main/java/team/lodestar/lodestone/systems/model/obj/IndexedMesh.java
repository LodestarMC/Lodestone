package team.lodestar.lodestone.systems.model.obj;

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
        for (int index : this.indices) {
            vertices.add(objModel.vertices.get(index));
        }
        return vertices;
    }

    public void triangulate(ObjModel objModel) {
        List<IndexedMesh> meshes = objModel.meshes;

    }
}

