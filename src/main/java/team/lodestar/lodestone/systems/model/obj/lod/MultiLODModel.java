package team.lodestar.lodestone.systems.model.obj.lod;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import team.lodestar.lodestone.systems.model.obj.ObjModel;

/**
 * A model that can switch between different levels of detail based on the model data.
 * @param <P> The type of model data that determines the level of detail on a per model basis.
 */
public class MultiLODModel<P> extends ObjModel {
    private final LODStrategy<?,P> lodStrategy;
    private P currentModelData;
    public MultiLODModel(LODStrategy<?,P> lodStrategy) {
        super(null);
        this.lodStrategy = lodStrategy;
    }

    @Override
    public void loadModel() {
        for (LevelOfDetail<?> levelOfDetail : lodStrategy.levelsOfDetail) {
            levelOfDetail.loadModel();
        }
    }

    /**
     * Prepares the model for rendering by supplying the model data to the LODStrategy.
     * <p>Call this method before rendering the model with {@link #render(PoseStack, RenderType, MultiBufferSource.BufferSource)}.</p>
     * @param modelData The model specific data to determine the level of detail.
     */
    public void prepareModel(P modelData) {
        LevelOfDetail<?> levelOfDetail = lodStrategy.getLODLevel(modelData);
        if (levelOfDetail == null) {
            // TODO: Dont throw an exception just default to an LOD
            throw new IllegalArgumentException("No level of detail found for model data: " + modelData);
        }
        currentModelData = modelData;
    }
    @Override
    public void render(PoseStack poseStack, RenderType renderType, MultiBufferSource.BufferSource bufferSource) {
        LevelOfDetail<?> levelOfDetail = lodStrategy.getLODLevel(currentModelData);
        levelOfDetail.render(poseStack, renderType, bufferSource);
        currentModelData = null;
    }
}
