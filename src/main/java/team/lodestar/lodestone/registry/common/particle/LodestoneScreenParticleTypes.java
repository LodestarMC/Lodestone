package team.lodestar.lodestone.registry.common.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.modules.rendering.particle.standard.screen.ScreenParticleConfig;
import team.lodestar.lodestone.modules.rendering.particle.standard.screen.ScreenParticleType;
import team.lodestar.lodestone.modules.rendering.particle.standard.screen.LodestoneScreenParticleType;

import java.util.ArrayList;

public class LodestoneScreenParticleTypes {

    public static final ArrayList<ScreenParticleType<?>> PARTICLE_TYPES = new ArrayList<>();
    public static final ScreenParticleType<ScreenParticleConfig> WISP = registerType(new LodestoneScreenParticleType());
    public static final ScreenParticleType<ScreenParticleConfig> SMOKE = registerType(new LodestoneScreenParticleType());
    public static final ScreenParticleType<ScreenParticleConfig> SPARKLE = registerType(new LodestoneScreenParticleType());
    public static final ScreenParticleType<ScreenParticleConfig> TWINKLE = registerType(new LodestoneScreenParticleType());
    public static final ScreenParticleType<ScreenParticleConfig> STAR = registerType(new LodestoneScreenParticleType());

    public static void registerParticleFactory(RegisterParticleProvidersEvent event) {//TODO maybe use event?
        registerProvider(WISP, new LodestoneScreenParticleType.Factory(getSpriteSet(LodestoneLib.lodestonePath("wisp"))));
        registerProvider(SMOKE, new LodestoneScreenParticleType.Factory(getSpriteSet(LodestoneLib.lodestonePath("smoke"))));
        registerProvider(SPARKLE, new LodestoneScreenParticleType.Factory(getSpriteSet(LodestoneLib.lodestonePath("sparkle"))));
        registerProvider(TWINKLE, new LodestoneScreenParticleType.Factory(getSpriteSet(LodestoneLib.lodestonePath("twinkle"))));
        registerProvider(STAR, new LodestoneScreenParticleType.Factory(getSpriteSet(LodestoneLib.lodestonePath("star"))));
    }

    public static <T extends ScreenParticleConfig> ScreenParticleType<T> registerType(ScreenParticleType<T> type) {
        PARTICLE_TYPES.add(type);
        return type;
    }

    public static <T extends ScreenParticleConfig> void registerProvider(ScreenParticleType<T> type, ScreenParticleType.ParticleProvider<T> provider) {
        type.provider = provider;
    }

    public static SpriteSet getSpriteSet(ResourceLocation resourceLocation) {
        return Minecraft.getInstance().particleEngine.spriteSets.get(resourceLocation);
    }
}