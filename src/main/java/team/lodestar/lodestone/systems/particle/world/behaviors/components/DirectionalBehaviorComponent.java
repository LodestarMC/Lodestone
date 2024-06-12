package team.lodestar.lodestone.systems.particle.world.behaviors.components;

import net.minecraft.world.phys.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;

import java.util.function.*;

public class DirectionalBehaviorComponent implements LodestoneBehaviorComponent {

    private final Function<LodestoneWorldParticle, Vec3> direction;

    protected DirectionalBehaviorComponent(Function<LodestoneWorldParticle, Vec3> direction) {
        this.direction = direction;
    }

    public DirectionalBehaviorComponent(Vec3 direction) {
        this(p -> direction);
    }

    public DirectionalBehaviorComponent() {
        this(p -> p.getParticleSpeed().normalize());
    }

    public Vec3 getDirection(LodestoneWorldParticle particle) {
        return direction.apply(particle);
    }

    @Override
    public LodestoneParticleBehavior getBehaviorType() {
        return LodestoneParticleBehavior.DIRECTIONAL;
    }
}