package team.lodestar.lodestone.systems.screenshake;

import net.minecraft.client.Camera;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.easing.Easing;

public class PositionedScreenshakeInstance extends ScreenshakeInstance {
    public final Vec3 position;
    public final float falloffDistance;
    public final float maxDistance;
    public final Easing falloffEasing;

    //TODO: make falloff affect duration; the further away a player is the less the screenshake lasts
    public PositionedScreenshakeInstance(int duration, Vec3 position, float falloffDistance, float maxDistance, Easing falloffEasing) {
        super(duration);
        this.position = position;
        this.falloffDistance = falloffDistance;
        this.maxDistance = maxDistance;
        this.falloffEasing = falloffEasing;
    }

    public PositionedScreenshakeInstance(int duration, Vec3 position, float falloffDistance, float maxDistance) {
        this(duration, position, falloffDistance, maxDistance, Easing.LINEAR);
    }

    @Override
    public float updateIntensity(Camera camera, RandomSource random) {
        float intensity = super.updateIntensity(camera, random);
        float distance = (float) position.distanceTo(camera.getPosition());
        if (distance > maxDistance) {
            return 0;
        }
        float distanceMultiplier = 1;
        if (distance > falloffDistance) {
            float remaining = maxDistance - falloffDistance;
            float current = distance - falloffDistance;
            distanceMultiplier = 1 - current / remaining;
        }
        Vector3f lookDirection = camera.getLookVector();
        Vec3 directionToScreenshake = position.subtract(camera.getPosition()).normalize();
        float angle = Math.max(0, lookDirection.dot(directionToScreenshake.toVector3f()));
        return ((intensity * distanceMultiplier) + (intensity * angle)) * 0.5f;
    }
}