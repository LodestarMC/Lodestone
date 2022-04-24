package com.sammy.ortus.systems.screenshake;

import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;

public class PositionedScreenshakeInstance extends ScreenshakeInstance{
    public Vec3 position;
    public float falloffDistance;
    public float maxDistance;

    public PositionedScreenshakeInstance(Vec3 position, float falloffDistance, float maxDistance, float intensity, float falloffTransformSpeed, int timeBeforeFastFalloff, float slowFalloff, float fastFalloff) {
        super(intensity, falloffTransformSpeed, timeBeforeFastFalloff, slowFalloff, fastFalloff);
        this.position = position;
        this.falloffDistance = falloffDistance;
        this.maxDistance = maxDistance;
    }

    @Override
    public float updateIntensity(Camera camera, float falloff) {
        float intensity = super.updateIntensity(camera, falloff);
        float distance = (float) position.distanceTo(camera.getPosition());
        if (distance > maxDistance)
        {
            return 0;
        }
        float distanceMultiplier = 1;
        if (distance > falloffDistance)
        {
            float remaining = maxDistance-falloffDistance;
            float current = distance-falloffDistance;
            distanceMultiplier = 1-current/remaining;
        }
        Vector3f lookDirection = camera.getLookVector();
        Vec3 directionToScreenshake = position.subtract(camera.getPosition()).normalize();
        float angle = lookDirection.dot(new Vector3f(directionToScreenshake));
        if (angle < 0)
        {
            return 0;
        }
        return intensity * distanceMultiplier * angle;
    }
}
