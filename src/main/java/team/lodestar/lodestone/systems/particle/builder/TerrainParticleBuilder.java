package team.lodestar.lodestone.systems.particle.builder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.lodestone.systems.particle.options.LodestoneTerrainParticleOptions;

import java.util.function.Supplier;

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