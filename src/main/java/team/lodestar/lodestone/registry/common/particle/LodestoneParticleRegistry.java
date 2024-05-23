package team.lodestar.lodestone.registry.common.particle;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
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

    public static void registerParticleFactory() {
        ParticleFactoryRegistry.getInstance().register(WISP_PARTICLE.get(), LodestoneParticleType.Factory::new);
        
        ParticleFactoryRegistry.getInstance().register(WISP_PARTICLE.get(), LodestoneParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SMOKE_PARTICLE.get(), LodestoneParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SPARKLE_PARTICLE.get(), LodestoneParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TWINKLE_PARTICLE.get(), LodestoneParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(STAR_PARTICLE.get(), LodestoneParticleType.Factory::new);

        ParticleFactoryRegistry.getInstance().register(SPARK_PARTICLE.get(), LodestoneSparkParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TERRAIN_PARTICLE.get(), LodestoneTerrainParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ITEM_PARTICLE.get(), LodestoneItemCrumbsParticleType.Factory::new);
    }
}