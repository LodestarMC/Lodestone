package team.lodestar.lodestone.systems.particle.world.options;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;

import java.util.function.*;

public class LodestoneTerrainParticleOptions extends WorldParticleOptions {

    public final BlockState blockState;
    public final BlockPos blockPos;

    public LodestoneTerrainParticleOptions(ParticleType<LodestoneTerrainParticleOptions> type, LodestoneParticleBehavior behavior, BlockState blockState, BlockPos blockPos) {
        super(type, behavior);
        this.blockState = blockState;
        this.blockPos = blockPos;
    }

    public LodestoneTerrainParticleOptions(ParticleType<LodestoneTerrainParticleOptions> type, BlockState blockState, BlockPos blockPos) {
        this(type, null, blockState, blockPos);
    }

    public LodestoneTerrainParticleOptions(ParticleType<LodestoneTerrainParticleOptions> type, LodestoneParticleBehavior behavior, BlockState blockState) {
        this(type, behavior, blockState, null);
    }

    public LodestoneTerrainParticleOptions(ParticleType<LodestoneTerrainParticleOptions> type, BlockState blockState) {
        this(type, blockState, null);
    }
}