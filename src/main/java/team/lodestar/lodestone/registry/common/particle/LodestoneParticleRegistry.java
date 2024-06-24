package team.lodestar.lodestone.registry.common.particle;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneItemCrumbsParticleType;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneTerrainParticleType;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

@SuppressWarnings("unused")
public class LodestoneParticleRegistry {
    public static LazyRegistrar<ParticleType<?>> PARTICLES = LazyRegistrar.create(BuiltInRegistries.PARTICLE_TYPE, LodestoneLib.LODESTONE);

    public static final RegistryObject<LodestoneWorldParticleType> WISP_PARTICLE = PARTICLES.register("wisp", LodestoneWorldParticleType::new);
    public static final RegistryObject<LodestoneWorldParticleType> SMOKE_PARTICLE = PARTICLES.register("smoke", LodestoneWorldParticleType::new);
    public static final RegistryObject<LodestoneWorldParticleType> SPARKLE_PARTICLE = PARTICLES.register("sparkle", LodestoneWorldParticleType::new);
    public static final RegistryObject<LodestoneWorldParticleType> TWINKLE_PARTICLE = PARTICLES.register("twinkle", LodestoneWorldParticleType::new);
    public static final RegistryObject<LodestoneWorldParticleType> STAR_PARTICLE = PARTICLES.register("star", LodestoneWorldParticleType::new);
    public static final RegistryObject<LodestoneWorldParticleType> SPARK_PARTICLE = PARTICLES.register("spark", LodestoneWorldParticleType::new);
    public static final RegistryObject<LodestoneWorldParticleType> EXTRUDING_SPARK_PARTICLE = PARTICLES.register("extruding_spark", LodestoneWorldParticleType::new);
    public static final RegistryObject<LodestoneWorldParticleType> THIN_EXTRUDING_SPARK_PARTICLE = PARTICLES.register("thin_extruding_spark", LodestoneWorldParticleType::new);

    public static final RegistryObject<LodestoneTerrainParticleType> TERRAIN_PARTICLE = PARTICLES.register("terrain" , LodestoneTerrainParticleType::new);
    public static final RegistryObject<LodestoneItemCrumbsParticleType> ITEM_PARTICLE = PARTICLES.register("item" , LodestoneItemCrumbsParticleType::new);
}

/*
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

 */