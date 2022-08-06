package team.lodestar.lodestone.systems.multiblock;

import team.lodestar.lodestone.setup.LodestoneBlockEntityRegistry;
import team.lodestar.lodestone.systems.block.LodestoneEntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class MultiblockComponentBlock extends LodestoneEntityBlock<MultiBlockComponentEntity> implements ILodestoneMultiblockComponent {
    public MultiblockComponentBlock(BlockBehaviour.Properties properties) {
        super(properties);
        setBlockEntity(LodestoneBlockEntityRegistry.MULTIBLOCK_COMPONENT);
    }
}
