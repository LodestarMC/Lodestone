package com.sammy.ortus.block.sign;

import com.sammy.ortus.blockentity.OrtusSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class OrtusWallSignBlock extends WallSignBlock implements EntityBlock
{
    public OrtusWallSignBlock(Properties properties, WoodType type)
    {
        super(properties, type);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OrtusSignBlockEntity(pos,state);
    }
}
