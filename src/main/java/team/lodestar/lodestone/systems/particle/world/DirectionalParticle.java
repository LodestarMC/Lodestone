package team.lodestar.lodestone.systems.particle.world;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.util.*;
import net.minecraft.world.phys.*;
import team.lodestar.lodestone.helpers.*;
import team.lodestar.lodestone.systems.particle.*;
import team.lodestar.lodestone.systems.particle.options.*;
import team.lodestar.lodestone.systems.rendering.*;

import static team.lodestar.lodestone.systems.particle.SimpleParticleOptions.ParticleSpritePicker.*;

public class DirectionalParticle extends GenericParticle<DirectionalParticleOptions> {

    public final Vec3 direction;
    public final Quaternion quaternion = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);

    public DirectionalParticle(ClientLevel world, DirectionalParticleOptions data, ParticleEngine.MutableSpriteSet spriteSet, double x, double y, double z, double xd, double yd, double zd) {
        super(world, data, spriteSet, x, y, z, xd, yd, zd);
        this.direction = data.direction;

        float yRot = ((float)(Mth.atan2(direction.x, direction.z) * (double)(180F / (float)Math.PI)));
        float xRot = ((float)(Mth.atan2(direction.y, direction.horizontalDistance()) * (double)(180F / (float)Math.PI)));
        float yaw = (float) Math.toRadians(yRot);
        float pitch = (float) Math.toRadians(-xRot);
        quaternion.mul(new Quaternion(0, yaw, 0, false));
        quaternion.mul(new Quaternion(pitch, 0, 0, false));
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        if (lifeDelay > 0) {
            return;
        }
        consumer = getVertexConsumer(consumer);
        Vec3 vec3 = camera.getPosition();
        float x = (float)(Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
        float y = (float)(Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
        float z = (float)(Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = this.getQuadSize(partialTicks);
        for(int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.transform(quaternion);
            vector3f.mul(f4);
            vector3f.add(x, y, z);
        }

        float f7 = this.getU0();
        float f8 = this.getU1();
        float f5 = this.getV0();
        float f6 = this.getV1();
        int j = this.getLightColor(partialTicks);
        consumer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        consumer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        consumer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        consumer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
    }

    @Override
    public SimpleParticleOptions.ParticleSpritePicker getSpritePicker() {
        return FIRST_INDEX;
    }

}