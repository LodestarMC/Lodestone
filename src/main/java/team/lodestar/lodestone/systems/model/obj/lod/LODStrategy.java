package team.lodestar.lodestone.systems.model.obj.lod;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.model.obj.ObjModel;

import java.util.ArrayList;
import java.util.List;

public abstract class LODStrategy<T> implements LODBuilder<T> {
    public static LODStrategy<Float> Distance(LODBuilderSetup<Float> lodBuilderSetup) {
        return new DistanceLODStrategy(lodBuilderSetup);
    }

    public static LODStrategy<Integer> Performance(LODBuilderSetup<Integer> lodBuilderSetup) {
        return new PerformanceLODStrategy(lodBuilderSetup);
    }

    public List<LevelOfDetail<T>> levelsOfDetail = new ArrayList<>();

    public abstract LevelOfDetail<T> getLODLevel(Vector3f modelPosition);

    public LODStrategy(LODBuilderSetup<T> lodBuilderSetup) {
        lodBuilderSetup.lodBuilder(this);
    }


    public interface LODBuilderSetup<T> {
        void lodBuilder(LODBuilder<T> builder);
    }

    private static class DistanceLODStrategy extends LODStrategy<Float> {
        public DistanceLODStrategy(LODBuilderSetup<Float> lodBuilderSetup) {
            super(lodBuilderSetup);
        }

        @Override
        public LevelOfDetail<Float> getLODLevel(Vector3f modelPosition) {
            Vector3f cameraPosition = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().toVector3f();
            float distanceSq = cameraPosition.distanceSquared(modelPosition);

            for (LevelOfDetail<Float> levelOfDetail : levelsOfDetail) {
                if (distanceSq <= levelOfDetail.getArgument()) {
                    return levelOfDetail;
                }
            }
            return levelsOfDetail.get(levelsOfDetail.size() - 1);
        }

        @Override
        public void create(Float argument, ResourceLocation modelLocation) {
            levelsOfDetail.add(new LevelOfDetail<>(new ObjModel(modelLocation), argument));
        }
    }

    private static class PerformanceLODStrategy extends LODStrategy<Integer> {
        public PerformanceLODStrategy(LODBuilderSetup<Integer> lodBuilderSetup) {
            super(lodBuilderSetup);
        }

        @Override
        public LevelOfDetail<Integer> getLODLevel(Vector3f modelPosition) {
            int fps = Minecraft.getInstance().getFps();
            int monitorRefreshRate = Minecraft.getInstance().getWindow().getRefreshRate();
            int optionsRefreshRate = Minecraft.getInstance().options.framerateLimit().get();
            int steps = levelsOfDetail.size();
            int step = Math.min(steps - 1, (int) Math.floor((double) fps / monitorRefreshRate * steps));
            return levelsOfDetail.get(step);
        }

        @Override
        public void create(Integer argument, ResourceLocation modelLocation) {
            levelsOfDetail.add(new LevelOfDetail<>(new ObjModel(modelLocation), argument));
        }
    }
}