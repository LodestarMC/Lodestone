package com.sammy.ortus;

import com.sammy.ortus.handlers.GhostBlockHandler;
import com.sammy.ortus.systems.rendering.outline.Outliner;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class OrtusLibClient {

    public static final Outliner OUTLINER = new Outliner();

    public static void onOrtus(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(OrtusLibClient::clientInit);
    }

    private static void clientInit(final FMLClientSetupEvent event) {
    }
}
