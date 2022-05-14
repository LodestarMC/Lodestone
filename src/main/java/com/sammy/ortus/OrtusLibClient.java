package com.sammy.ortus;

import com.sammy.ortus.systems.rendering.ghost.GhostBlocks;
import com.sammy.ortus.systems.rendering.outline.Outliner;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class OrtusLibClient {

    public static final GhostBlocks GHOST_BLOCKS = new GhostBlocks();
    public static final Outliner OUTLINER = new Outliner();

    public static void onOrtus(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(OrtusLibClient::clientInit);
    }

    private static void clientInit(final FMLClientSetupEvent event) {
    }
}
