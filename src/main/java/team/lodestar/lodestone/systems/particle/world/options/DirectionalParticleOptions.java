package team.lodestar.lodestone.systems.particle.world.options;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;

public class DirectionalParticleOptions<T extends LodestoneParticleBehavior<T>> extends WorldParticleOptions<T> {

    public Vec3 direction = new Vec3(0, 1, 0);

    public DirectionalParticleOptions(ParticleType<?> type, T behavior) {
        super(type, behavior);
    }
}