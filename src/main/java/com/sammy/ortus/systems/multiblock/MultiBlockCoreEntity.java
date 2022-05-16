package com.sammy.ortus.systems.multiblock;

import com.sammy.ortus.systems.blockentity.OrtusBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class MultiBlockCoreEntity extends OrtusBlockEntity implements IMultiBlockCore {

    ArrayList<BlockPos> componentPositions = new ArrayList<>();

    public final MultiBlockStructure structure;

    public MultiBlockCoreEntity(BlockEntityType<?> type, MultiBlockStructure structure, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.structure = structure;
        setupMultiblock(pos);
    }

    @Override
    public MultiBlockStructure getStructure() {
        return structure;
    }

    @Override
    public ArrayList<BlockPos> getComponentPositions() {
        return componentPositions;
    }

    @Override
    public void onBreak(@Nullable Player player) {
        if (player != null && player.isCreative()) {
            return;
        }
        destroyMultiblock(level, worldPosition);
    }
}