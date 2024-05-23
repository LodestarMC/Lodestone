package team.lodestar.lodestone;

import io.github.fabricators_of_create.porting_lib.config.ConfigRegistry;
import io.github.fabricators_of_create.porting_lib.config.ConfigType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.events.ClientRuntimeEvents;
import team.lodestar.lodestone.events.LodestoneRenderEvents;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.handlers.screenparticle.ParticleEmitterHandler;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.registry.common.LodestoneBlockEntityRegistry;
import team.lodestar.lodestone.registry.common.LodestoneOptionRegistry;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;

public class LodestoneLibClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ConfigRegistry.registerConfig(LodestoneLib.LODESTONE, ConfigType.CLIENT, ClientConfig.clientSpec);

        LodestoneParticleRegistry.registerParticleFactory();

        LodestoneBlockEntityRegistry.ClientOnly.registerRenderer();
        LodestoneOptionRegistry.addOption();
        ParticleEmitterHandler.registerParticleEmitters();

        WorldRenderEvents.END.register(ScreenParticleHandler::renderTick);
        WorldRenderEvents.LAST.register(PostProcessHandler::onWorldRenderLast);
        ClientTickEvents.END_CLIENT_TICK.register(ClientRuntimeEvents::clientTick);

        LodestoneRenderEvents.AFTER_SKY.register(ClientRuntimeEvents::renderStages);
        LodestoneRenderEvents.AFTER_PARTICLES.register(ClientRuntimeEvents::renderStages);
        LodestoneRenderEvents.AFTER_WEATHER.register(ClientRuntimeEvents::renderStages);

        RenderHandler.onClientSetup();
    }
}
