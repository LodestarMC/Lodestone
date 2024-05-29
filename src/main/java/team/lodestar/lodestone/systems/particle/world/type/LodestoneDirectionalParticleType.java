package team.lodestar.lodestone.systems.particle.world.type;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import team.lodestar.lodestone.systems.particle.world.options.*;
import team.lodestar.lodestone.systems.particle.world.DirectionalParticle;

import javax.annotation.Nullable;

public class LodestoneDirectionalParticleType extends AbstractLodestoneParticleType<DirectionalParticleOptions<?>> {

    public LodestoneDirectionalParticleType() {
        super();
    }

    public static class Factory implements ParticleProvider<DirectionalParticleOptions<?>> {
        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(DirectionalParticleOptions data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            return new DirectionalParticle<>(world, data, (ParticleEngine.MutableSpriteSet) sprite, x, y, z, mx, my, mz);
        }
    }
}