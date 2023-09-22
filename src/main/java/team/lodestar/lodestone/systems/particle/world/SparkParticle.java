package team.lodestar.lodestone.systems.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;

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
        Vec3 vec3 = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
        float y = (float) (Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
        float z = (float) (Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
        final Vec3 pos = new Vec3(x, y, z);
        consumer = getVertexConsumer(consumer);
        builder.setUV(getU0(), getV0(), getU1(), getV1()).setColorRaw(rCol, gCol, bCol).setAlpha(alpha);

        float length = lengthData.getValue(age, lifetime);
        Vec3 offset = getParticleSpeed().normalize().scale(length);
        Vec3 movingTo = pos.add(offset);
        Vec3 movingFrom = pos.subtract(offset);

        builder.renderBeam(consumer, null, movingFrom, movingTo, quadSize);
    }

    @Override
    public SimpleParticleOptions.ParticleSpritePicker getSpritePicker() {
        return FIRST_INDEX;
    }
}