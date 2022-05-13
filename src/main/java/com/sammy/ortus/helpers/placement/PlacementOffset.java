package com.sammy.ortus.helpers.placement;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class PlacementOffset {
    private final boolean success;
    private Vec3i pos;
    private Function<BlockState, BlockState> stateTransform;
    private BlockState ghostState;

    private PlacementOffset(boolean success) {
        this.success = success;
        this.pos = BlockPos.ZERO;
        this.stateTransform = Function.identity();
        this.ghostState = null;
    }

    public static PlacementOffset fail() {
        return new PlacementOffset(false);
    }

    public static PlacementOffset success() {
        return new PlacementOffset(true);
    }


    public static PlacementOffset success(Vec3i pos, Function<BlockState, BlockState> transform) {
        return success().at(pos).withTransform(transform);
    }

    public PlacementOffset at(Vec3i pos) {
        this.pos = pos;
        return this;
    }

    public PlacementOffset withTransform(Function<BlockState, BlockState> stateTransform) {
        this.stateTransform = stateTransform;
        return this;
    }

    public PlacementOffset withGhostState(BlockState ghostState) {
        this.ghostState = ghostState;
        return this;
    }

    public boolean isSuccessful() {
        return success;
    }

    public Vec3i getPos() {
        return pos;
    }

    public BlockPos getBlockPos() {
        if (pos instanceof BlockPos)
            return (BlockPos) pos;

        return new BlockPos(pos);
    }

    public Function<BlockState, BlockState> getTransform() {
        return stateTransform;
    }

    public boolean hasGhostState() {
        return ghostState != null;
    }

    public BlockState getGhostState() {
        return ghostState;
    }


}
