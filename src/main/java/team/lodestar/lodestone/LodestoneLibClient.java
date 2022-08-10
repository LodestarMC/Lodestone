package team.lodestar.lodestone;

import team.lodestar.lodestone.systems.rendering.outline.Outliner;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class LodestoneLibClient {

    public static final Outliner OUTLINER = new Outliner();

    //TODO: get rid of this?
    public static void onLodestone(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(LodestoneLibClient::clientInit);
    }

    private static void clientInit(final FMLClientSetupEvent event) {
    }
}
