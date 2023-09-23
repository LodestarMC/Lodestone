package team.lodestar.lodestone.systems.particle.data.color;

import team.lodestar.lodestone.systems.easing.*;

public class ColorParticleDataBuilder {

    protected float r1, g1, b1, r2, g2, b2;
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
