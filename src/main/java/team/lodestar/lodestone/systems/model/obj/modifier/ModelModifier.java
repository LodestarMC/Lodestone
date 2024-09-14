package team.lodestar.lodestone.systems.model.obj.modifier;

import team.lodestar.lodestone.systems.model.obj.IndexedModel;
import team.lodestar.lodestone.systems.model.obj.data.IndexedMesh;

public abstract class ModelModifier<T extends ModiferSettings> {

    public T settings;

    public ModelModifier(T settings) {
        this.settings = settings;
    }
    /**
     * Apply the modification to the whole {@link IndexedModel}.
     * @param model The model to apply the modification to.
     */
    public abstract void apply(IndexedModel model);

    /**
     * Apply the modification to a specific {@link IndexedMesh} in an {@link IndexedModel}.
     * @param model The model that contains the mesh.
     * @param mesh The mesh to apply the modification to.
     */
    public abstract void apply(IndexedModel model, IndexedMesh mesh);
}
