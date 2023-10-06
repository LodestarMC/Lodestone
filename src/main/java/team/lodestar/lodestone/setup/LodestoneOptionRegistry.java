package team.lodestar.lodestone.setup;

import com.mojang.serialization.Codec;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.ScreenEvent;

public class LodestoneOptionRegistry {

    public static final OptionInstance<Double> SCREENSHAKE_INTENSITY = createSliderOption("lodestone.options.screenshakeIntensity");
    public static final OptionInstance<Double> FIRE_OFFSET = createSliderOption("lodestone.options.fireOffset");

    private static OptionInstance<Double> createSliderOption(String key) {
        return new OptionInstance<>(key,
                OptionInstance.cachedConstantTooltip(Component.translatable(key + ".tooltip")),
                (component, val) -> val == 0.0D ? Component.translatable("options.generic_value", component,
                        CommonComponents.OPTION_OFF) :
                        Component.translatable("options.percent_value", component, (int) (val * 100.0D)),
                OptionInstance.UnitDouble.INSTANCE.xmap(Mth::square, Math::sqrt),
                Codec.doubleRange(0.0D, 1.0D), 1.0D, (val) -> {
        });
    }

    public static void addOption(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof AccessibilityOptionsScreen accessibilityOptionsScreen) {
            accessibilityOptionsScreen.list.addSmall(SCREENSHAKE_INTENSITY, FIRE_OFFSET);
        }
    }
}
