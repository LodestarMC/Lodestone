package team.lodestar.lodestone.systems.particle.world.behaviors.components;

import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;

public class SparkParticleBehaviorComponent implements LodestoneParticleBehaviorComponent {

    private final GenericParticleData lengthData;

    public SparkParticleBehaviorComponent(GenericParticleData lengthData) {
        this.lengthData = lengthData;
    }

    public SparkParticleBehaviorComponent() {
        this(null);
    }

    public GenericParticleData getLengthData(LodestoneWorldParticle particle) {
        return lengthData != null ? lengthData : particle.scaleData;
    }
}
