package com.sammy.ortus.setup;

import com.sammy.ortus.OrtusLib;
import com.sammy.ortus.systems.rendering.particle.type.OrtusParticleType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class OrtusParticleRegistry {
    public static DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, OrtusLib.ORTUS);

    public static RegistryObject<OrtusParticleType> WISP_PARTICLE = PARTICLES.register("wisp", OrtusParticleType::new);
    public static RegistryObject<OrtusParticleType> SMOKE_PARTICLE = PARTICLES.register("smoke", OrtusParticleType::new);
    public static RegistryObject<OrtusParticleType> SPARKLE_PARTICLE = PARTICLES.register("sparkle", OrtusParticleType::new);
    public static RegistryObject<OrtusParticleType> TWINKLE_PARTICLE = PARTICLES.register("twinkle", OrtusParticleType::new);
    public static RegistryObject<OrtusParticleType> STAR_PARTICLE = PARTICLES.register("star", OrtusParticleType::new);

    public static void registerParticleFactory(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(WISP_PARTICLE.get(), OrtusParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(SMOKE_PARTICLE.get(), OrtusParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(SPARKLE_PARTICLE.get(), OrtusParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(TWINKLE_PARTICLE.get(), OrtusParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(STAR_PARTICLE.get(), OrtusParticleType.Factory::new);
    }
}