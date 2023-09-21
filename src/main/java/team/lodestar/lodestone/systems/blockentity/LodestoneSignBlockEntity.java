package team.lodestar.lodestone.systems.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.lodestone.setup.LodestoneBlockEntityRegistry;

public class LodestoneSignBlockEntity extends SignBlockEntity {
    public LodestoneSignBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return LodestoneBlockEntityRegistry.SIGN.get();
    }
}
