package team.lodestar.lodestone.systems.model.objold.lod;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.model.objold.ObjModel;

public class MultiLODModel extends ObjModel {
    private final LODStrategy<?> lodStrategy;

    public MultiLODModel(LODStrategy<?> lodStrategy) {
        super(null);
        this.lodStrategy = lodStrategy;
    }

    @Override
    public void loadModel() {
        for (LevelOfDetail<?> levelOfDetail : lodStrategy.levelsOfDetail) {
            levelOfDetail.getModel().loadModel();
        }
    }

    @Override
    public void renderModel(PoseStack poseStack, RenderType renderType, int packedLight) {
        Vector3f modelPosition = new Vector3f(0.0f, 0.0f, 0.0f);
        LevelOfDetail<?> levelOfDetail = lodStrategy.getLODLevel(modelPosition);
        levelOfDetail.getModel().renderModel(poseStack, renderType, packedLight);
    }
}