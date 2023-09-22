package team.lodestar.lodestone.systems.particle.type;

import com.mojang.serialization.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;
import team.lodestar.lodestone.systems.particle.world.*;

import javax.annotation.*;

public class LodestoneSparkParticleType extends ParticleType<SparkParticleOptions> {
    public LodestoneSparkParticleType() {
        super(false, SparkParticleOptions.DESERIALIZER);
    }


    @Override
    public Codec<SparkParticleOptions> codec() {
        return SparkParticleOptions.sparkCodec(this);
    }

    public static class Factory implements ParticleProvider<SparkParticleOptions> {
        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(SparkParticleOptions data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            return new SparkParticle(world, data, (ParticleEngine.MutableSpriteSet) sprite, x, y, z, mx, my, mz);
        }
    }
}