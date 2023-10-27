package team.lodestar.lodestone.systems.particle.type;

import com.mojang.serialization.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;
import team.lodestar.lodestone.systems.particle.options.*;
import team.lodestar.lodestone.systems.particle.world.LodestoneTerrainParticle;

import javax.annotation.*;

public class LodestoneTerrainParticleType extends ParticleType<LodestoneTerrainParticleOptions> {
    public LodestoneTerrainParticleType() {
        super(false, LodestoneTerrainParticleOptions.DESERIALIZER);
    }

    @Override
    public Codec<LodestoneTerrainParticleOptions> codec() {
        return LodestoneTerrainParticleOptions.terrainCodec(this);
    }

    public static class Factory implements ParticleProvider<LodestoneTerrainParticleOptions> {

        public Factory() {
        }

        @Nullable
        @Override
        public Particle createParticle(LodestoneTerrainParticleOptions data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            return new LodestoneTerrainParticle(world, data, x, y, z, mx, my, mz);
        }
    }
}