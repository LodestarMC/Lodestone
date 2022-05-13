package com.sammy.ortus.systems.rendering.ghost;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class GhostBlockParams {

    protected final BlockState blockState;
    protected BlockPos blockPos;
    protected Supplier<Float> alphaSupplier;

    private GhostBlockParams(BlockState state) {
        this.blockState = state;
        this.blockPos = BlockPos.ZERO;
        this.alphaSupplier = () -> 1.0F;
    }

    public static GhostBlockParams create(BlockState state) {
        return new GhostBlockParams(state);
    }

    public static GhostBlockParams create(Block block) {
        return create(block.defaultBlockState());
    }

    public GhostBlockParams at(BlockPos pos) {
        this.blockPos = pos;
        return this;
    }

    public GhostBlockParams at(int x, int y, int z) {
        return at(new BlockPos(x, y, z));
    }
    public GhostBlockParams withAlpha(Supplier<Float> alphaSupplier) {
        this.alphaSupplier = alphaSupplier;
        return this;
    }

    public GhostBlockParams withAlpha(float alpha) {
        return withAlpha(() -> alpha);
    }
}
