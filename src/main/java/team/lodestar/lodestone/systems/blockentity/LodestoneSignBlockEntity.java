package team.lodestar.lodestone.systems.blockentity;

import team.lodestar.lodestone.setup.LodestoneBlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LodestoneSignBlockEntity extends SignBlockEntity
{
    public LodestoneSignBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType()
    {
        return LodestoneBlockEntityRegistry.SIGN.get();
    }
}
