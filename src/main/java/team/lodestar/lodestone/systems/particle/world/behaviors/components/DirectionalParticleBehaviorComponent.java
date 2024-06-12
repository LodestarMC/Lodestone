package team.lodestar.lodestone.systems.particle.world.behaviors.components;

import net.minecraft.world.phys.*;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;

import java.util.function.*;

public class DirectionalParticleBehaviorComponent implements LodestoneParticleBehaviorComponent {

    private final Function<LodestoneWorldParticle, Vec3> direction;

    public DirectionalParticleBehaviorComponent(Vec3 direction) {
        this.direction = p -> direction;
    }

    public DirectionalParticleBehaviorComponent() {
        this.direction = p -> p.getParticleSpeed().normalize();
    }

    public Vec3 getDirection(LodestoneWorldParticle particle) {
        return direction.apply(particle);
    }

    @Override
    public LodestoneParticleBehavior getBehaviorType() {
        return LodestoneParticleBehavior.DIRECTIONAL;
    }
}
