package team.lodestar.lodestone.systems.model.obj.lod;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.RenderType;
import team.lodestar.lodestone.systems.model.obj.ObjModel;

public class MultiLODModel extends ObjModel {
    private final LODStrategy<?> lodStrategy;

    public MultiLODModel(LODStrategy<?> lodStrategy) {
        super(null);
        this.lodStrategy = lodStrategy;
    }

    @Override
    public void loadModel() {
        for (LevelOfDetail<?> levelOfDetail : lodStrategy.levelsOfDetail) {
            levelOfDetail.model().loadModel();
        }
    }

    @Override
    public void renderModel(PoseStack poseStack, RenderType renderType, int packedLight) {
        Vector3f modelPosition = new Vector3f(0.0f, 0.0f, 0.0f);
        LevelOfDetail<?> levelOfDetail = lodStrategy.getLODLevel(modelPosition);
        levelOfDetail.model().renderModel(poseStack, renderType, packedLight);
    }
}