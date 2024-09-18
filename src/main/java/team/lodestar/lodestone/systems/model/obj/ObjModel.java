package team.lodestar.lodestone.systems.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.model.obj.data.IndexedMesh;
import team.lodestar.lodestone.systems.model.obj.data.Vertex;
import team.lodestar.lodestone.systems.model.obj.modifier.ModelModifier;
import team.lodestar.lodestone.systems.model.obj.modifier.ModifierQueue;

import java.util.ArrayList;
import java.util.List;

public class ObjModel extends IndexedModel {
    public ObjModel(ResourceLocation modelId) {
        super(modelId);
    }

    @Override
    public void loadModel() {
        ObjParser.startParse(this);
        this.applyModifiers();
    }

    @Override
    public void render(PoseStack poseStack, RenderType renderType, MultiBufferSource.BufferSource bufferSource) {
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        for (IndexedMesh mesh : meshes) {
            if (mesh.isCompatibleWith(renderType.mode())) {
                for (Vertex vertex : mesh.getVertices(this)) {
                    vertex.supplyVertexData(vertexConsumer, renderType.format(), poseStack);
                }
            }
        }
    }

    public static class Builder implements ModifierQueue {
        private final ResourceLocation modelId;
        private VertexFormat.Mode bakeMode;
        private boolean convertQuadsToTriangles;
        private final boolean cacheModifications = false;
        private List<ModelModifier<?>> modifiers;

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

        public Builder withModifiers(ModifierQueueSetup setup) {
            this.modifiers = new ArrayList<>();
            setup.setup(this);
            return this;
        }

        @Override
        public void queueModifier(ModelModifier<?> modifier) {
            this.modifiers.add(modifier);
        }

        public ObjModel build() {
            ObjModel model = new ObjModel(modelId);
            model.modifiers = this.modifiers;
            return model;
        }

        public interface ModifierQueueSetup {
            void setup(ModifierQueue queue);
        }
    }
}