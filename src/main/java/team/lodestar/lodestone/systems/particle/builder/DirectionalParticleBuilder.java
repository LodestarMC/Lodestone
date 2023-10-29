package team.lodestar.lodestone.systems.particle.builder;

import net.minecraft.core.particles.*;
import net.minecraft.world.phys.*;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.options.*;

import java.util.function.*;

public class DirectionalParticleBuilder extends AbstractWorldParticleBuilder<DirectionalParticleBuilder, DirectionalParticleOptions> {

    final DirectionalParticleOptions options;

    public static DirectionalParticleBuilder create(Supplier<? extends ParticleType<DirectionalParticleOptions>> type) {
        return new DirectionalParticleBuilder(type.get());
    }

    protected DirectionalParticleBuilder(ParticleType<DirectionalParticleOptions> type) {
        super(type);
        this.options = new DirectionalParticleOptions(type);
    }

    @Override
    public DirectionalParticleOptions getParticleOptions() {
        return options;
    }

    public Vec3 getDirection() {
        return getParticleOptions().direction;
    }

    public DirectionalParticleBuilder setDirection(Vec3 direction) {
        getParticleOptions().direction = direction;
        return wrapper();
    }

}