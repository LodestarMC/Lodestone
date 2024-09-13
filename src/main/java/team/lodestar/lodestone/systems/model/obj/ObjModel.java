package team.lodestar.lodestone.systems.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.systems.model.obj.data.Vertex;

public class ObjModel extends IndexedModel {
    public ObjModel(ResourceLocation modelId) {
        super(modelId);
    }

    @Override
    public void loadModel() {
        ObjParser.startParse(this);
    }

    @Override
    public void render(PoseStack poseStack, RenderType renderType, MultiBufferSource.BufferSource bufferSource) {
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        this.meshes.forEach(mesh -> {
            mesh.indices.forEach(index -> {
                Vertex vertex = vertices.get(index);
                vertex.supplyVertexData(vertexConsumer, renderType.format(), poseStack);
            });
        });
    }

    public static class Builder {
        private final ResourceLocation modelId;
        private VertexFormat.Mode bakeMode;
        private boolean convertQuadsToTriangles;
        private final boolean cacheModifications = false;

        private Builder(ResourceLocation modelId) {
            this.modelId = modelId;
        }

        public static Builder of(ResourceLocation modelId) {
            return new Builder(modelId);
        }
        public Builder bakeIndicies(VertexFormat.Mode primitiveMode, boolean convertQuadsToTriangles) {
            this.bakeMode = primitiveMode;
            return this;
        }

        public ObjModel build() {
            ObjModel model = new ObjModel(modelId);
            return model;
        }

        public Runnable buildPostLoadOperations(ObjModel model) {
            return () -> {
                if (bakeMode != null) model.bakeIndices(bakeMode, convertQuadsToTriangles);
            };
        }


    }
}