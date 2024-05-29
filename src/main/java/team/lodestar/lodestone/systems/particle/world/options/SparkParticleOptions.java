package team.lodestar.lodestone.systems.particle.world.options;

import net.minecraft.core.particles.ParticleType;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;

public class SparkParticleOptions<T extends LodestoneParticleBehavior<T>> extends WorldParticleOptions<T> {

    public GenericParticleData lengthData = DEFAULT_GENERIC;

    public SparkParticleOptions(ParticleType<?> type, T behavior) {
        super(type, behavior);
    }
}