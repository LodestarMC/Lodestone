package team.lodestar.lodestone.systems.model.obj;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.systems.model.obj.modifier.ModelModifier;
import team.lodestar.lodestone.systems.model.obj.modifier.ModifierQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * An {@link IndexedModel} that is parsed from an Wavefront OBJ file.
 */
public class ObjModel extends IndexedModel {
    public ObjModel(ResourceLocation modelId) {
        super(modelId);
    }

    @Override
    public void loadModel() {
        ObjParser parser = new ObjParser();
        parser.startParse(this);
        this.applyModifiers();
    }

    public static class Builder implements ModifierQueue {
        private final ResourceLocation modelId;
        private VertexFormat.Mode bakeMode;
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

        public Builder withModifiers(Consumer<ModifierQueue> modifierQueue) {
            this.modifiers = new ArrayList<>();
            modifierQueue.accept(this);
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

    }
}