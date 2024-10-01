package team.lodestar.lodestone.registry.common.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleOptions;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleType;
import team.lodestar.lodestone.systems.particle.screen.LodestoneScreenParticleType;

import java.util.ArrayList;

public class LodestoneScreenParticleTypes {

    public static final ArrayList<ScreenParticleType<?>> PARTICLE_TYPES = new ArrayList<>();
    public static final ScreenParticleType<ScreenParticleOptions> WISP = registerType(new LodestoneScreenParticleType());
    public static final ScreenParticleType<ScreenParticleOptions> SMOKE = registerType(new LodestoneScreenParticleType());
    public static final ScreenParticleType<ScreenParticleOptions> SPARKLE = registerType(new LodestoneScreenParticleType());
    public static final ScreenParticleType<ScreenParticleOptions> TWINKLE = registerType(new LodestoneScreenParticleType());
    public static final ScreenParticleType<ScreenParticleOptions> STAR = registerType(new LodestoneScreenParticleType());

    public static void registerParticleFactory() {//TODO maybe use event?
        registerProvider(WISP, new LodestoneScreenParticleType.Factory(getSpriteSet(LodestoneLib.lodestonePath("wisp"))));
        registerProvider(SMOKE, new LodestoneScreenParticleType.Factory(getSpriteSet(LodestoneLib.lodestonePath("smoke"))));
        registerProvider(SPARKLE, new LodestoneScreenParticleType.Factory(getSpriteSet(LodestoneLib.lodestonePath("sparkle"))));
        registerProvider(TWINKLE, new LodestoneScreenParticleType.Factory(getSpriteSet(LodestoneLib.lodestonePath("twinkle"))));
        registerProvider(STAR, new LodestoneScreenParticleType.Factory(getSpriteSet(LodestoneLib.lodestonePath("star"))));
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