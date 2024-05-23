package team.lodestar.lodestone.registry.common.particle;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.helpers.DataHelper;
import team.lodestar.lodestone.systems.particle.type.LodestoneItemCrumbsParticleType;
import team.lodestar.lodestone.systems.particle.type.LodestoneParticleType;
import team.lodestar.lodestone.systems.particle.type.LodestoneSparkParticleType;
import team.lodestar.lodestone.systems.particle.type.LodestoneTerrainParticleType;

import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public class LodestoneParticleRegistry {
    //public static LazyRegistrar<ParticleType<?>> PARTICLES = LazyRegistrar.create(BuiltInRegistries.PARTICLE_TYPE, LodestoneLib.LODESTONE);

    public static final LodestoneParticleType WISP_PARTICLE = new LodestoneParticleType();
    public static final LodestoneParticleType SMOKE_PARTICLE = new LodestoneParticleType();
    public static final LodestoneParticleType SPARKLE_PARTICLE = new LodestoneParticleType();
    public static final LodestoneParticleType TWINKLE_PARTICLE = new LodestoneParticleType();
    public static final LodestoneParticleType STAR_PARTICLE = new LodestoneParticleType();


    public static final LodestoneSparkParticleType SPARK_PARTICLE = new LodestoneSparkParticleType();
    public static final LodestoneTerrainParticleType TERRAIN_PARTICLE = new LodestoneTerrainParticleType();
    public static final LodestoneItemCrumbsParticleType ITEM_PARTICLE = new LodestoneItemCrumbsParticleType();

    public static void init() {
        initParticles(bind(BuiltInRegistries.PARTICLE_TYPE));
    }

    private static <T> BiConsumer<T, ResourceLocation> bind(Registry<? super T> registry) {
        return (t, id) -> Registry.register(registry, id, t);
    }

    private static void initParticles(BiConsumer<ParticleType<?>, ResourceLocation> registry) {
        registry.accept(WISP_PARTICLE, LodestoneLib.lodestonePath("wisp"));
        registry.accept(SMOKE_PARTICLE, LodestoneLib.lodestonePath("smoke"));
        registry.accept(SPARKLE_PARTICLE, LodestoneLib.lodestonePath("sparkle"));
        registry.accept(TWINKLE_PARTICLE, LodestoneLib.lodestonePath("twinkle"));
        registry.accept(STAR_PARTICLE, LodestoneLib.lodestonePath("star"));
    }

    public static void registerFactories() {
        ParticleFactoryRegistry.getInstance().register(WISP_PARTICLE, LodestoneParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SMOKE_PARTICLE, LodestoneParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SPARKLE_PARTICLE, LodestoneParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TWINKLE_PARTICLE, LodestoneParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(STAR_PARTICLE, LodestoneParticleType.Factory::new);

        ParticleFactoryRegistry.getInstance().register(SPARK_PARTICLE, LodestoneSparkParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TERRAIN_PARTICLE, LodestoneTerrainParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ITEM_PARTICLE, LodestoneItemCrumbsParticleType.Factory::new);
    }

    /*
    @Environment(EnvType.CLIENT)
    public static void registerParticleFactory() {
        ParticleFactoryRegistry.getInstance().register(WISP_PARTICLE.get(), LodestoneParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SMOKE_PARTICLE.get(), LodestoneParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SPARKLE_PARTICLE.get(), LodestoneParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TWINKLE_PARTICLE.get(), LodestoneParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(STAR_PARTICLE.get(), LodestoneParticleType.Factory::new);

        ParticleFactoryRegistry.getInstance().register(SPARK_PARTICLE.get(), LodestoneSparkParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TERRAIN_PARTICLE.get(), LodestoneTerrainParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ITEM_PARTICLE.get(), LodestoneItemCrumbsParticleType.Factory::new);
    }

     */
}