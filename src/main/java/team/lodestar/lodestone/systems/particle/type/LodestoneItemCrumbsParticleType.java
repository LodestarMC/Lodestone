package team.lodestar.lodestone.systems.particle.type;

import com.mojang.serialization.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;
import team.lodestar.lodestone.systems.particle.options.*;
import team.lodestar.lodestone.systems.particle.world.*;

import javax.annotation.*;

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