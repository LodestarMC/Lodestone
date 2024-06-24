package team.lodestar.lodestone.registry.common.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneItemCrumbsParticleType;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneTerrainParticleType;

@SuppressWarnings("unused")
public class LodestoneParticleRegistry {
    public static DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, LodestoneLib.LODESTONE);

    public static RegistryObject<LodestoneWorldParticleType> WISP_PARTICLE = PARTICLES.register("wisp", LodestoneWorldParticleType::new);
    public static RegistryObject<LodestoneWorldParticleType> SMOKE_PARTICLE = PARTICLES.register("smoke", LodestoneWorldParticleType::new);
    public static RegistryObject<LodestoneWorldParticleType> SPARKLE_PARTICLE = PARTICLES.register("sparkle", LodestoneWorldParticleType::new);
    public static RegistryObject<LodestoneWorldParticleType> TWINKLE_PARTICLE = PARTICLES.register("twinkle", LodestoneWorldParticleType::new);
    public static RegistryObject<LodestoneWorldParticleType> STAR_PARTICLE = PARTICLES.register("star", LodestoneWorldParticleType::new);
    public static RegistryObject<LodestoneWorldParticleType> SPARK_PARTICLE = PARTICLES.register("spark", LodestoneWorldParticleType::new);
    public static RegistryObject<LodestoneWorldParticleType> EXTRUDING_SPARK_PARTICLE = PARTICLES.register("extruding_spark", LodestoneWorldParticleType::new);
    public static RegistryObject<LodestoneWorldParticleType> THIN_EXTRUDING_SPARK_PARTICLE = PARTICLES.register("thin_extruding_spark", LodestoneWorldParticleType::new);

    public static RegistryObject<LodestoneTerrainParticleType> TERRAIN_PARTICLE = PARTICLES.register("terrain", LodestoneTerrainParticleType::new);
    public static RegistryObject<LodestoneItemCrumbsParticleType> ITEM_PARTICLE = PARTICLES.register("item", LodestoneItemCrumbsParticleType::new);


}