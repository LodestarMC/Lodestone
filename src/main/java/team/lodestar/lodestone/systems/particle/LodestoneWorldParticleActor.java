package team.lodestar.lodestone.systems.particle;

import net.minecraft.world.phys.*;
import team.lodestar.lodestone.systems.particle.options.*;

public interface LodestoneWorldParticleActor<T extends AbstractWorldParticleOptions<T>> {

    Vec3 getParticlePosition();

    default LodestoneWorldParticleActor<T> setParticlePosition(Vec3 vec3) {
        return setParticlePosition(vec3.x, vec3.y, vec3.z);
    }

    LodestoneWorldParticleActor<T> setParticlePosition(double x, double y, double z);

    Vec3 getParticleSpeed();

    default LodestoneWorldParticleActor<T> setParticleSpeed(Vec3 vec3) {
        return setParticleSpeed(vec3.x, vec3.y, vec3.z);
    }

    LodestoneWorldParticleActor<T> setParticleSpeed(double x, double y, double z);

    int getParticleAge();

    int getParticleLifespan();
}
