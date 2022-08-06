package team.lodestar.lodestone.options;

import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.data.LodestoneLangDatagen;
import team.lodestar.lodestone.systems.option.LodestoneOption;
import net.minecraft.client.ProgressOption;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.event.ScreenEvent;

public class ScreenshakeOption extends ProgressOption implements LodestoneOption {

    private static final Component TOOLTIP = new TranslatableComponent(LodestoneLangDatagen.getOptionTooltip("screenshake_intensity"));

    public ScreenshakeOption() {
        super(LodestoneLangDatagen.getOption("screenshake_intensity"),
                0.0D,
                1.0D,
                0.01F,
                (options) -> ClientConfig.SCREENSHAKE_INTENSITY.getConfigValue(),
                (options, value) -> ClientConfig.SCREENSHAKE_INTENSITY.setConfigValue(Math.round(value * 100d) / 100d),
                (options, progressOption) -> {
                    double value = progressOption.toPct(progressOption.get(options));
                    return value == 0.0D ? progressOption.genericValueLabel(CommonComponents.OPTION_OFF) : progressOption.percentValueLabel(value);
                },
                (minecraft) -> minecraft.font.split(TOOLTIP, 200));
    }

    @Override
    public boolean canAdd(ScreenEvent.InitScreenEvent.Post event) {
        return event.getScreen() instanceof AccessibilityOptionsScreen;
    }
}