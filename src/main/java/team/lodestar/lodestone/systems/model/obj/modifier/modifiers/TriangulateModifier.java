package team.lodestar.lodestone.systems.model.obj.modifier.modifiers;

import org.joml.Vector3f;
import team.lodestar.lodestone.systems.model.obj.IndexedModel;
import team.lodestar.lodestone.systems.model.obj.data.IndexedMesh;
import team.lodestar.lodestone.systems.model.obj.data.Vertex;
import team.lodestar.lodestone.systems.model.obj.modifier.ModelModifier;

import java.util.ArrayList;
import java.util.List;

public class TriangulateModifier extends ModelModifier<TriangulateSettings> {
    protected List<IndexedMesh> newMeshes;
    public TriangulateModifier(TriangulateSettings settings) {
        super(settings);
        this.newMeshes = new ArrayList<>();
    }

    public static TriangulateModifier of(TriangulateSettings.QuadMethod quadMethod, TriangulateSettings.NgonMethod ngonMethod) {
        return new TriangulateModifier(new TriangulateSettings(quadMethod, ngonMethod));

    }

    @Override
    public void apply(IndexedModel model) {
        //newMeshes.clear();

        for (IndexedMesh mesh : new ArrayList<>(model.getMeshes())) {
            apply(model, mesh);
        }

        model.getMeshes().addAll(newMeshes);
    }

    @Override
    public void apply(IndexedModel model, IndexedMesh mesh) {
        if (mesh.isTriangle()) return;

        if (mesh.isQuad()) {
            Quad quad = new Quad(this);
            quad.apply(model, mesh);
        }
        else if (mesh.isNgon()) {
            Ngon ngon = new Ngon(this);
            ngon.apply(model, mesh);
        }
    }

    public static class Quad extends TriangulateModifier {
        private final TriangulateModifier parent;
        public Quad(TriangulateModifier parent) {
            super(parent.settings);
            this.parent = parent;
        }

        @Override
        public void apply(IndexedModel model, IndexedMesh mesh) {
            TriangulateSettings.QuadMethod method = this.settings.quadMethod;

            List<Vertex> vertices = mesh.getVertices(model);
            Vertex v0 = vertices.get(0);
            Vertex v1 = vertices.get(1);
            Vertex v2 = vertices.get(2);
            Vertex v3 = vertices.get(3);

            float d1 = this.getDistance(v0, v2);
            float d2 = this.getDistance(v1, v3);

            boolean f = (this.settings.quadMethod == TriangulateSettings.QuadMethod.ShortestDiagonal) ? d1 > d2 : d1 < d2;

            if (f) {
                this.splitMesh(model, mesh, 0, 1, 3, 1, 2, 3);
            } else {
                this.splitMesh(model, mesh, 0, 2, 3, 0, 1, 2);
            }
        }

        /**
         * Splits the mesh into two triangles, preserving the original IndexedMesh by modification, then creating a new IndexedMesh and adding it to the ObjModel.
         */
        private void splitMesh(IndexedModel model, IndexedMesh mesh, int idx1, int idx2, int idx3, int newIdx1, int newIdx2, int newIdx3) {
            List<Integer> fourIndices = List.copyOf(mesh.getIndices());

            IndexedMesh mesh1 = new IndexedMesh();
            IndexedMesh mesh2 = new IndexedMesh();

            mesh1.addIndex(fourIndices.get(idx1));
            mesh1.addIndex(fourIndices.get(idx2));
            mesh1.addIndex(fourIndices.get(idx3));

            mesh2.addIndex(fourIndices.get(newIdx1));
            mesh2.addIndex(fourIndices.get(newIdx2));
            mesh2.addIndex(fourIndices.get(newIdx3));

            model.getMeshes().remove(mesh);
            this.parent.newMeshes.add(mesh1);
            this.parent.newMeshes.add(mesh2);
        }

        private float getDistance(Vertex v1, Vertex v2) {
            Vector3f pos1 = v1.getPosition();
            Vector3f pos2 = v2.getPosition();
            return pos1.distance(pos2);
        }
    }

    public static class Ngon extends TriangulateModifier {
        private final TriangulateModifier parent;
        public Ngon(TriangulateModifier parent) {
            super(parent.settings);
            this.parent = parent;
        }

        @Override
        public void apply(IndexedModel model, IndexedMesh mesh) {
            TriangulateSettings.NgonMethod method = this.settings.ngonMethod;
            //TODO: Implement Ngon triangulation
        }
    }
}
