package team.lodestar.lodestone.systems.particle.editor.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import team.lodestar.lodestone.modules.core.easing.Easing;

import java.util.Locale;

public class ColorGroupData {
    public static final Codec<Integer> RGB_CODEC = Codec.STRING.comapFlatMap(ColorGroupData::readColor, ColorGroupData::writeColor);
    public static final Codec<ColorGroupData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("enabled", true).forGetter(data -> data.enabled),
            RGB_CODEC.optionalFieldOf("startColor", 0x79D9FF).forGetter(data -> data.startColor),
            RGB_CODEC.optionalFieldOf("endColor", 0xFFFFFF).forGetter(data -> data.endColor),
            Codec.FLOAT.optionalFieldOf("coefficient", 1.0f).forGetter(data -> data.coefficient),
            Easing.CODEC.optionalFieldOf("easing", Easing.SINE_IN_OUT).forGetter(data -> data.easing)
    ).apply(instance, ColorGroupData::new));

    public boolean enabled;
    public int startColor;
    public int endColor;
    public float coefficient;
    public Easing easing;

    public ColorGroupData(boolean enabled, int startColor, int endColor, float coefficient, Easing easing) {
        this.enabled = enabled;
        this.startColor = startColor;
        this.endColor = endColor;
        this.coefficient = coefficient;
        this.easing = easing != null ? easing : Easing.SINE_IN_OUT;
    }

    public static ColorGroupData createDefault() {
        return new ColorGroupData(true, 0x79D9FF, 0xFFFFFF, 1.0f, Easing.SINE_IN_OUT);
    }

    private static DataResult<Integer> readColor(String value) {
        String clean = value.startsWith("#") ? value.substring(1) : value;
        try {
            return DataResult.success(Integer.parseUnsignedInt(clean, 16) & 0xFFFFFF);
        } catch (NumberFormatException exception) {
            return DataResult.error(() -> "Invalid RGB color: " + value);
        }
    }

    private static String writeColor(int color) {
        return "#" + String.format(Locale.ROOT, "%06X", color & 0xFFFFFF);
    }
}
