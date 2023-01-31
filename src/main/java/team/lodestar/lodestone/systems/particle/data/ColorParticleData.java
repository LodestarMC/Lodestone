package team.lodestar.lodestone.systems.particle.data;

import net.minecraft.util.Mth;
import team.lodestar.lodestone.systems.easing.Easing;

import java.awt.*;

public class ColorParticleData {

    public final float r1, g1, b1, r2, g2, b2;
    public final float colorCoefficient;
    public final Easing colorCurveEasing;

    protected ColorParticleData(float r1, float g1, float b1, float r2, float g2, float b2, float colorCoefficient, Easing colorCurveEasing) {
        this.r1 = r1;
        this.g1 = g1;
        this.b1 = b1;
        this.r2 = r2;
        this.g2 = g2;
        this.b2 = b2;
        this.colorCoefficient = colorCoefficient;
        this.colorCurveEasing = colorCurveEasing;
    }

    public float getProgress(float age, float lifetime) {
        return Mth.clamp((age * colorCoefficient) / lifetime, 0, 1);
    }

    public static ColorParticleDataBuilder create(float r1, float g1, float b1, float r2, float g2, float b2) {
        return new ColorParticleDataBuilder(r1, g1, b1, r2, g2, b2);
    }

    public static ColorParticleDataBuilder create(Color start, Color end) {
        return create(start.getRed()/255f, start.getGreen()/255f, start.getBlue()/255f, end.getRed()/255f, end.getGreen()/255f, end.getBlue()/255f);
    }

    public static class ColorParticleDataBuilder {

        protected final float r1, g1, b1, r2, g2, b2;
        protected float colorCoefficient = 1f;

        protected Easing colorCurveEasing = Easing.LINEAR;

        protected ColorParticleDataBuilder(float r1, float g1, float b1, float r2, float g2, float b2) {
            this.r1 = r1;
            this.g1 = g1;
            this.b1 = b1;
            this.r2 = r2;
            this.g2 = g2;
            this.b2 = b2;
        }

        public ColorParticleDataBuilder setCoefficient(float coefficient) {
            this.colorCoefficient = coefficient;
            return this;
        }

        public ColorParticleDataBuilder setEasing(Easing easing) {
            this.colorCurveEasing = easing;
            return this;
        }
        public ColorParticleData build() {
            return new ColorParticleData(r1, g1, b1, r2, g2, b2, colorCoefficient, colorCurveEasing);
        }

    }
}
