package team.lodestar.lodestone.systems.model.obj;

import org.joml.Vector2f;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.model.LodestoneParser;
import team.lodestar.lodestone.systems.model.obj.data.IndexedMesh;
import team.lodestar.lodestone.systems.model.obj.data.IndexedVertex;
import team.lodestar.lodestone.systems.model.obj.data.Vertex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ObjParser extends LodestoneParser<ObjModel> {
    private static final String VERTEX_POSITION = "v ";
    private static final String VERTEX_NORMAL = "vn ";
    private static final String VERTEX_UV = "vt ";
    private static final String FACE = "f ";

    @Override
    public void parse(InputStream inputStream, ObjModel model) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Builder builder = new Builder(model);

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(VERTEX_POSITION)) {
                builder.addPosition(builder.parseVector3f(line));
            } else if (line.startsWith(VERTEX_NORMAL)) {
                builder.addNormal(builder.parseVector3f(line));
            } else if (line.startsWith(VERTEX_UV)) {
                builder.addUv(builder.parseVector2f(line));
            } else if (line.startsWith(FACE)) {
                IndexedMesh mesh = builder.parseMesh(line);
                builder.addMesh(mesh);
            }
        }
        builder.build();
        reader.close();
    }

    public static class Builder {
        public List<Vector3f> positions = new ArrayList<>();
        public List<Vector3f> normals = new ArrayList<>();
        public List<Vector2f> uvs = new ArrayList<>();

        private List<IndexedVertex> indexedVertices = new ArrayList<>();
        private List<IndexedMesh> meshes = new ArrayList<>();

        private ObjModel model;

        public Builder(ObjModel model) {
            this.model = model;
        }

        private Vector3f parseVector3f(String line) {
            String[] tokens = line.split(" ");
            float x = Float.parseFloat(tokens[1]);
            float y = Float.parseFloat(tokens[2]);
            float z = Float.parseFloat(tokens[3]);
            return new Vector3f(x, y, z);
        }

        private Vector2f parseVector2f(String line) {
            String[] tokens = line.split(" ");
            float u = Float.parseFloat(tokens[1]);
            float v = Float.parseFloat(tokens[2]);
            return new Vector2f(u, v);
        }

        private IndexedMesh parseMesh(String line) {
            String[] tokens = line.split(" ");
            IndexedMesh mesh = new IndexedMesh();
            for (int i = 1; i < tokens.length; i++) {
                IndexedVertex indexedVertex = parseIndexedVertex(tokens[i]);
                if (this.indexedVertices.contains(indexedVertex)) {
                    int index = this.indexedVertices.indexOf(indexedVertex);
                    mesh.addIndex(index);
                } else {
                    this.indexedVertices.add(indexedVertex);
                    mesh.addIndex(this.indexedVertices.size() - 1);
                }
            }
            return mesh;
        }

        private IndexedVertex parseIndexedVertex(String line) {
            String[] tokens = line.split("/");
            int vertexIndex = Integer.parseInt(tokens[0]) - 1;
            int textureIndex = tokens.length > 1 && !tokens[1].isEmpty() ? Integer.parseInt(tokens[1]) - 1 : -1;
            int normalIndex = tokens.length > 2 && !tokens[2].isEmpty() ? Integer.parseInt(tokens[2]) - 1 : -1;
            return this.getOrCreateIndexedVertex(vertexIndex, normalIndex, textureIndex);
        }

        public void addPosition(Vector3f position) {
            this.positions.add(position);
        }

        public void addNormal(Vector3f normal) {
            this.normals.add(normal);
        }

        public void addUv(Vector2f uv) {
            this.uvs.add(uv);
        }

        public void addIndexedVertex(IndexedVertex indexedVertex) {
            this.indexedVertices.add(indexedVertex);
        }

        public void addMesh(IndexedMesh mesh) {
            this.meshes.add(mesh);
        }

        public boolean containsVertex(IndexedVertex indexedVertex) {
            return this.indexedVertices.contains(indexedVertex);
        }

        public int getIndexOfVertex(IndexedVertex indexedVertex) {
            return this.indexedVertices.indexOf(indexedVertex);
        }

        public int addVertexAndReturnIndex(IndexedVertex indexedVertex) {
            this.indexedVertices.add(indexedVertex);
            return this.indexedVertices.size() - 1;
        }

        public IndexedVertex getOrCreateIndexedVertex(int vertexIndex, int normalIndex, int textureIndex) {
            IndexedVertex vertex = new IndexedVertex(vertexIndex, normalIndex, textureIndex);
            if (containsVertex(vertex)) {
                int index = getIndexOfVertex(vertex);
                return this.indexedVertices.get(index);
            }
            return vertex;
        }

        public void build() {
            this.indexedVertices.forEach(indexedVertex -> this.model.vertices.add(new Vertex(indexedVertex, this)));
            this.model.meshes = meshes;
        }
    }
}
