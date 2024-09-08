package team.lodestar.lodestone.systems.model.obj;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.Resource;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ObjParser {
    private static final String VERTEX_POSITION = "v ";
    private static final String VERTEX_NORMAL = "vn ";
    private static final String VERTEX_UV = "vt ";
    private static final String FACE = "f ";

    public static void startParse(ObjModel model) {
        Optional<Resource> resource = Minecraft.getInstance().getResourceManager().getResource(model.getAssetLocation());
        if (resource.isEmpty()) throw new RuntimeException("Lodestone Model" + model.getModelId() + " not found at " + model.getAssetLocation());
        try {
            InputStream inputStream = resource.get().open();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            parse(reader, model);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Model file: " + model.getModelId(), e);
        }
    }

    protected static void parse(BufferedReader reader, ObjModel model) throws IOException {
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
                if (indexedVertices.contains(indexedVertex)) {
                    int index = indexedVertices.indexOf(indexedVertex);
                    mesh.addIndex(index);
                } else {
                    indexedVertices.add(indexedVertex);
                    mesh.addIndex(indexedVertices.size() - 1);
                }
            }
            return mesh;
        }

        private IndexedVertex parseIndexedVertex(String line) {
            String[] tokens = line.split("/");
            int vertexIndex = Integer.parseInt(tokens[1]);
            int normalIndex = Integer.parseInt(tokens[2]);
            int textureIndex = Integer.parseInt(tokens[3]);
            // Check to see if the vertex already exists, if it does then fetch it instead of creating a new one
            return getOrCreateIndexedVertex(vertexIndex, normalIndex, textureIndex);
        }

        public void addPosition(Vector3f position) {
            positions.add(position);
        }

        public void addNormal(Vector3f normal) {
            normals.add(normal);
        }

        public void addUv(Vector2f uv) {
            uvs.add(uv);
        }

        public void addIndexedVertex(IndexedVertex indexedVertex) {
            indexedVertices.add(indexedVertex);
        }

        public void addMesh(IndexedMesh mesh) {
            meshes.add(mesh);
        }

        public boolean containsVertex(IndexedVertex indexedVertex) {
            return indexedVertices.contains(indexedVertex);
        }

        public int getIndexOfVertex(IndexedVertex indexedVertex) {
            return indexedVertices.indexOf(indexedVertex);
        }

        public int addVertexAndReturnIndex(IndexedVertex indexedVertex) {
            indexedVertices.add(indexedVertex);
            return indexedVertices.size() - 1;
        }

        public IndexedVertex getOrCreateIndexedVertex(int vertexIndex, int normalIndex, int textureIndex) {
            IndexedVertex vertex = new IndexedVertex(vertexIndex, normalIndex, textureIndex);
            if (containsVertex(vertex)) {
                int index = getIndexOfVertex(vertex);
                return indexedVertices.get(index);
            }
            return vertex;
        }

        public void build() {
            List<Vertex> vertices = new ArrayList<>();
            this.indexedVertices.forEach(indexedVertex -> vertices.add(new Vertex(indexedVertex, this)));
            model.vertices = vertices;
            model.meshes = meshes;
        }
    }
}
