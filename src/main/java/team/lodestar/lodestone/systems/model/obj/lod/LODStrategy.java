package team.lodestar.lodestone.systems.model.obj.lod;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.model.obj.ObjModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * An LODStrategy or Level of Detail Strategy is a class containing the logic for determining which level of detail to use for a given {@link ObjModel}.
 * <p>The LODStrategy is what stores the LODs and determines which LOD to use based on the model's P data.</p>
 *
 * @param <T> The type of argument that the LODStrategy uses to determine the level of detail.
 */
public abstract class LODStrategy<T> implements LODBuilder<T> {

    public List<LevelOfDetail<T>> levelsOfDetail = new ArrayList<>();

    public LODStrategy(Consumer<LODBuilder<T>> lodBuilderSetup) {
        lodBuilderSetup.accept(this);
    }

    public abstract LevelOfDetail<T> getLODLevel(PoseStack poseStack);

    public static LODStrategy<Float> Distance(Consumer<LODBuilder<Float>> lodBuilder) {
        return new LODStrategy.DistanceLODStrategy(lodBuilder);
    }

    public static LODStrategy<GraphicsStatus> Graphics(Consumer<LODBuilder<GraphicsStatus>> lodBuilder) {
        return new LODStrategy.GraphicsSettingsLODStrategy(lodBuilder);
    }


    /**
     * A LODStrategy that uses the distance from the camera to the model to determine the level of detail.
     * <p>For example:</p>
     * <p>LOD1 - 0-10 blocks</p>
     * <p>LOD2 - 10-20 blocks</p>
     * <p>LOD3 - 20-30 blocks</p>
     * <p>LOD4 - 30+ blocks</p>
     */
    private static class DistanceLODStrategy extends LODStrategy<Float> {
        public DistanceLODStrategy(Consumer<LODBuilder<Float>> lodBuilder) {
            super(lodBuilder);
        }

        @Override
        public LevelOfDetail<Float> getLODLevel(PoseStack poseStack) {
            Vector3f cameraPosition = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().toVector3f();
            float distanceSq = cameraPosition.distanceSquared(this.getPositionFromPose(poseStack));

            for (LevelOfDetail<Float> levelOfDetail : levelsOfDetail) {
                if (distanceSq <= levelOfDetail.getArgument()) {
                    return levelOfDetail;
                }
            }
            return levelsOfDetail.get(levelsOfDetail.size() - 1);
        }

        @Override
        public void create(Float argument, ResourceLocation modelLocation) {
            levelsOfDetail.add(new LevelOfDetail<>(modelLocation, argument*argument));
        }

        private Vector3f getPositionFromPose(PoseStack poseStack) {
            Matrix4f pose = poseStack.last().pose();
            return new Vector3f(pose.m30(), pose.m31(), pose.m32());
        }
    }

    /**
     * A LODStrategy that uses the graphics settings to determine the level of detail.
     * <p>For Example:</p>
     * <p>Fast - LOD1</p>
     * <p>Fancy - LOD2</p>
     * <p>Fabulous - LOD3</p>
     */
    private static class GraphicsSettingsLODStrategy extends LODStrategy<GraphicsStatus> {
        public GraphicsSettingsLODStrategy(Consumer<LODBuilder<GraphicsStatus>> lodBuilder) {
            super(lodBuilder);
        }

        @Override
        public LevelOfDetail<GraphicsStatus> getLODLevel(PoseStack poseStack) {
            GraphicsStatus graphicsQuality = Minecraft.getInstance().options.graphicsMode().get();
            for (LevelOfDetail<GraphicsStatus> levelOfDetail : levelsOfDetail) {
                if (graphicsQuality == levelOfDetail.getArgument()) {
                    return levelOfDetail;
                }
            }
            return null;
        }

        @Override
        public void create(GraphicsStatus argument, ResourceLocation modelLocation) {
            if (checkForDuplicates(argument)) {
                throw new IllegalArgumentException("Duplicate graphics status found: " + argument);
            }
            levelsOfDetail.add(new LevelOfDetail<>(modelLocation, argument));
        }

        private boolean checkForDuplicates(GraphicsStatus argument) {
            for (LevelOfDetail<GraphicsStatus> levelOfDetail : levelsOfDetail) {
                if (levelOfDetail.getArgument().equals(argument)) {
                    return true;
                }
            }
            return false;
        }
    }
}