package team.lodestar.lodestone.systems.rendering.trail;

import com.mojang.math.*;
import net.minecraft.core.*;
import net.minecraft.world.phys.*;

import java.util.*;
import java.util.stream.*;

public class TrailPointBuilder {

    private final List<TrailPoint> trailPoints = new ArrayList<>();
    public final int trailLength;

    public TrailPointBuilder(int trailLength) {
        this.trailLength = trailLength;
    }

    public static TrailPointBuilder create(int trailLength) {
        return new TrailPointBuilder(trailLength);
    }

    public List<TrailPoint> getTrailPoints() {
        return trailPoints;
    }

    public TrailPointBuilder addTrailPoint(Vector3f point) {
        trailPoints.add(new TrailPoint(point, 0));
        return this;
    }

    public TrailPointBuilder addTrailPoint(Vec3 point) {
        return addTrailPoint(new Vector3f(point));
    }

    public TrailPointBuilder tickTrailPoints() {
        trailPoints.forEach(TrailPoint::tick);
        trailPoints.removeIf(p -> p.getTimeActive() > trailLength);
        return this;
    }

    public List<Vector4f> build() {
        return trailPoints.stream().map(TrailPoint::getMatrixPosition).collect(Collectors.toList());
    }
}
