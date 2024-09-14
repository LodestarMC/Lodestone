package team.lodestar.lodestone.systems.model.obj.lod;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import team.lodestar.lodestone.systems.model.obj.ObjModel;

/**
 * A model that can switch between different levels of detail based on the model data.
 */
public class MultiLODModel extends ObjModel {
    private final LODStrategy<?> lodStrategy;

    public MultiLODModel(LODStrategy<?> lodStrategy) {
        super(null);
        this.lodStrategy = lodStrategy;
    }

    @Override
    public void loadModel() {
        for (LevelOfDetail<?> levelOfDetail : lodStrategy.levelsOfDetail) {
            levelOfDetail.loadModel();
        }
    }


    @Override
    public void render(PoseStack poseStack, RenderType renderType, MultiBufferSource.BufferSource bufferSource) {
        LevelOfDetail<?> levelOfDetail = this.lodStrategy.getLODLevel(poseStack);
        levelOfDetail.render(poseStack, renderType, bufferSource);
    }
}
