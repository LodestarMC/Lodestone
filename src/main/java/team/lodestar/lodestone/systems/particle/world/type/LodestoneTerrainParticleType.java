package team.lodestar.lodestone.systems.particle.world.type;

import com.mojang.serialization.Codec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import team.lodestar.lodestone.systems.particle.world.options.*;
import team.lodestar.lodestone.systems.particle.world.LodestoneTerrainParticle;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;

import javax.annotation.Nullable;

public class LodestoneTerrainParticleType extends AbstractLodestoneParticleType<LodestoneTerrainParticleOptions<?>> {
    public LodestoneTerrainParticleType() {
        super();
    }

    public static class Factory implements ParticleProvider<LodestoneTerrainParticleOptions<?>> {

        public Factory() {
        }

        @Nullable
        @Override
        public Particle createParticle(LodestoneTerrainParticleOptions<?> data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            return new LodestoneTerrainParticle<>(world, data, x, y, z, mx, my, mz);
        }
    }
}