package team.lodestar.lodestone.systems.particle.builder;

import net.minecraft.core.particles.*;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.options.*;

import java.util.function.*;

public class SparkParticleBuilder extends AbstractWorldParticleBuilder<SparkParticleBuilder, SparkParticleOptions> {

    final SparkParticleOptions options;

    public static SparkParticleBuilder create(Supplier<? extends ParticleType<SparkParticleOptions>> type) {
        return new SparkParticleBuilder(type.get());
    }

    protected SparkParticleBuilder(ParticleType<SparkParticleOptions> type) {
        super(type);
        this.options = new SparkParticleOptions(type);
    }

    @Override
    public SparkParticleOptions getParticleOptions() {
        return options;
    }

    public GenericParticleData getLengthData() {
        return getParticleOptions().lengthData;
    }

    public SparkParticleBuilder setLengthData(GenericParticleData lengthData) {
        getParticleOptions().lengthData = lengthData;
        return wrapper();
    }

}