package team.lodestar.lodestone.systems.particle.world.type;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
import team.lodestar.lodestone.systems.particle.world.options.*;

import org.jetbrains.annotations.Nullable;

public class LodestoneWorldParticleType extends AbstractLodestoneParticleType<WorldParticleOptions<?>> {
    public LodestoneWorldParticleType() {
        super();
    }

    public static class Factory implements ParticleProvider<WorldParticleOptions<?>> {
        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(WorldParticleOptions data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            return new LodestoneWorldParticle<>(world, data, (FabricSpriteProviderImpl) sprite, x, y, z, mx, my, mz);
        }
    }
}