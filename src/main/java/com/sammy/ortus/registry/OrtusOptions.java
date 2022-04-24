package com.sammy.ortus.registry;

import com.sammy.ortus.options.FireOffsetOption;
import com.sammy.ortus.options.ScreenshakeOption;
import com.sammy.ortus.systems.option.OrtusOption;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;

@SuppressWarnings("ALL")
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OrtusOptions {
    public static final ArrayList<OrtusOption> OPTIONS = new ArrayList<>();

    @SubscribeEvent
    public static void registerOptions(FMLCommonSetupEvent event) {
        registerOption(new ScreenshakeOption());
        registerOption(new FireOffsetOption());
    }

    public static void registerOption(OrtusOption option) {
        OPTIONS.add(option);
    }
}