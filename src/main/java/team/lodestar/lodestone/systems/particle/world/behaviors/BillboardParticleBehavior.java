package team.lodestar.lodestone.systems.particle.world.behaviors;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import net.minecraft.world.phys.*;
import org.joml.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.components.*;

public class BillboardParticleBehavior implements LodestoneParticleBehavior {

    protected BillboardParticleBehavior() {
    }

    @Override
    public LodestoneBehaviorComponent getComponent(LodestoneBehaviorComponent component) {
        return null;
    }

    @Override
    public void render(LodestoneWorldParticle particle, VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 vec3 = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, particle.getXOld(), particle.getX()) - vec3.x());
        float y = (float) (Mth.lerp(partialTicks, particle.getYOld(), particle.getY()) - vec3.y());
        float z = (float) (Mth.lerp(partialTicks, particle.getZOld(), particle.getZ()) - vec3.z());
        Quaternionf quaternionf;
        if (particle.getRoll() == 0.0F) {
            quaternionf = camera.rotation();
        } else {
            quaternionf = new Quaternionf(camera.rotation());
            quaternionf.rotateZ(Mth.lerp(partialTicks, particle.getORoll(), particle.getRoll()));
        }

        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float size = particle.getQuadSize(partialTicks);

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(quaternionf);
            vector3f.mul(size);
            vector3f.add(x, y, z);
        }

        float u0 = particle.getU0();
        float u1 = particle.getU1();
        float v0 = particle.getV0();
        float v1 = particle.getV1();
        float red = particle.getRed();
        float green = particle.getGreen();
        float blue = particle.getBlue();
        float alpha = particle.getAlpha();
        int j = particle.getLightColor(partialTicks);
        consumer.addVertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).setUv(u1, v1).setColor(red, green, blue, alpha).setLight(j);
        consumer.addVertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).setUv(u1, v0).setColor(red, green, blue, alpha).setLight(j);
        consumer.addVertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).setUv(u0, v0).setColor(red, green, blue, alpha).setLight(j);
        consumer.addVertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).setUv(u0, v1).setColor(red, green, blue, alpha).setLight(j);
    }
}
