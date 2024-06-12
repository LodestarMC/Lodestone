package team.lodestar.lodestone.systems.particle.world.options;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;
import team.lodestar.lodestone.systems.particle.world.type.*;

import java.util.function.*;

public class LodestoneTerrainParticleOptions extends WorldParticleOptions {

    public final BlockState blockState;
    public final BlockPos blockPos;

    public LodestoneTerrainParticleOptions(ParticleType<LodestoneTerrainParticleOptions> type, BlockState blockState, BlockPos blockPos) {
        super(type);
        this.blockState = blockState;
        this.blockPos = blockPos;
    }

    public LodestoneTerrainParticleOptions(RegistryObject<? extends LodestoneTerrainParticleType> type, BlockState blockState, BlockPos blockPos) {
        this(type.get(), blockState, blockPos);
    }

    public LodestoneTerrainParticleOptions(ParticleType<LodestoneTerrainParticleOptions> type, BlockState blockState) {
        this(type, blockState, null);
    }

    public LodestoneTerrainParticleOptions(RegistryObject<? extends LodestoneTerrainParticleType> type, BlockState blockState) {
        this(type.get(), blockState);
    }
}