package team.lodestar.lodestone.registry.common.particle;

import io.github.fabricators_of_create.porting_lib.util.DeferredRegister;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneItemCrumbsParticleType;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneTerrainParticleType;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class LodestoneParticleTypes {

    public static DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, LodestoneLib.LODESTONE);

    public static Supplier<LodestoneWorldParticleType> WISP_PARTICLE = PARTICLES.register("wisp", LodestoneWorldParticleType::new);
    public static Supplier<LodestoneWorldParticleType> SMOKE_PARTICLE = PARTICLES.register("smoke", LodestoneWorldParticleType::new);
    public static Supplier<LodestoneWorldParticleType> SPARKLE_PARTICLE = PARTICLES.register("sparkle", LodestoneWorldParticleType::new);
    public static Supplier<LodestoneWorldParticleType> TWINKLE_PARTICLE = PARTICLES.register("twinkle", LodestoneWorldParticleType::new);
    public static Supplier<LodestoneWorldParticleType> STAR_PARTICLE = PARTICLES.register("star", LodestoneWorldParticleType::new);
    public static Supplier<LodestoneWorldParticleType> SPARK_PARTICLE = PARTICLES.register("spark", LodestoneWorldParticleType::new);
    public static Supplier<LodestoneWorldParticleType> EXTRUDING_SPARK_PARTICLE = PARTICLES.register("extruding_spark", LodestoneWorldParticleType::new);
    public static Supplier<LodestoneWorldParticleType> THIN_EXTRUDING_SPARK_PARTICLE = PARTICLES.register("thin_extruding_spark", LodestoneWorldParticleType::new);

    public static Supplier<LodestoneTerrainParticleType> TERRAIN_PARTICLE = PARTICLES.register("terrain", LodestoneTerrainParticleType::new);
    public static Supplier<LodestoneItemCrumbsParticleType> ITEM_PARTICLE = PARTICLES.register("item", LodestoneItemCrumbsParticleType::new);


}