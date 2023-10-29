package team.lodestar.lodestone.systems.particle.type;

import com.mojang.serialization.Codec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import team.lodestar.lodestone.systems.particle.options.LodestoneItemCrumbsParticleOptions;
import team.lodestar.lodestone.systems.particle.world.LodestoneItemCrumbParticle;

import javax.annotation.Nullable;

public class LodestoneItemCrumbsParticleType extends ParticleType<LodestoneItemCrumbsParticleOptions> {
    public LodestoneItemCrumbsParticleType() {
        super(false, LodestoneItemCrumbsParticleOptions.DESERIALIZER);
    }

    @Override
    public Codec<LodestoneItemCrumbsParticleOptions> codec() {
        return LodestoneItemCrumbsParticleOptions.brokenItemCodec(this);
    }

    public static class Factory implements ParticleProvider<LodestoneItemCrumbsParticleOptions> {

        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(LodestoneItemCrumbsParticleOptions data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            return new LodestoneItemCrumbParticle(world, data, x, y, z, mx, my, mz);
        }
    }
}