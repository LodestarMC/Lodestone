package com.sammy.ortus.setup;

import com.sammy.ortus.OrtusLib;
import com.sammy.ortus.systems.rendering.particle.screen.ScreenParticleOptions;
import com.sammy.ortus.systems.rendering.particle.screen.ScreenParticleType;
import com.sammy.ortus.systems.rendering.particle.type.OrtusScreenParticleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;

import java.util.ArrayList;

public class OrtusScreenParticleRegistry {

    public static final ArrayList<ScreenParticleType<?>> PARTICLE_TYPES = new ArrayList<>();
    public static final ScreenParticleType<ScreenParticleOptions> WISP = registerType(new OrtusScreenParticleType());
    public static final ScreenParticleType<ScreenParticleOptions> SMOKE = registerType(new OrtusScreenParticleType());
    public static final ScreenParticleType<ScreenParticleOptions> SPARKLE = registerType(new OrtusScreenParticleType());
    public static final ScreenParticleType<ScreenParticleOptions> TWINKLE = registerType(new OrtusScreenParticleType());
    public static final ScreenParticleType<ScreenParticleOptions> STAR = registerType(new OrtusScreenParticleType());

    public static void registerParticleFactory(ParticleFactoryRegisterEvent event) {
        registerProvider(WISP, new OrtusScreenParticleType.Factory(getSpriteSet(OrtusLib.prefix("wisp"))));
        registerProvider(SMOKE, new OrtusScreenParticleType.Factory(getSpriteSet(OrtusLib.prefix("smoke"))));
        registerProvider(SPARKLE, new OrtusScreenParticleType.Factory(getSpriteSet(OrtusLib.prefix("sparkle"))));
        registerProvider(TWINKLE, new OrtusScreenParticleType.Factory(getSpriteSet(OrtusLib.prefix("twinkle"))));
        registerProvider(STAR, new OrtusScreenParticleType.Factory(getSpriteSet(OrtusLib.prefix("star"))));
    }

    public static <T extends ScreenParticleOptions> ScreenParticleType<T> registerType(ScreenParticleType<T> type) {
        PARTICLE_TYPES.add(type);
        return type;
    }

    public static <T extends ScreenParticleOptions> void registerProvider(ScreenParticleType<T> type, ScreenParticleType.ParticleProvider<T> provider) {
        type.provider = provider;
    }

    public static SpriteSet getSpriteSet(ResourceLocation resourceLocation) {
        return Minecraft.getInstance().particleEngine.spriteSets.get(resourceLocation);
    }
}