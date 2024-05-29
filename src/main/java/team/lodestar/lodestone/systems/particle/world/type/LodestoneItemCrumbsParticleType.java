package team.lodestar.lodestone.systems.particle.world.type;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import team.lodestar.lodestone.systems.particle.world.options.*;
import team.lodestar.lodestone.systems.particle.world.LodestoneItemCrumbParticle;

import javax.annotation.Nullable;

public class LodestoneItemCrumbsParticleType extends AbstractLodestoneParticleType<LodestoneItemCrumbsParticleOptions<?>> {
    public LodestoneItemCrumbsParticleType() {
        super();
    }

    public static class Factory implements ParticleProvider<LodestoneItemCrumbsParticleOptions<?>> {

        public Factory() {
        }

        @Nullable
        @Override
        public Particle createParticle(LodestoneItemCrumbsParticleOptions data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            return new LodestoneItemCrumbParticle<>(world, data, x, y, z, mx, my, mz);
        }
    }
}