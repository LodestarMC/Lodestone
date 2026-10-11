package team.lodestar.lodestone.modules.rendering.particle.standard.renderer;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import org.joml.*;
import team.lodestar.lodestone.modules.rendering.particle.standard.*;

public class BillboardParticleRenderer implements LodestoneParticleRenderer {

    public static final BillboardParticleRenderer INSTANCE = new BillboardParticleRenderer();

    private BillboardParticleRenderer() {
    }

    @Override
    public void render(LodestoneParticleBody particle, VertexConsumer consumer, Camera camera, float partialTicks) {
        var pos = particle.getInterpolatedPosition(camera, partialTicks);

        Quaternionf quaternionf;
        if (particle.getRoll() == 0.0F) {
            quaternionf = camera.rotation();
        } else {
            quaternionf = new Quaternionf(camera.rotation());
            quaternionf.rotateZ(Mth.lerp(partialTicks, particle.getORoll(), particle.getRoll()));
        }

        var bounds = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float width = particle.getQuadSize(partialTicks);
        float length = particle.getQuadLength(partialTicks);

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = bounds[i];
            vector3f.mul(width, length, 1);
            vector3f.rotate(quaternionf);
            vector3f.add(pos.x, pos.y, pos.z);
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
        consumer.addVertex(bounds[0].x(), bounds[0].y(), bounds[0].z()).setUv(u1, v1).setColor(red, green, blue, alpha).setLight(j);
        consumer.addVertex(bounds[1].x(), bounds[1].y(), bounds[1].z()).setUv(u1, v0).setColor(red, green, blue, alpha).setLight(j);
        consumer.addVertex(bounds[2].x(), bounds[2].y(), bounds[2].z()).setUv(u0, v0).setColor(red, green, blue, alpha).setLight(j);
        consumer.addVertex(bounds[3].x(), bounds[3].y(), bounds[3].z()).setUv(u0, v1).setColor(red, green, blue, alpha).setLight(j);
    }
}
