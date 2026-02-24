package team.lodestar.lodestone.registry.common.particle;

import net.minecraft.client.*;
import net.minecraft.client.particle.*;
import net.minecraft.resources.*;
import net.neoforged.neoforge.client.event.*;
import team.lodestar.lodestone.*;
import team.lodestar.lodestone.systems.particle.screen.*;

import java.util.*;

public class LodestoneScreenParticleTypes {

    public static final ArrayList<ScreenParticleType> PARTICLE_TYPES = new ArrayList<>();
    public static final ScreenParticleType WISP = registerType(new LodestoneScreenParticleType());
    public static final ScreenParticleType SMOKE = registerType(new LodestoneScreenParticleType());
    public static final ScreenParticleType SPARKLE = registerType(new LodestoneScreenParticleType());
    public static final ScreenParticleType TWINKLE = registerType(new LodestoneScreenParticleType());
    public static final ScreenParticleType STAR = registerType(new LodestoneScreenParticleType());

    public static void registerParticleFactory(RegisterParticleProvidersEvent event) {//TODO maybe use event?
        registerProvider(WISP, new LodestoneScreenParticleType.Factory(getSpriteSet(LodestoneLib.lodestonePath("wisp"))));
        registerProvider(SMOKE, new LodestoneScreenParticleType.Factory(getSpriteSet(LodestoneLib.lodestonePath("smoke"))));
        registerProvider(SPARKLE, new LodestoneScreenParticleType.Factory(getSpriteSet(LodestoneLib.lodestonePath("sparkle"))));
        registerProvider(TWINKLE, new LodestoneScreenParticleType.Factory(getSpriteSet(LodestoneLib.lodestonePath("twinkle"))));
        registerProvider(STAR, new LodestoneScreenParticleType.Factory(getSpriteSet(LodestoneLib.lodestonePath("star"))));
    }

    public static ScreenParticleType registerType(ScreenParticleType type) {
        PARTICLE_TYPES.add(type);
        return type;
    }

    public static void registerProvider(ScreenParticleType type, ScreenParticleType.ScreenParticleProvider provider) {
        type.provider = provider;
    }

    public static SpriteSet getSpriteSet(ResourceLocation resourceLocation) {
        return Minecraft.getInstance().particleEngine.spriteSets.get(resourceLocation);
    }
}