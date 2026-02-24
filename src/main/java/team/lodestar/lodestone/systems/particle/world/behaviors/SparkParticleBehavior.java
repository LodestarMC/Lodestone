package team.lodestar.lodestone.systems.particle.world.behaviors;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import net.minecraft.world.phys.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.rendering.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.rendering.*;

/**
 * A particle behavior, renders as a moving spark that follows the velocity of the particle.
 * Can be configured to use a predefined direction instead.
 */
public class SparkParticleBehavior implements LodestoneParticleBehavior {

    private static final VFXBuilders.WorldVFXBuilder SPARK_BUILDER = VFXBuilders.createWorld().setFormat(DefaultVertexFormat.PARTICLE);

    public static final Vec3 UP = new Vec3(0, 1, 0);
    public static final Vec3 DOWN = new Vec3(0, -1, 0);

    protected Vec3 forcedDirection;
    protected float lengthCenter;

    private Vec3 cachedDirection;

    public static SparkParticleBehavior sparkBehavior() {
        return new SparkParticleBehavior();
    }

    protected SparkParticleBehavior() {

    }

    /**
     * Defines a static direction for the particle to aim towards instead, regardless of velocity
     */
    public SparkParticleBehavior setForcedDirection(Vec3 forcedDirection) {
        this.forcedDirection = forcedDirection;
        return this;
    }

    /**
     * Defines a weight center for the particle's length distribution to leverage off of
     * Positive values cause the spark to extrude forward, whereas negative ones cause it to extrude backwards
     * 0 causes an equal length distribution between forwards and backwards
     */
    public SparkParticleBehavior setLengthCenter(float lengthCenter) {
        this.lengthCenter = lengthCenter;
        return this;
    }

    @Override
    public void tick(LodestoneWorldParticle particle) {
        var direction = particle.getParticleSpeed().normalize();
        if (!direction.equals(Vec3.ZERO)) {
            cachedDirection = direction;
        }
    }

    @Override
    public void render(LodestoneWorldParticle particle, VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 vec3 = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, particle.getXOld(), particle.getX()) - vec3.x());
        float y = (float) (Mth.lerp(partialTicks, particle.getYOld(), particle.getY()) - vec3.y());
        float z = (float) (Mth.lerp(partialTicks, particle.getZOld(), particle.getZ()) - vec3.z());
        Vec3 pos = new Vec3(x, y, z);
        float length = particle.getQuadLength(partialTicks);
        Vec3 offset = getDirection(particle).scale(length);
        Vec3 movingFrom = getStartPos(pos, offset);
        Vec3 movingTo = getEndPos(pos, offset);
        SPARK_BUILDER.setVertexConsumer(consumer)
                .setUV(particle.getU0(), particle.getV0(), particle.getU1(), particle.getV1())
                .setColor(particle.getRed(), particle.getGreen(), particle.getBlue())
                .setAlpha(particle.getAlpha())
                .renderBeam(null, movingFrom, movingTo, particle.getQuadSize(partialTicks), Vec3.ZERO);
    }

    public Vec3 getDirection(LodestoneWorldParticle particle) {
        if (forcedDirection != null) {
            return forcedDirection;
        }
        return cachedDirection != null ? cachedDirection : particle.getParticleSpeed().normalize();
    }

    public Vec3 getStartPos(Vec3 pos, Vec3 offset) {
        return pos.add(offset.scale(lengthCenter-1));
    }

    public Vec3 getEndPos(Vec3 pos, Vec3 offset) {
        return pos.add(offset.scale(lengthCenter+1));
    }
}