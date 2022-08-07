package team.lodestar.lodestone.setup;

import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.rendering.particle.type.LodestoneParticleType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class LodestoneParticleRegistry {
    public static DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, LodestoneLib.LODESTONE);

    public static RegistryObject<LodestoneParticleType> WISP_PARTICLE = PARTICLES.register("wisp", LodestoneParticleType::new);
    public static RegistryObject<LodestoneParticleType> SMOKE_PARTICLE = PARTICLES.register("smoke", LodestoneParticleType::new);
    public static RegistryObject<LodestoneParticleType> SPARKLE_PARTICLE = PARTICLES.register("sparkle", LodestoneParticleType::new);
    public static RegistryObject<LodestoneParticleType> TWINKLE_PARTICLE = PARTICLES.register("twinkle", LodestoneParticleType::new);
    public static RegistryObject<LodestoneParticleType> STAR_PARTICLE = PARTICLES.register("star", LodestoneParticleType::new);

    public static void registerParticleFactory(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(WISP_PARTICLE.get(), LodestoneParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(SMOKE_PARTICLE.get(), LodestoneParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(SPARKLE_PARTICLE.get(), LodestoneParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(TWINKLE_PARTICLE.get(), LodestoneParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(STAR_PARTICLE.get(), LodestoneParticleType.Factory::new);
    }
}