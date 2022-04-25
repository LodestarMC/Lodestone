package com.sammy.ortus.options;

import com.sammy.ortus.config.ClientConfig;
import com.sammy.ortus.systems.option.OrtusOption;
import net.minecraft.client.ProgressOption;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.event.ScreenEvent;

import static com.sammy.ortus.OrtusLib.ORTUS;

public class FireOffsetOption extends ProgressOption implements OrtusOption {

    private static final Component TOOLTIP = new TranslatableComponent("options." + ORTUS + ".fire_offset.tooltip");

    public FireOffsetOption() {
        super("options." + ORTUS + ".fire_offset",
                0.0D,
                1.0D,
                0.01F,
                (options) -> ClientConfig.FIRE_OVERLAY_OFFSET.getConfigValue(),
                (options, value) -> ClientConfig.FIRE_OVERLAY_OFFSET.setConfigValue(Math.round(value * 100d) / 100d),
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