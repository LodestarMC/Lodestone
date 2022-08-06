package team.lodestar.lodestone.systems.rendering.ghost;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class GhostBlockOptions {

    protected final BlockState blockState;
    protected BlockPos blockPos;
    protected Supplier<Float> alphaSupplier;

    private GhostBlockOptions(BlockState state) {
        this.blockState = state;
        this.blockPos = BlockPos.ZERO;
        this.alphaSupplier = () -> 1.0F;
    }

    public static GhostBlockOptions create(BlockState state) {
        return new GhostBlockOptions(state);
    }

    public static GhostBlockOptions create(Block block) {
        return create(block.defaultBlockState());
    }

    public GhostBlockOptions at(BlockPos pos) {
        this.blockPos = pos;
        return this;
    }

    public GhostBlockOptions at(int x, int y, int z) {
        return at(new BlockPos(x, y, z));
    }

    public GhostBlockOptions withAlpha(Supplier<Float> alphaSupplier) {
        this.alphaSupplier = alphaSupplier;
        return this;
    }
}