package team.lodestar.lodestone.systems.particle;

import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.particle.options.AbstractWorldParticleOptions;

public interface LodestoneWorldParticleActor<T extends AbstractWorldParticleOptions<T>> {

    Vec3 getPosition();

    default LodestoneWorldParticleActor<T> setPosition(Vec3 vec3) {
        return setPosition(vec3.x, vec3.y, vec3.z);
    }

    LodestoneWorldParticleActor<T> setPosition(double x, double y, double z);

    Vec3 getMotion();

    default LodestoneWorldParticleActor<T> setMotion(Vec3 vec3) {
        return setMotion(vec3.x, vec3.y, vec3.z);
    }

    LodestoneWorldParticleActor<T> setMotion(double x, double y, double z);

    int getParticleAge();

    int getParticleLifespan();
}
