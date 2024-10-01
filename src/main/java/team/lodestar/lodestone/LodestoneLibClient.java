package team.lodestar.lodestone;

import io.github.fabricators_of_create.porting_lib.config.ConfigRegistry;
import io.github.fabricators_of_create.porting_lib.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.events.ClientRuntimeEvents;
import team.lodestar.lodestone.events.LodestoneRenderEvents;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.handlers.screenparticle.ParticleEmitterHandler;
import team.lodestar.lodestone.registry.client.LodestoneOBJModels;
import team.lodestar.lodestone.registry.client.LodestoneShaders;
import team.lodestar.lodestone.registry.common.LodestoneBlockEntities;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneItemCrumbsParticleType;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;

import static team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes.*;


public class LodestoneLibClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ConfigRegistry.registerConfig(LodestoneLib.LODESTONE, ModConfig.Type.CLIENT, ClientConfig.clientSpec);

        registerParticleFactory();

        LodestoneBlockEntities.ClientOnly.registerRenderer();
        ParticleEmitterHandler.registerParticleEmitters();
        LodestoneShaders.init();
        LodestoneOBJModels.loadModels();

        //WorldRenderEvents.END.register(ScreenParticleHandler::renderTick);
        WorldRenderEvents.LAST.register(PostProcessHandler::onWorldRenderLast);
        ClientTickEvents.END_CLIENT_TICK.register(ClientRuntimeEvents::clientTick);

        LodestoneRenderEvents.AFTER_SKY.register(ClientRuntimeEvents::renderStages);
        LodestoneRenderEvents.AFTER_PARTICLES.register(ClientRuntimeEvents::renderStages);
        LodestoneRenderEvents.AFTER_WEATHER.register(ClientRuntimeEvents::renderStages);
        LodestoneRenderEvents.BEFORE_CLEAR.register(PostProcessHandler::onAfterSolidBlocks);
        LodestoneRenderEvents.AFTER_LEVEL.register(ClientRuntimeEvents::renderStages);

        RenderHandler.onClientSetup();
    }

    public static void registerParticleFactory() {
        ParticleFactoryRegistry.getInstance().register(WISP_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SMOKE_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SPARKLE_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TWINKLE_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(STAR_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);

        ParticleFactoryRegistry.getInstance().register(SPARK_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);


        ParticleFactoryRegistry.getInstance().register(LodestoneParticleTypes.EXTRUDING_SPARK_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(LodestoneParticleTypes.THIN_EXTRUDING_SPARK_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);

        ParticleFactoryRegistry.getInstance().register(TERRAIN_PARTICLE.get(), s -> new team.lodestar.lodestone.systems.particle.world.type.LodestoneTerrainParticleType.Factory());
        ParticleFactoryRegistry.getInstance().register(ITEM_PARTICLE.get(), s -> new LodestoneItemCrumbsParticleType.Factory());
    }
}