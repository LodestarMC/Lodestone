//package team.lodestar.lodestone.modules.rendering.particle.standard.old;
//
//import net.minecraft.core.*;
//import net.minecraft.core.particles.*;
//import net.minecraft.world.level.block.state.*;
//import team.lodestar.lodestone.modules.rendering.particle.standard.world.options.*;
//import team.lodestar.lodestone.modules.rendering.particle.standard.world.type.*;
//
//import java.util.function.*;
//
//public class LodestoneTerrainParticleOptions extends WorldParticleOptions {
//
//    public final BlockState blockState;
//    public final BlockPos blockPos;
//
//    public LodestoneTerrainParticleOptions(ParticleType<LodestoneTerrainParticleOptions> type, BlockState blockState, BlockPos blockPos) {
//        super(type);
//        this.blockState = blockState;
//        this.blockPos = blockPos;
//    }
//
//    public LodestoneTerrainParticleOptions(Supplier<? extends LodestoneTerrainParticleType> type, BlockState blockState, BlockPos blockPos) {
//        this(type.get(), blockState, blockPos);
//    }
//
//    public LodestoneTerrainParticleOptions(ParticleType<LodestoneTerrainParticleOptions> type, BlockState blockState) {
//        this(type, blockState, null);
//    }
//
//    public LodestoneTerrainParticleOptions(Supplier<? extends LodestoneTerrainParticleType> type, BlockState blockState) {
//        this(type.get(), blockState);
//    }
//}