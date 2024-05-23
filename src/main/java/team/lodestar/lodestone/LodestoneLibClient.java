package team.lodestar.lodestone;

import io.github.fabricators_of_create.porting_lib.client_events.event.client.RenderArmCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.registry.common.LodestoneBlockEntityRegistry;
import team.lodestar.lodestone.registry.common.LodestoneOptionRegistry;
import team.lodestar.lodestone.systems.client.ClientTickCounter;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;

public class LodestoneLibClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        LodestoneBlockEntityRegistry.ClientOnly.registerRenderer();
        LodestoneOptionRegistry.addOption();

        initEvents();
    }

    public static void initEvents(){
        WorldRenderEvents.END.register(ScreenParticleHandler::renderTick);
        WorldRenderEvents.LAST.register(PostProcessHandler::onWorldRenderLast);
    }
}
