package team.lodestar.lodestone.systems.rendering.trail;

import com.mojang.math.*;
import net.minecraft.world.phys.*;

public final class TrailPoint {
    private final Vec3 position;
    private int timeActive;

    public TrailPoint(Vec3 position, int timeActive) {
        this.position = position;
        this.timeActive = timeActive;
    }

    public TrailPoint(Vec3 position) {
        this(position, 0);
    }

    public Vector4f getMatrixPosition() {
        return new Vector4f((float)position.x, (float)position.y, (float)position.z, 1.0f);
    }

    public Vec3 getPosition() {
        return position;
    }

    public int getTimeActive() {
        return timeActive;
    }

    public TrailPoint lerp(TrailPoint trailPoint, float delta) {
        return new TrailPoint(position.lerp(trailPoint.position, delta), timeActive);
    }

    public void tick() {
        timeActive++;
    }
}
