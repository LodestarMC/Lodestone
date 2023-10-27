package team.lodestar.lodestone.systems.particle;

import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.particle.options.AbstractWorldParticleOptions;

public interface LodestoneWorldParticleActor {

    Vec3 getParticlePosition();

    default LodestoneWorldParticleActor setParticlePosition(Vec3 vec3) {
        return setParticlePosition(vec3.x, vec3.y, vec3.z);
    }

    LodestoneWorldParticleActor setParticlePosition(double x, double y, double z);

    Vec3 getParticleSpeed();

    default LodestoneWorldParticleActor setParticleMotion(Vec3 vec3) {
        return setParticleMotion(vec3.x, vec3.y, vec3.z);
    }

    LodestoneWorldParticleActor setParticleMotion(double x, double y, double z);

    int getParticleAge();

    int getParticleLifespan();
}