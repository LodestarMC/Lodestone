package team.lodestar.lodestone.systems.model.obj.lod;

import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
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
            Vector3f cameraPosition = new Vector3f(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition());
            float distanceSq = ((modelPosition.x()*modelPosition.x())-(cameraPosition.x()*cameraPosition.x())) + ((modelPosition.y()*modelPosition.y())-(cameraPosition.y()*cameraPosition.y())) + ((modelPosition.z()*modelPosition.z())-(cameraPosition.z()*cameraPosition.z()));

            for (LevelOfDetail<Float> levelOfDetail : levelsOfDetail) {
                if (distanceSq <= levelOfDetail.argument()) {
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
            int fps = Minecraft.fps;
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