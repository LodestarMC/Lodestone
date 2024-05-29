package team.lodestar.lodestone.systems.particle.world.behaviors;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import net.minecraft.world.phys.*;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.rendering.*;

public class SparkParticleBehavior implements LodestoneParticleBehavior<SparkParticleBehavior> {

    public final GenericParticleData lengthData;

    private static final VFXBuilders.WorldVFXBuilder SPARK_BUILDER = VFXBuilders.createWorld().setFormat(DefaultVertexFormat.PARTICLE);

    public SparkParticleBehavior(GenericParticleData lengthData) {
        this.lengthData = lengthData;
    }

    @Override
    public void render(LodestoneWorldParticle<SparkParticleBehavior> particle, VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 vec3 = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, particle.getXOld(), particle.getX()) - vec3.x());
        float y = (float) (Mth.lerp(partialTicks, particle.getYOld(), particle.getY()) - vec3.y());
        float z = (float) (Mth.lerp(partialTicks, particle.getZOld(), particle.getZ()) - vec3.z());
        final Vec3 pos = new Vec3(x, y, z);

        float length = lengthData.getValue(particle.age(), particle.getLifetime());
        Vec3 offset = particle.getParticleMotion().normalize().scale(length);
        Vec3 movingTo = pos.add(offset);
        Vec3 movingFrom = pos.subtract(offset);
        SPARK_BUILDER.setVertexConsumer(particle.getVertexConsumer(consumer))
                .setUV(particle.getU0(), particle.getV0(), particle.getU1(), particle.getV1())
                .setColorRaw(particle.getRed(), particle.getGreen(), particle.getBlue())
                .setAlpha(particle.getAlpha())
                .renderBeam(null, movingFrom, movingTo, particle.getQuadSize(partialTicks), Vec3.ZERO);
    }
}
