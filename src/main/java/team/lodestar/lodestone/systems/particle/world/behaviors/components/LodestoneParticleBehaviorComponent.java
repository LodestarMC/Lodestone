package team.lodestar.lodestone.systems.particle.world.behaviors.components;

import team.lodestar.lodestone.systems.particle.world.behaviors.*;

public interface LodestoneParticleBehaviorComponent {

    DirectionalParticleBehaviorComponent DIRECTIONAL = new DirectionalParticleBehaviorComponent();
    SparkParticleBehaviorComponent SPARK = new SparkParticleBehaviorComponent();

    LodestoneParticleBehavior getBehaviorType();
}
