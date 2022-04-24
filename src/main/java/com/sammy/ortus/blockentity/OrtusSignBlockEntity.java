package com.sammy.ortus.blockentity;

import com.sammy.ortus.setup.OrtusBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class OrtusSignBlockEntity extends SignBlockEntity
{
    public OrtusSignBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType()
    {
        return OrtusBlockEntities.SIGN.get();
    }
}
