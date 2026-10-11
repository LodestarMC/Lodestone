package team.lodestar.lodestone.modules.rendering.particle.standard.screen.base;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import team.lodestar.lodestone.modules.rendering.particle.standard.render_types.LodestoneScreenParticleRenderType;

@OnlyIn(Dist.CLIENT)
public abstract class ScreenParticle {

    public static final RandomSource RANDOM = RandomSource.create();

    public final ClientLevel level;

    public double xOld, yOld;
    public double x, y;
    public double xMotion, yMotion;
    public double xMoved, yMoved;
    public boolean removed;
    public int age, lifetime;
    public float size = 1;
    public float r = 1, g = 1, b = 1, a = 1;
    public float oRoll, roll;

    public float gravity;
    public float friction = 0.98F;

    protected ScreenParticle(ClientLevel level, double x, double y) {
        this.level = level;
        this.setSize(0.2F);
        this.x = x;
        this.y = y;
        this.xOld = x;
        this.yOld = y;
        this.lifetime = (int) (4.0F / (RANDOM.nextFloat() * 0.9F + 0.1F));
    }

    public ScreenParticle(ClientLevel pLevel, double pX, double pY, double pXSpeed, double pYSpeed) {
        this(pLevel, pX, pY);
        this.xMotion = pXSpeed + (Math.random() * 2.0D - 1.0D) * (double) 0.4F;
        this.yMotion = pYSpeed + (Math.random() * 2.0D - 1.0D) * (double) 0.4F;
        double d0 = (Math.random() + Math.random() + 1.0D) * (double) 0.15F;
        double d1 = Math.sqrt(this.xMotion * this.xMotion + this.yMotion * this.yMotion);
        this.xMotion = this.xMotion / d1 * d0 * (double) 0.4F;
        this.yMotion = this.yMotion / d1 * d0 * (double) 0.4F + (double) 0.1F;
    }

    public abstract void render(BufferBuilder bufferBuilder, PoseStack poseStack);

    public abstract LodestoneScreenParticleRenderType getRenderType();

    public void setParticleSpeed(double pXd, double pYd) {
        this.xMotion = pXd;
        this.yMotion = pYd;
    }

    public ScreenParticle setSize(float size) {
        this.size = size;
        return this;
    }

    public void setColor(float pParticleRed, float pParticleGreen, float pParticleBlue) {
        this.r = pParticleRed;
        this.g = pParticleGreen;
        this.b = pParticleBlue;
    }

    protected void setAlpha(float pAlpha) {
        this.a = pAlpha;
    }

    public void setLifetime(int pParticleLifeTime) {
        this.lifetime = pParticleLifeTime;
    }

    public int getLifetime() {
        return this.lifetime;
    }

    public void tick() {
        this.xOld = this.x;
        this.yOld = this.y;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.yMotion -= 0.04D * (double) this.gravity;
            this.xMotion *= this.friction;
            this.yMotion *= this.friction;
            this.x += xMotion;
            this.y += yMotion;
            this.xMoved += xMotion;
            this.yMoved += yMotion;
        }
    }

    public void remove() {
        this.removed = true;
    }

    public boolean isAlive() {
        return !this.removed;
    }
}