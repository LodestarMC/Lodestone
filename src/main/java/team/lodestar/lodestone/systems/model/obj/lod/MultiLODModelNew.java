package team.lodestar.lodestone.systems.model.obj.lod;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import team.lodestar.lodestone.systems.model.obj.ObjModel;

public class MultiLODModelNew<P> extends ObjModel {
    private final LODStrategy<?,P> lodStrategy;
    private P currentModelData;
    public MultiLODModelNew(LODStrategy<?,P> lodStrategy) {
        super(null);
        this.lodStrategy = lodStrategy;
    }

    @Override
    public void loadModel() {
        for (LevelOfDetail<?> levelOfDetail : lodStrategy.levelsOfDetail) {
            levelOfDetail.getModel().loadModel();
        }
    }

    /**
     * Prepares the model for rendering by supplying the model data to the LODStrategy.
     * <p>Call this method before rendering the model with {@link #render(PoseStack, RenderType, MultiBufferSource.BufferSource)}.</p>
     * @param modelData The model specific data to determine the level of detail.
     */
    public void prepareModel(P modelData) {
        LevelOfDetail<?> levelOfDetail = lodStrategy.getLODLevel(modelData);
        if (levelOfDetail != null) currentModelData = modelData;
    }
    @Override
    public void render(PoseStack poseStack, RenderType renderType, MultiBufferSource.BufferSource bufferSource) {
        LevelOfDetail<?> levelOfDetail = lodStrategy.getLODLevel(currentModelData);
        levelOfDetail.render(poseStack, renderType, bufferSource);
        currentModelData = null;
    }
}
