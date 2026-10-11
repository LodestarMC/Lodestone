package team.lodestar.lodestone.modules.rendering.particle.standard.renderer;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import org.joml.*;
import team.lodestar.lodestone.helpers.*;
import team.lodestar.lodestone.modules.rendering.particle.standard.*;

public class BillboardParticleRenderer implements LodestoneParticleRenderer {

    public static final BillboardParticleRenderer INSTANCE = new BillboardParticleRenderer();

    private BillboardParticleRenderer() {
    }

    @Override
    public void render(LodestoneParticleBody particle, VertexConsumer consumer, Camera camera, float partialTicks) {
        var bounds = getVertices(particle, camera, partialTicks);

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

    private Vector3f[] getVertices(LodestoneParticleBody particle, @Nullable Camera camera, float partialTicks) {
        var position = particle.getInterpolatedPosition(partialTicks);
        float spin = particle.getInterpolatedSpin(partialTicks);
        float width = particle.getInterpolatedWidth(partialTicks);
        float height = particle.getInterpolatedHeight(partialTicks);
        var vertices = new Vector3f[]{new Vector3f(-1, -1, 0), new Vector3f(-1, 1, 0), new Vector3f(1, 1, 0), new Vector3f(1, -1, 0)};

        Quaternionf rotation;
        if (particle.isWorld()) {
            assert camera != null;
            rotation = new Quaternionf(camera.rotation());
            rotation.rotateZ(spin);
        }
        else {
            rotation = new Quaternionf(new AxisAngle4f(spin, VecHelper.Vector3fHelper.ZP));
        }
        for (int i = 0; i < 4; ++i) {
            var vertex = vertices[i];
            vertex.rotate(rotation);
            vertex.mul(width, height, 1);
            vertex.add(position);
        }
        return vertices;
    }
}
