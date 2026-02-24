package team.lodestar.lodestone.systems.particle.data.color;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import team.lodestar.lodestone.helpers.RandomHelper;
import team.lodestar.lodestone.systems.easing.Easing;

import java.awt.*;

public class ColorParticleData implements ColorParticleDataWrapper {

    public static final Codec<ColorParticleData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("r1").forGetter(data -> data.r1),
            Codec.FLOAT.fieldOf("g1").forGetter(data -> data.g1),
            Codec.FLOAT.fieldOf("b1").forGetter(data -> data.b1),
            Codec.FLOAT.fieldOf("r2").forGetter(data -> data.r2),
            Codec.FLOAT.fieldOf("g2").forGetter(data -> data.g2),
            Codec.FLOAT.fieldOf("b2").forGetter(data -> data.b2),
            Codec.FLOAT.fieldOf("colorCoefficient").forGetter(data -> data.colorCoefficient),
            Easing.CODEC.fieldOf("colorCurveEasing").forGetter(data -> data.colorCurve),
            Codec.FLOAT.fieldOf("coefficientMultiplier").forGetter(data -> data.coefficientMultiplier)
    ).apply(instance, ColorParticleData::new));

    protected final float r1, g1, b1, r2, g2, b2;
    protected final Color startingColor, endingColor;
    protected final float colorCoefficient;
    protected final Easing colorCurve;

    protected float coefficientMultiplier = 1;
    protected boolean locked;

    protected ColorParticleData(float r1, float g1, float b1, float r2, float g2, float b2, float colorCoefficient, Easing colorCurve, float coefficientMultiplier) {
        this(r1, g1, b1, r2, g2, b2, colorCoefficient, colorCurve);
        this.coefficientMultiplier = coefficientMultiplier;
    }

    protected ColorParticleData(float r1, float g1, float b1, float r2, float g2, float b2, float colorCoefficient, Easing colorCurve) {
        this.r1 = r1;
        this.g1 = g1;
        this.b1 = b1;
        this.r2 = r2;
        this.g2 = g2;
        this.b2 = b2;
        this.startingColor = new Color(r1, g1, b1);
        this.endingColor = new Color(r2, g2, b2);
        this.colorCoefficient = colorCoefficient;
        this.colorCurve = colorCurve;
    }

    @Override
    public ColorParticleData unwrap() {
        return this;
    }

    /**
     * Locks the data, preventing any modifications to the value and coefficient.
     */
    public ColorParticleData lock() {
        locked = true;
        return this;
    }

    public Color getStartingColor() {
        return startingColor;
    }

    public Color getEndingColor() {
        return endingColor;
    }

    public float getColorCoefficient() {
        return colorCoefficient;
    }

    public Easing getColorCurve() {
        return colorCurve;
    }

    public ColorParticleData multiplyCoefficient(float coefficientMultiplier) {
        if (!locked) {
            this.coefficientMultiplier *= coefficientMultiplier;
        }
        return this;
    }

    public ColorParticleData overrideCoefficientMultiplier(float coefficientMultiplier) {
        if (!locked) {
            this.coefficientMultiplier = coefficientMultiplier;
        }
        return this;
    }

    public void rgbToHsv(float[] hsv1, float[] hsv2) {
        Color.RGBtoHSB((int) (255 * Math.min(1.0f, r1)), (int) (255 * Math.min(1.0f, g1)), (int) (255 * Math.min(1.0f, b1)), hsv1);
        Color.RGBtoHSB((int) (255 * Math.min(1.0f, r2)), (int) (255 * Math.min(1.0f, g2)), (int) (255 * Math.min(1.0f, b2)), hsv2);

    }

    public float getProgress(float age, float lifetime) {
        return Mth.clamp((age * colorCoefficient * coefficientMultiplier) / lifetime, 0, 1);
    }

    public ColorParticleDataBuilder invert() {
        return create(r2, g2, b2, r1, g1, b1).setCoefficient(colorCoefficient).setEasing(colorCurve);
    }

    public ColorParticleDataBuilder copy() {
        return create(r1, g1, b1, r2, g2, b2).setCoefficient(colorCoefficient).setEasing(colorCurve);
    }

    public static ColorParticleDataBuilder create(float r1, float g1, float b1, float r2, float g2, float b2) {
        return new ColorParticleDataBuilder(r1, g1, b1, r2, g2, b2);
    }

    public static ColorParticleDataBuilder create(float r, float g, float b) {
        return new ColorParticleDataBuilder(r, g, b, r, g, b);
    }

    public static ColorParticleDataBuilder create(Color start, Color end) {
        return create(start.getRed() / 255f, start.getGreen() / 255f, start.getBlue() / 255f, end.getRed() / 255f, end.getGreen() / 255f, end.getBlue() / 255f);
    }

    public static ColorParticleDataBuilder create(Color color) {
        return create(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
    }

    public static ColorParticleData createGrayParticleColor(RandomSource random) {
        return createGrayParticleColor(random, 0.6f, 1f);
    }

    public static ColorParticleData createGrayParticleColor(RandomSource random, float min, float max) {
        int brightness = (int) (255 * RandomHelper.randomBetween(random, min, max));
        Color color = new Color(brightness, brightness, brightness);
        return ColorParticleData.create(color, color.darker()).setEasing(Easing.SINE_IN_OUT).build();
    }
}