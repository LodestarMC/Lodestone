package team.lodestar.lodestone.systems.particle.world.behaviors;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import net.minecraft.world.phys.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.components.*;
import team.lodestar.lodestone.systems.rendering.*;

public class SparkParticleBehavior implements LodestoneParticleBehavior {

    private static final VFXBuilders.WorldVFXBuilder SPARK_BUILDER = VFXBuilders.createWorld().setFormat(DefaultVertexFormat.PARTICLE);

    protected SparkParticleBehavior() {

    }

    @Override
    public SparkBehaviorComponent getComponent(LodestoneBehaviorComponent component) {
        return component instanceof SparkBehaviorComponent spark ? spark : LodestoneBehaviorComponent.SPARK;
    }

    @Override
    public void render(LodestoneWorldParticle particle, VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 vec3 = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, particle.getXOld(), particle.getX()) - vec3.x());
        float y = (float) (Mth.lerp(partialTicks, particle.getYOld(), particle.getY()) - vec3.y());
        float z = (float) (Mth.lerp(partialTicks, particle.getZOld(), particle.getZ()) - vec3.z());
        final Vec3 pos = new Vec3(x, y, z);
        var component = getComponent(particle.behaviorComponent);
        var lengthData = component.getLengthData(particle);
        float length = lengthData.getValue(particle.getAge(), particle.getLifetime());
        Vec3 offset = particle.getParticleSpeed().normalize().scale(length);
        Vec3 movingTo = pos.add(offset);
        Vec3 movingFrom = pos.subtract(offset);
        SPARK_BUILDER.setVertexConsumer(consumer)
                .setUV(particle.getU0(), particle.getV0(), particle.getU1(), particle.getV1())
                .setColorRaw(particle.getRed(), particle.getGreen(), particle.getBlue())
                .setAlpha(particle.getAlpha())
                .renderBeam(null, movingFrom, movingTo, particle.getQuadSize(partialTicks), Vec3.ZERO);
    }
}