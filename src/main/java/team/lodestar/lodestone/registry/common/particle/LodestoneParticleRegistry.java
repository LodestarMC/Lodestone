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
import team.lodestar.lodestone.systems.particle.world.type.LodestoneSparkParticleType;
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
    public static final RegistryObject<LodestoneTerrainParticleType> TERRAIN_PARTICLE = PARTICLES.register("terrain" , LodestoneTerrainParticleType::new);
    public static final RegistryObject<LodestoneItemCrumbsParticleType> ITEM_PARTICLE = PARTICLES.register("item" , LodestoneItemCrumbsParticleType::new);


    @Environment(EnvType.CLIENT)
    public static void registerParticleFactory() {
        ParticleFactoryRegistry.getInstance().register(WISP_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SMOKE_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SPARKLE_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TWINKLE_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(STAR_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);

        ParticleFactoryRegistry.getInstance().register(SPARK_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TERRAIN_PARTICLE.get(), s -> new team.lodestar.lodestone.systems.particle.world.type.LodestoneTerrainParticleType.Factory());
        ParticleFactoryRegistry.getInstance().register(ITEM_PARTICLE.get(), s -> new LodestoneItemCrumbsParticleType.Factory());
    }
}