package team.lodestar.lodestone.systems.particle.world;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.options.SparkParticleOptions;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;

public class SparkParticle extends GenericParticle<SparkParticleOptions> {

    public static final VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setFormat(DefaultVertexFormat.PARTICLE);

    public final GenericParticleData lengthData;

    public SparkParticle(ClientLevel world, SparkParticleOptions data, FabricSpriteProviderImpl spriteSet, double x, double y, double z, double xd, double yd, double zd) {
        super(world, data, spriteSet, x, y, z, xd, yd, zd);
        this.lengthData = data.lengthData;
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        if (lifeDelay > 0) {
            return;
        }
        renderActors.forEach(actor -> actor.accept(this));
        Vec3 vec3 = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
        float y = (float) (Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
        float z = (float) (Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
        final Vec3 pos = new Vec3(x, y, z);

        float length = lengthData.getValue(age, lifetime);
        Vec3 offset = getParticleSpeed().normalize().scale(length);
        Vec3 movingTo = pos.add(offset);
        Vec3 movingFrom = pos.subtract(offset);
        builder.setVertexConsumer(getVertexConsumer(consumer));
        builder.setUV(getU0(), getV0(), getU1(), getV1()).setColorRaw(rCol, gCol, bCol).setAlpha(alpha);
        builder.renderBeam(null, movingFrom, movingTo, quadSize, Vec3.ZERO);
    }
}