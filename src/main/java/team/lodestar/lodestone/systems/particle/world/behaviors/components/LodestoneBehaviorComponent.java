package team.lodestar.lodestone.systems.particle.world.behaviors.components;

import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
import team.lodestar.lodestone.systems.particle.world.behaviors.LodestoneParticleBehavior;

public interface LodestoneBehaviorComponent {

    DirectionalBehaviorComponent DIRECTIONAL = new DirectionalBehaviorComponent();
    SparkBehaviorComponent SPARK = new SparkBehaviorComponent();
    ExtrudingSparkBehaviorComponent EXTRUDING_SPARK = new ExtrudingSparkBehaviorComponent();

    LodestoneParticleBehavior getBehaviorType();

    default void tick(LodestoneWorldParticle particle) {

    }
}
