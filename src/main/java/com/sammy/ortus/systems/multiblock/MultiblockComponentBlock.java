package com.sammy.ortus.systems.multiblock;

import com.sammy.ortus.setup.OrtusBlockEntityRegistry;
import com.sammy.ortus.systems.block.OrtusEntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class MultiblockComponentBlock extends OrtusEntityBlock<MultiBlockComponentEntity> implements IOrtusMultiblockComponent {
    public MultiblockComponentBlock(BlockBehaviour.Properties properties) {
        super(properties);
        setBlockEntity(OrtusBlockEntityRegistry.MULTIBLOCK_COMPONENT);
    }
}
