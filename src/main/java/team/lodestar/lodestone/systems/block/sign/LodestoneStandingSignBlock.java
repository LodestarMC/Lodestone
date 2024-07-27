package team.lodestar.lodestone.systems.block.sign;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import team.lodestar.lodestone.systems.blockentity.LodestoneSignBlockEntity;

public class LodestoneStandingSignBlock extends StandingSignBlock implements EntityBlock {


    public LodestoneStandingSignBlock(WoodType pWoodType, Properties pProperties) {
        super(pWoodType, pProperties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LodestoneSignBlockEntity(pos, state);
    }
}
