package team.lodestar.lodestone.systems.particle.world;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.world.phys.*;
import team.lodestar.lodestone.systems.particle.*;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.rendering.*;

import static team.lodestar.lodestone.systems.particle.SimpleParticleOptions.ParticleSpritePicker.*;

public class SparkParticle extends GenericParticle {

    public static final VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setParticleFormat();

    public final GenericParticleData lengthData;

    public SparkParticle(ClientLevel world, SparkParticleOptions data, ParticleEngine.MutableSpriteSet spriteSet, double x, double y, double z, double xd, double yd, double zd) {
        super(world, data, spriteSet, x, y, z, xd, yd, zd);
        this.lengthData = data.lengthData;
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        consumer = getVertexConsumer(consumer);
        builder.setColorRaw(rCol, gCol, bCol).setAlpha(alpha);

        float length = lengthData.getValue(age, lifetime);

        Vec3 movingTo = getPos().add(getParticleSpeed()).normalize().scale(length);
        Vec3 movingFrom = getPos().subtract(getParticleSpeed()).normalize().scale(length);

        builder.renderBeam(consumer, null, movingFrom, movingTo, quadSize);
    }

    @Override
    public SimpleParticleOptions.ParticleSpritePicker getSpritePicker() {
        return FIRST_INDEX;
    }
}