package team.lodestar.lodestone.systems.particle.world;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.util.*;
import net.minecraft.world.phys.*;
import team.lodestar.lodestone.systems.particle.*;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.rendering.*;

import static team.lodestar.lodestone.systems.particle.SimpleParticleOptions.ParticleSpritePicker.FIRST_INDEX;

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
        Vec3 vec3 = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
        float y = (float) (Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
        float z = (float) (Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
        final Vec3 pos = new Vec3(x, y, z);

        float length = lengthData.getValue(age, lifetime);
        Vec3 offset = getParticleSpeed().normalize().scale(length);
        Vec3 movingTo = pos.add(offset);
        Vec3 movingFrom = pos.subtract(offset);
        builder.setUV(getU0(), getV0(), getU1(), getV1()).setColorRaw(rCol, gCol, bCol).setAlpha(alpha);
        builder.renderBeam(consumer, null, movingFrom, movingTo, quadSize, Vec3.ZERO);
    }

    @Override
    public SimpleParticleOptions.ParticleSpritePicker getSpritePicker() {
        return FIRST_INDEX;
    }

}