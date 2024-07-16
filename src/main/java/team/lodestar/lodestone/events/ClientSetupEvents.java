package team.lodestar.lodestone.events;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.handlers.ThrowawayBlockDataHandler;
import team.lodestar.lodestone.handlers.screenparticle.ParticleEmitterHandler;
import team.lodestar.lodestone.registry.common.particle.*;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneItemCrumbsParticleType;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneTerrainParticleType;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientSetupEvents {

    @SubscribeEvent
    public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
        registerParticleProviders(event);
        LodestoneScreenParticleRegistry.registerParticleFactory(event);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        RenderHandler.onClientSetup(event);
        ParticleEmitterHandler.registerParticleEmitters(event);
        ThrowawayBlockDataHandler.setRenderLayers(event);
    }

    private static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(LodestoneParticleRegistry.WISP_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(LodestoneParticleRegistry.SMOKE_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(LodestoneParticleRegistry.SPARKLE_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(LodestoneParticleRegistry.TWINKLE_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(LodestoneParticleRegistry.STAR_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(LodestoneParticleRegistry.SPARK_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(LodestoneParticleRegistry.EXTRUDING_SPARK_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(LodestoneParticleRegistry.THIN_EXTRUDING_SPARK_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);


        event.registerSpriteSet(LodestoneParticleRegistry.TERRAIN_PARTICLE.get(), s -> new LodestoneTerrainParticleType.Factory());
        event.registerSpriteSet(LodestoneParticleRegistry.ITEM_PARTICLE.get(), s -> new LodestoneItemCrumbsParticleType.Factory());
    }
}