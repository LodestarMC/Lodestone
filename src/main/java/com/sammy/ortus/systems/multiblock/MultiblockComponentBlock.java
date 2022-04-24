package com.sammy.ortus.systems.multiblock;

import com.sammy.ortus.setup.OrtusBlockEntities;
import com.sammy.ortus.systems.block.OrtusBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class MultiblockComponentBlock extends OrtusBlock<MultiBlockComponentEntity> {
    public MultiblockComponentBlock(BlockBehaviour.Properties properties) {
        super(properties);
        setBlockEntity(OrtusBlockEntities.MULTIBLOCK_COMPONENT);
    }
}
