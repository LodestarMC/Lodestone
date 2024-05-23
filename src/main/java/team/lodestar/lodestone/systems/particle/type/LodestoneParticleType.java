package team.lodestar.lodestone.systems.particle.type;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import team.lodestar.lodestone.systems.particle.options.WorldParticleOptions;
import team.lodestar.lodestone.systems.particle.world.GenericParticle;

import org.jetbrains.annotations.Nullable;

public class LodestoneParticleType extends ParticleType<WorldParticleOptions> {
    public LodestoneParticleType() {
        super(false, WorldParticleOptions.DESERIALIZER);
    }


    @Override
    public Codec<WorldParticleOptions> codec() {
        return WorldParticleOptions.worldCodec(this);
    }

    public static class Factory implements ParticleProvider<WorldParticleOptions> {
        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(WorldParticleOptions data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            return new GenericParticle(world, data, (FabricSpriteProviderImpl) sprite, x, y, z, mx, my, mz);
        }
    }
}