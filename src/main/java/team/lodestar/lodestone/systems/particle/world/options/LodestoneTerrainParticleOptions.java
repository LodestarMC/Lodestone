package team.lodestar.lodestone.systems.particle.world.options;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;

import java.util.function.*;

public class LodestoneTerrainParticleOptions<T extends LodestoneParticleBehavior<T>> extends WorldParticleOptions<T> {

    public final BlockState blockState;
    public final BlockPos blockPos;

    public LodestoneTerrainParticleOptions(Supplier<ParticleType<?>> type, T behavior, BlockState blockState, BlockPos blockPos) {
        super(type, behavior);
        this.blockState = blockState;
        this.blockPos = blockPos;
    }

    public LodestoneTerrainParticleOptions(Supplier<ParticleType<?>> type, BlockState blockState, BlockPos blockPos) {
        this(type, null, blockState, blockPos);
    }

    public LodestoneTerrainParticleOptions(Supplier<ParticleType<?>> type, T behavior, BlockState blockState) {
        this(type, behavior, blockState, null);
    }

    public LodestoneTerrainParticleOptions(Supplier<ParticleType<?>> type, BlockState blockState) {
        this(type, blockState, null);
    }
}