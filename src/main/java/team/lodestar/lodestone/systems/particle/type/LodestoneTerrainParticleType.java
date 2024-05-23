package team.lodestar.lodestone.systems.particle.type;

import com.mojang.serialization.Codec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import team.lodestar.lodestone.systems.particle.options.LodestoneTerrainParticleOptions;
import team.lodestar.lodestone.systems.particle.world.LodestoneTerrainParticle;

import org.jetbrains.annotations.Nullable;

public class LodestoneTerrainParticleType extends ParticleType<LodestoneTerrainParticleOptions> {
    public LodestoneTerrainParticleType() {
        super(false, LodestoneTerrainParticleOptions.DESERIALIZER);
    }

    @Override
    public Codec<LodestoneTerrainParticleOptions> codec() {
        return LodestoneTerrainParticleOptions.terrainCodec(this);
    }

    public static class Factory implements ParticleProvider<LodestoneTerrainParticleOptions> {

        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(LodestoneTerrainParticleOptions data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            return new LodestoneTerrainParticle(world, data, x, y, z, mx, my, mz);
        }
    }
}