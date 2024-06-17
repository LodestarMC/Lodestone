package team.lodestar.lodestone.systems.model.obj.lod;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.model.obj.ObjModel;
import team.lodestar.lodestone.systems.model.obj.lod.strategy.LODStrategy;

import java.util.ArrayList;
import java.util.List;

public class MultiLODModel extends ObjModel implements LevelOfDetailBuilder {
    private final List<LevelOfDetail> lodEntries = new ArrayList<>();
    private final LODStrategy lodStrategy;

    public MultiLODModel(LODStrategy lodStrategy, LevelOfDetailBuilderSetup setup) {
        super(null);
        this.lodStrategy = lodStrategy;
        setup.setup(this);
    }

    @Override
    public void create(float argument, ResourceLocation modelLocation) {
        if (this.lodStrategy.equals(LODStrategy.Distance)) argument *= argument;
        lodEntries.add(new LevelOfDetail(new ObjModel(modelLocation), argument));
    }

    @Override
    public void loadModel() {
        for (LevelOfDetail entry : lodEntries) {
            entry.getModel().loadModel();
        }
    }

    @Override
    public void renderModel(PoseStack poseStack, RenderType renderType, int packedLight) {
        Vector3f modelPosition = new Vector3f(0.0f, 0.0f, 0.0f);
        LevelOfDetail levelOfDetail = lodStrategy.getLODLevel(modelPosition, lodEntries);
        levelOfDetail.getModel().renderModel(poseStack, renderType, packedLight);
    }

    @FunctionalInterface
    public interface LevelOfDetailBuilderSetup {
        void setup(LevelOfDetailBuilder builder);
    }
}