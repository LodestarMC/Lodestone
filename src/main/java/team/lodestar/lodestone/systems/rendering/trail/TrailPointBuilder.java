package team.lodestar.lodestone.systems.rendering.trail;

import com.mojang.math.*;
import net.minecraft.core.*;
import net.minecraft.world.phys.*;
import org.joml.Vector4f;
import team.lodestar.lodestone.helpers.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class TrailPointBuilder {

    private final List<TrailPoint> trailPoints = new ArrayList<>();
    public final Supplier<Integer> trailLength;

    public TrailPointBuilder(Supplier<Integer> trailLength) {
        this.trailLength = trailLength;
    }

    public static TrailPointBuilder create(int trailLength) {
        return create(()-> trailLength);
    }

    public static TrailPointBuilder create(Supplier<Integer> trailLength) {
        return new TrailPointBuilder(trailLength);
    }

    public List<TrailPoint> getTrailPoints() {
        return trailPoints;
    }

    public List<TrailPoint> getTrailPoints(float lerp) {
        List<TrailPoint> lerpedTrailPoints = new ArrayList<>();
        final int size = trailPoints.size();
        if (size > 1) {
            for (int i = 0; i < size - 2; i++) {
                lerpedTrailPoints.add(trailPoints.get(i).lerp(trailPoints.get(i +1), lerp));
            }
        }
        return lerpedTrailPoints;
    }

    public TrailPointBuilder addTrailPoint(Vec3 point) {
        trailPoints.add(new TrailPoint(point, 0));
        return this;
    }

    public TrailPointBuilder tickTrailPoints() {
        int trailLength = this.trailLength.get();
        trailPoints.forEach(TrailPoint::tick);
        trailPoints.removeIf(p -> p.getTimeActive() > trailLength);
        return this;
    }

    public List<Vector4f> build() {
        return trailPoints.stream().map(TrailPoint::getMatrixPosition).collect(Collectors.toList());
    }
}
