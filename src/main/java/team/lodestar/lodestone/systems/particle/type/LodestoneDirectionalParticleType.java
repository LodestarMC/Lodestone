package team.lodestar.lodestone.systems.particle.type;

import com.mojang.serialization.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;
import team.lodestar.lodestone.systems.particle.options.*;
import team.lodestar.lodestone.systems.particle.world.*;

import javax.annotation.*;

public class LodestoneDirectionalParticleType extends ParticleType<DirectionalParticleOptions> {
    public LodestoneDirectionalParticleType() {
        super(false, DirectionalParticleOptions.DESERIALIZER);
    }


    @Override
    public Codec<DirectionalParticleOptions> codec() {
        return DirectionalParticleOptions.directionalCodec(this);
    }

    public static class Factory implements ParticleProvider<DirectionalParticleOptions> {
        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(DirectionalParticleOptions data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            return new DirectionalParticle(world, data, (ParticleEngine.MutableSpriteSet) sprite, x, y, z, mx, my, mz);
        }
    }
}