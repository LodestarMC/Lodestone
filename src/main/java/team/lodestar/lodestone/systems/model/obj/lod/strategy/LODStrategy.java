package team.lodestar.lodestone.systems.model.obj.lod.strategy;

import net.minecraft.client.Minecraft;
import org.joml.Vector3f;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.model.obj.lod.LevelOfDetail;

import java.util.List;

public interface LODStrategy {
    LODStrategy Distance = new DistanceLODStrategy();
    LODStrategy DynamicPerformance = new PerformanceLODStrategy();

    LevelOfDetail getLODLevel(Vector3f modelPosition, List<LevelOfDetail> levelsOfDetail);

    class DistanceLODStrategy implements LODStrategy {
        @Override
        public LevelOfDetail getLODLevel(Vector3f modelPosition, List<LevelOfDetail> levelsOfDetail) {
            Vector3f cameraPosition = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().toVector3f();
            float distanceSq = cameraPosition.distanceSquared(modelPosition);

            for (LevelOfDetail levelOfDetail : levelsOfDetail) {
                if (distanceSq <= levelOfDetail.getArgument()) {
                    return levelOfDetail;
                }
            }
            return levelsOfDetail.get(levelsOfDetail.size() - 1);
        }
    }

    class PerformanceLODStrategy implements LODStrategy {
        @Override
        public LevelOfDetail getLODLevel(Vector3f modelPosition, List<LevelOfDetail> levelsOfDetail) {
            int fps = Minecraft.getInstance().getFps();
            int monitorRefreshRate = Minecraft.getInstance().getWindow().getRefreshRate();
            int optionsRefreshRate = Minecraft.getInstance().options.framerateLimit().get();
            int steps = levelsOfDetail.size();
            int step = Math.min(steps - 1, (int) Math.floor((double) fps / monitorRefreshRate * steps));
            return levelsOfDetail.get(step);
        }
    }

}
