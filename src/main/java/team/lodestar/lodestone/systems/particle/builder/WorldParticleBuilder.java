package team.lodestar.lodestone.systems.particle.builder;

import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.systems.particle.world.WorldParticleOptions;

import java.util.function.Supplier;

public class WorldParticleBuilder extends AbstractWorldParticleBuilder<WorldParticleBuilder, WorldParticleOptions> {

    final WorldParticleOptions options;

    public static WorldParticleBuilder create(Supplier<? extends ParticleType<WorldParticleOptions>> type) {
        return new WorldParticleBuilder(type.get());
    }

    protected WorldParticleBuilder(ParticleType<WorldParticleOptions> type) {
        super(type);
        this.options = new WorldParticleOptions(type);
    }

    @Override
    public WorldParticleOptions getParticleOptions() {
        return options;
    }
}