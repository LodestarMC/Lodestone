package team.lodestar.lodestone.systems.particle.builder;

import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.world.level.block.state.*;
import team.lodestar.lodestone.systems.particle.options.*;

import java.util.function.*;

public class TerrainParticleBuilder extends AbstractWorldParticleBuilder<TerrainParticleBuilder, LodestoneTerrainParticleOptions> {

    final LodestoneTerrainParticleOptions options;

    public static TerrainParticleBuilder create(Supplier<? extends ParticleType<LodestoneTerrainParticleOptions>> type, BlockState blockState, BlockPos blockPos) {
        return new TerrainParticleBuilder(type.get(), blockState, blockPos);
    }

    protected TerrainParticleBuilder(ParticleType<LodestoneTerrainParticleOptions> type, BlockState blockState, BlockPos blockPos) {
        super(type);
        this.options = new LodestoneTerrainParticleOptions(type, blockState, blockPos);
    }

    @Override
    public LodestoneTerrainParticleOptions getParticleOptions() {
        return options;
    }
}