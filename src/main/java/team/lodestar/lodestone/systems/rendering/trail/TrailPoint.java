package team.lodestar.lodestone.systems.rendering.trail;

import com.mojang.math.*;

public final class TrailPoint {
    private final Vector3f position;
    private int timeActive;

    public TrailPoint(Vector3f position, int timeActive) {
        this.position = position;
        this.timeActive = timeActive;
    }

    public TrailPoint(Vector3f position) {
        this(position, 0);
    }

    public Vector4f getMatrixPosition() {
        return new Vector4f(position);
    }

    public Vector3f getPosition() {
        return position;
    }

    public int getTimeActive() {
        return timeActive;
    }

    public void tick() {
        timeActive++;
    }
}
