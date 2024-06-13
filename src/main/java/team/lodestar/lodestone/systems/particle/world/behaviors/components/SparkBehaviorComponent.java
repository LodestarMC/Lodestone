package team.lodestar.lodestone.systems.particle.world.behaviors.components;

import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;

public class SparkBehaviorComponent implements LodestoneBehaviorComponent {

    private final GenericParticleData lengthData;

    public SparkBehaviorComponent(GenericParticleData lengthData) {
        this.lengthData = lengthData;
    }

    public SparkBehaviorComponent() {
        this(null);
    }

    public GenericParticleData getLengthData(LodestoneWorldParticle particle) {
        return lengthData != null ? lengthData : particle.scaleData;
    }

    @Override
    public LodestoneParticleBehavior getBehaviorType() {
        return LodestoneParticleBehavior.SPARK;
    }

    public GenericParticleData getLengthData() {
        return lengthData;
    }
}