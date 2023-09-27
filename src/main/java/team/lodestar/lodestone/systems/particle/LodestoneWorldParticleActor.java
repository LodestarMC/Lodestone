package team.lodestar.lodestone.systems.particle;

import net.minecraft.world.phys.*;
import team.lodestar.lodestone.systems.particle.options.*;

public interface LodestoneWorldParticleActor<T extends AbstractWorldParticleOptions> {

    Vec3 getPosition();

    LodestoneWorldParticleActor<T> setPosition();

    Vec3 getMotion();

    LodestoneWorldParticleActor<T> setMotion();

    int getParticleAge();

    T getParticleOptions();
}
