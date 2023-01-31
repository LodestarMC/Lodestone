package team.lodestar.lodestone.systems.particle.data;

import net.minecraft.util.Mth;
import team.lodestar.lodestone.systems.easing.Easing;

import java.awt.*;

public class ColorParticleData {

    public final float r1, g1, b1, r2, g2, b2;
    public final float colorCoefficient;
    public final Easing colorCurveEasing;

    public ColorParticleData(float r1, float g1, float b1, float r2, float g2, float b2, float colorCoefficient, Easing colorCurveEasing) {
        this.r1 = r1;
        this.g1 = g1;
        this.b1 = b1;
        this.r2 = r2;
        this.g2 = g2;
        this.b2 = b2;
        this.colorCoefficient = colorCoefficient;
        this.colorCurveEasing = colorCurveEasing;
    }

    public ColorParticleData(float r1, float g1, float b1, float r2, float g2, float b2, float colorCoefficient) {
        this(r1, g1, b1, r2, g2, b2, colorCoefficient, Easing.LINEAR);
    }

    public ColorParticleData(float r1, float g1, float b1, float r2, float g2, float b2) {
        this(r1, g1, b1, r2, g2, b2, 1f);
    }

    public ColorParticleData(Color start, Color end, float colorCoefficient, Easing colorCurveEasing) {
        this(start.getRed() / 255f, start.getGreen() / 255f, start.getBlue() / 255f, end.getRed() / 255f, end.getGreen() / 255f, end.getBlue() / 255f, colorCoefficient, colorCurveEasing);
    }

    public ColorParticleData(Color start, Color end, float colorCoefficient) {
        this(start, end, colorCoefficient, Easing.LINEAR);
    }

    public ColorParticleData(Color start, Color end) {
        this(start, end, 1f);
    }

    public float getProgress(float age, float lifetime) {
        return Mth.clamp((age * colorCoefficient) / lifetime, 0, 1);
    }
}
