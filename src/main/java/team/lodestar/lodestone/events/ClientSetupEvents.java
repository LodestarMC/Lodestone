package team.lodestar.lodestone.events;

import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.handlers.ThrowawayBlockDataHandler;
import team.lodestar.lodestone.handlers.screenparticle.ParticleEmitterHandler;
import team.lodestar.lodestone.registry.common.particle.*;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneItemCrumbsParticleType;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneTerrainParticleType;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

public class ClientSetupEvents {

    public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
        registerParticleProviders(event);
        LodestoneScreenParticleTypes.registerParticleFactory(event);
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        RenderHandler.onClientSetup(event);
        ParticleEmitterHandler.registerParticleEmitters(event);
        ThrowawayBlockDataHandler.setRenderLayers(event);
    }

    private static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(LodestoneParticleTypes.WISP_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(LodestoneParticleTypes.SMOKE_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(LodestoneParticleTypes.SPARKLE_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(LodestoneParticleTypes.TWINKLE_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(LodestoneParticleTypes.STAR_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(LodestoneParticleTypes.SPARK_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(LodestoneParticleTypes.EXTRUDING_SPARK_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(LodestoneParticleTypes.THIN_EXTRUDING_SPARK_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);


        event.registerSpriteSet(LodestoneParticleTypes.TERRAIN_PARTICLE.get(), s -> new LodestoneTerrainParticleType.Factory());
        event.registerSpriteSet(LodestoneParticleTypes.ITEM_PARTICLE.get(), s -> new LodestoneItemCrumbsParticleType.Factory());
    }
}