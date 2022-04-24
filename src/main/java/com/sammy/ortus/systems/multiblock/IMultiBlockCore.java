package com.sammy.ortus.systems.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public interface IMultiBlockCore {

    ArrayList<BlockPos> getComponentPositions();

    MultiBlockStructure getStructure();

    default void setupMultiblock(BlockPos pos) {
        getStructure().structurePieces.forEach(p -> {
            Vec3i offset = p.offset;
            getComponentPositions().add(pos.offset(offset));
        });
    }

    default void destroyMultiblock(Level level, BlockPos pos) {
        getComponentPositions().forEach(p -> {
            if (level.getBlockEntity(p) instanceof MultiBlockComponentEntity) {
                level.destroyBlock(p, false);
            }
        });
        if (level.getBlockEntity(pos) instanceof MultiBlockCoreEntity) {
            level.destroyBlock(pos, true);
        }
    }
}