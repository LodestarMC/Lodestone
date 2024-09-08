package team.lodestar.lodestone.systems.model.objold;

import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.phys.Vec2;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ObjParser {
    public List<Vector3f> vertices = new ArrayList<>();
    public List<Vector3f> normals = new ArrayList<>();
    public List<Vec2> uvs = new ArrayList<>();
    public List<Face> faces = new ArrayList<>();

    public void parseObjFile(Resource resource) throws IOException {
        InputStream inputStream = resource.open();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("v ")) {
                String[] tokens = line.split(" ");
                float x = Float.parseFloat(tokens[1]);
                float y = Float.parseFloat(tokens[2]);
                float z = Float.parseFloat(tokens[3]);
                vertices.add(new Vector3f(x, y, z));

            } else if (line.startsWith("vn ")) {
                String[] tokens = line.split(" ");
                float x = Float.parseFloat(tokens[1]);
                float y = Float.parseFloat(tokens[2]);
                float z = Float.parseFloat(tokens[3]);
                normals.add(new Vector3f(x, y, z));

            } else if (line.startsWith("vt ")) {
                String[] tokens = line.split(" ");
                float u = Float.parseFloat(tokens[1]);
                float v = Float.parseFloat(tokens[2]);
                uvs.add(new Vec2(u, v));

            } else if (line.startsWith("f ")) {
                String[] tokens = line.split(" ");
                List<Vertex> faceVertices = new ArrayList<>();

                for (int i = 1; i < tokens.length; i++) {
                    String[] parts = tokens[i].split("/");
                    int vertexIndex = Integer.parseInt(parts[0]) - 1;
                    int textureIndex = parts.length > 1 && !parts[1].isEmpty() ? Integer.parseInt(parts[1]) - 1 : 0;
                    int normalIndex = parts.length > 2 ? Integer.parseInt(parts[2]) - 1 : 0;

                    Vector3f position = vertices.get(vertexIndex);
                    Vector3f normal = normals.get(normalIndex);
                    Vec2 uv = uvs.get(textureIndex);

                    Vertex vertex = new Vertex(position, normal, uv);
                    faceVertices.add(vertex);
                }

                Face face = new Face(faceVertices);
                faces.add(face);
            }
        }
        reader.close();
    }

    public ArrayList<Face> getFaces() {
        return (ArrayList<Face>) faces;
    }
}