package team.lodestar.lodestone.systems.multiblock;

import net.minecraft.world.level.block.state.BlockBehaviour;
import team.lodestar.lodestone.registry.common.*;
import team.lodestar.lodestone.systems.block.LodestoneEntityBlock;

/**
 * A basic Multiblock component block.
 */
public class MultiblockComponentBlock extends LodestoneEntityBlock<MultiBlockComponentEntity> implements ILodestoneMultiblockComponent {
    public MultiblockComponentBlock(BlockBehaviour.Properties properties) {
        super(properties);
        setBlockEntity(LodestoneBlockEntities.MULTIBLOCK_COMPONENT);
    }
}
