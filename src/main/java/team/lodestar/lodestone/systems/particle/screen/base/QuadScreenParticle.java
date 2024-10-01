package team.lodestar.lodestone.systems.particle.screen.base;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import team.lodestar.lodestone.helpers.VecHelper;

@Environment(EnvType.CLIENT)
public abstract class QuadScreenParticle extends ScreenParticle {
    protected float quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;

    protected QuadScreenParticle(ClientLevel pLevel, double pX, double pY) {
        super(pLevel, pX, pY);
    }

    protected QuadScreenParticle(ClientLevel pLevel, double pX, double pY, double pXSpeed, double pYSpeed) {
        super(pLevel, pX, pY, pXSpeed, pYSpeed);
    }

    @Override
    public void render(BufferBuilder bufferBuilder) {
        float partialTicks = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        float size = getQuadSize(partialTicks) * 10;
        float u0 = getU0();
        float u1 = getU1();
        float v0 = getV0();
        float v1 = getV1();
        float roll = Mth.lerp(partialTicks, this.oRoll, this.roll);
        Vector3f[] vectors = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        Quaternionf rotation = new Quaternionf(new AxisAngle4f(roll, VecHelper.Vector3fHelper.ZP));
        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = vectors[i];
            vector3f.rotate(rotation);
            vector3f.mul(size);
            vector3f.add((float) x, (float) y, 0);
        }
        float quadZ = getQuadZPosition();
        bufferBuilder.addVertex(vectors[0].x(), vectors[0].y(), quadZ).setUv(u1, v1).setColor(this.rCol, this.gCol, this.bCol, this.alpha);
        bufferBuilder.addVertex(vectors[1].x(), vectors[1].y(), quadZ).setUv(u1, v0).setColor(this.rCol, this.gCol, this.bCol, this.alpha);
        bufferBuilder.addVertex(vectors[2].x(), vectors[2].y(), quadZ).setUv(u0, v0).setColor(this.rCol, this.gCol, this.bCol, this.alpha);
        bufferBuilder.addVertex(vectors[3].x(), vectors[3].y(), quadZ).setUv(u0, v1).setColor(this.rCol, this.gCol, this.bCol, this.alpha);
    }

    public float getQuadSize(float partialTicks) {
        return this.quadSize;
    }

    public float getQuadZPosition() {
        return 390;
    }

    protected abstract float getU0();

    protected abstract float getU1();

    protected abstract float getV0();

    protected abstract float getV1();
}