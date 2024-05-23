package team.lodestar.lodestone.registry.common.particle;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.particle.type.LodestoneItemCrumbsParticleType;
import team.lodestar.lodestone.systems.particle.type.LodestoneParticleType;
import team.lodestar.lodestone.systems.particle.type.LodestoneSparkParticleType;
import team.lodestar.lodestone.systems.particle.type.LodestoneTerrainParticleType;

@SuppressWarnings("unused")
public class LodestoneParticleRegistry {
    public static LazyRegistrar<ParticleType<?>> PARTICLES = LazyRegistrar.create(BuiltInRegistries.PARTICLE_TYPE, LodestoneLib.LODESTONE);

    public static RegistryObject<LodestoneParticleType> WISP_PARTICLE = PARTICLES.register("wisp", LodestoneParticleType::new);
    public static RegistryObject<LodestoneParticleType> SMOKE_PARTICLE = PARTICLES.register("smoke", LodestoneParticleType::new);
    public static RegistryObject<LodestoneParticleType> SPARKLE_PARTICLE = PARTICLES.register("sparkle", LodestoneParticleType::new);
    public static RegistryObject<LodestoneParticleType> TWINKLE_PARTICLE = PARTICLES.register("twinkle", LodestoneParticleType::new);
    public static RegistryObject<LodestoneParticleType> STAR_PARTICLE = PARTICLES.register("star", LodestoneParticleType::new);

    public static RegistryObject<LodestoneSparkParticleType> SPARK_PARTICLE = PARTICLES.register("spark", LodestoneSparkParticleType::new);

    public static RegistryObject<LodestoneTerrainParticleType> TERRAIN_PARTICLE = PARTICLES.register("terrain", LodestoneTerrainParticleType::new);
    public static RegistryObject<LodestoneItemCrumbsParticleType> ITEM_PARTICLE = PARTICLES.register("item", LodestoneItemCrumbsParticleType::new);

    public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(WISP_PARTICLE.get(), LodestoneParticleType.Factory::new);
        event.registerSpriteSet(SMOKE_PARTICLE.get(), LodestoneParticleType.Factory::new);
        event.registerSpriteSet(SPARKLE_PARTICLE.get(), LodestoneParticleType.Factory::new);
        event.registerSpriteSet(TWINKLE_PARTICLE.get(), LodestoneParticleType.Factory::new);
        event.registerSpriteSet(STAR_PARTICLE.get(), LodestoneParticleType.Factory::new);

        event.registerSpriteSet(SPARK_PARTICLE.get(), LodestoneSparkParticleType.Factory::new);
        event.registerSpriteSet(TERRAIN_PARTICLE.get(), LodestoneTerrainParticleType.Factory::new);
        event.registerSpriteSet(ITEM_PARTICLE.get(), LodestoneItemCrumbsParticleType.Factory::new);
    }
}